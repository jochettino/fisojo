package com.jmlopez.fisojo

import com.jmlopez.fisojo.config.ConfigReaderImpl
import com.jmlopez.fisojo.dto.ReviewData
import com.jmlopez.fisojo.logger.LoggerProvider
import org.apache.logging.log4j.Level
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    println("Hello, I'm Fisojo!!!")

    val SLEEP_IN_SECS: Long = 30
    val SLEEP_IN_MILIS: Long = SLEEP_IN_SECS*1000

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
    val slackHandler = SlackHandler(configReader.getSlackConfig())

    while(true) {

        // getting data from fisheye
        var reviewDataListFromServer: List<ReviewData>
        try {
            reviewDataListFromServer = fisheyeHandler.getReviewDataListFromServer()
        } catch (ex: Exception) {
            logger.error("Error getting data from Fisheye: ${ex.message}")
            logger.error("Last call: ${fisheyeHandler.lastHttpCall}")
            logger.error("Sleeping ${SLEEP_IN_SECS*4} secs")
            Thread.sleep(SLEEP_IN_MILIS*4)
            continue
        }

        if (reviewDataListFromServer.isEmpty()) {
            logger.info("New reviews not found")
        } else {

            var messageForSending = ""

            reviewDataListFromServer.forEach { review ->
                val id = review.permaId.id
                val username = review.author.userName
                val name = review.name
                val str = "New CR by ${fisheyeHandler.createLinkTextForUsername(username)}: ${fisheyeHandler.createLinkTextForReviewId(name, id)}"
                println(str)
                messageForSending += str + "\n"
            }

            // sending data to slack
            try {
                slackHandler.sendMessageToSlack(messageForSending)
            } catch (ex: Exception) {
                logger.error("Error sending data to Slack")
                logger.error("Last call: ${slackHandler.lastHttpCall}")
                logger.error("Sleeping ${SLEEP_IN_SECS*4} secs")
                Thread.sleep(SLEEP_IN_MILIS*4)
                continue
            }
        }

        logger.info("Fisojo is going to sleep $SLEEP_IN_SECS secs")
        Thread.sleep(SLEEP_IN_MILIS)
        logger.info("Fisojo is awaken")
    }

}
