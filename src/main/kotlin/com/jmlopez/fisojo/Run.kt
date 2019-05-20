package com.jmlopez.fisojo

import com.jmlopez.fisojo.config.ConfigReaderImpl
import com.jmlopez.fisojo.dto.ReviewData
import com.jmlopez.fisojo.logger.LoggerProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import kotlin.system.exitProcess

const val SLEEP_IN_SECS: Long = 30

fun main(args: Array<String>) {

    println("Hello, I'm Fisojo!!!")

    if (args.isEmpty() || args.size > 2) {
        System.err.println("Expected params: <properties_file> [--debug]")
        exitProcess(1)
    }

    val loggerProvider = LoggerProvider()
    val logger = loggerProvider.getLogger("main")

    var propsFilename: String? = null

    args.forEach {
        if (it == "--debug") {
            loggerProvider.setDefaultLevel(Level.DEBUG)
        } else {
            propsFilename = it
        }
    }

    val configReader = ConfigReaderImpl(propsFilename!!)

    val fisheyeHandler = FisheyeHandler(configReader.getFisheyeConfig(), loggerProvider)
    val slackHandler = SlackHandler(configReader.getSlackConfig(), configReader.getFisheyeConfig())

    while(true) {

        // getting data from fisheye
        val reviewDataListFromServer: List<ReviewData>
        try {
            reviewDataListFromServer = fisheyeHandler.getReviewDataListFromServer()
        } catch (ex: Exception) {
            logErrorAndSleep(logger, "Error getting data from Fisheye", ex, fisheyeHandler.lastHttpCall)
            continue
        }

        if (reviewDataListFromServer.isNotEmpty()) {
            try {
                slackHandler.sendMessageToSlack(reviewDataListFromServer)
            } catch (ex: Exception) {
                logErrorAndSleep(logger, "Error sending data to Slack", ex, slackHandler.lastHttpCall)
                continue
            }
        }

        logger.debug("Fisojo is going to sleep $SLEEP_IN_SECS secs")
        Thread.sleep(SLEEP_IN_SECS * 1000)
        logger.debug("Fisojo is awaken")
    }

}

fun logErrorAndSleep(logger: Logger, errorMessage: String, exception: Exception, lastHttpCall: String) {
    logger.error(errorMessage, exception)
    logger.error("Last call: $lastHttpCall")
    logger.error("Sleeping ${SLEEP_IN_SECS * 4} secs")
    Thread.sleep(SLEEP_IN_SECS * 4 * 1000)
}
