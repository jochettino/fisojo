package com.github.jochettino.fisojo

import com.github.jochettino.fisojo.config.EnvironmentConfigHandlerImpl
import com.github.jochettino.fisojo.config.FileConfigHandlerImpl
import com.github.jochettino.fisojo.dto.ReviewData
import com.github.jochettino.fisojo.logger.LoggerProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    val loggerProvider = LoggerProvider()
    val logger = loggerProvider.getLogger(LoggerProvider.MAIN_LOGGER)

    val noConfigFile = "NO_CONFIG_FILE"
    var propsFilename : String = noConfigFile

    logger.info("Hello, I'm Fisojo!!!")

    args.forEach {
        if (it.startsWith("-")) {
            val flag = it.split("=").first()
            when (flag) {
                "--debug", "d" -> loggerProvider.setDefaultLevel(Level.DEBUG)
                "--file", "-f" -> propsFilename = it.split("=").getOrNull(1) ?: noConfigFile
                "--help", "-h" -> {
                    System.out.println("""
    --debug | -d ) Enable debugging mode, extra logging info
    --file= | -f= ) Read configuration from a file specified
    --help | -h ) Print this text
                    """)
                    exitProcess(0)
                }
            }
        }
    }

    val configHandler = when(propsFilename) {
        noConfigFile -> EnvironmentConfigHandlerImpl()
        else -> FileConfigHandlerImpl(propsFilename)
    }

    val pollingFrequency = configHandler.getFisheyeConfig().pollingFrequency

    val fisheyeHandler = FisheyeHandler(configHandler, loggerProvider)
    val slackHandler =
        SlackHandler(configHandler.getSlackConfig(), configHandler.getFisheyeConfig())

    while(true) {
        val reviewsData: List<ReviewData>

        try {
            reviewsData = fisheyeHandler.getReviewsData()
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

        if (reviewsData.isNotEmpty()) {
            try {
                slackHandler.sendMessageToSlack(reviewsData)
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
