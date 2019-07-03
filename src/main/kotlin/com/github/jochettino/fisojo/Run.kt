package com.github.jochettino.fisojo

import com.github.jochettino.fisojo.config.ConfigHandlerImpl
import com.github.jochettino.fisojo.dto.ReviewData
import com.github.jochettino.fisojo.logger.LoggerProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import kotlin.system.exitProcess


fun main(args: Array<String>) {

    val loggerProvider = LoggerProvider()
    val logger = loggerProvider.getLogger("main")

    logger.error("Hello, I'm Fisojo!!!")

    if (args.isEmpty() || args.size > 2) {
        System.err.println("Expected params: <properties_file> [--debug]")
        exitProcess(1)
    }

    var propsFilename: String? = null

    args.forEach {
        if (it == "--debug") {
            loggerProvider.setDefaultLevel(Level.DEBUG)
        } else {
            propsFilename = it
        }
    }

    val configHandler = ConfigHandlerImpl(propsFilename!!)
    val pollingFrequency = configHandler.getFisheyeConfig().pollingFrequency

    val fisheyeHandler = FisheyeHandler(configHandler, loggerProvider)
    val slackHandler =
        SlackHandler(configHandler.getSlackConfig(), configHandler.getFisheyeConfig())

    while(true) {

        // getting data from fisheye
        val reviewDataListFromServer: List<ReviewData>
        try {
            reviewDataListFromServer = fisheyeHandler.getReviewDataListFromServer()
        } catch (ex: Exception) {
            logErrorAndSleep(
                logger,
                "Error getting data from Fisheye",
                ex,
                fisheyeHandler.lastHttpCall,
                pollingFrequency
            )
            continue
        }

        if (reviewDataListFromServer.isNotEmpty()) {
            try {
                slackHandler.sendMessageToSlack(reviewDataListFromServer)
            } catch (ex: Exception) {
                logErrorAndSleep(
                    logger,
                    "Error sending data to Slack",
                    ex,
                    slackHandler.lastHttpCall,
                    pollingFrequency
                )
                continue
            }
        }

        logger.debug("Fisojo is going to sleep $pollingFrequency secs")
        Thread.sleep(pollingFrequency * 1000)
        logger.debug("Fisojo is awaken")
    }

}

fun logErrorAndSleep(
    logger: Logger,
    errorMessage: String,
    exception: Exception,
    lastHttpCall: String,
    pollingFrequency: Long
) {
    logger.error(errorMessage, exception)
    logger.error("Last call: $lastHttpCall")
    logger.error("Sleeping ${pollingFrequency * 4} secs")
    Thread.sleep(pollingFrequency * 4 * 1000)
}
