package com.jmlopez.fisojo

import com.jmlopez.fisojo.config.ConfigReaderImpl
import com.jmlopez.fisojo.dto.ReviewData
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    println("Hello, I'm Fisojo!!!")

    val SLEEP_IN_SECS: Long = 30
    val SLEEP_IN_MILIS: Long = SLEEP_IN_SECS*1000

    if (args.size != 1) {
        println("<properties_file> not passed as argument")
        exitProcess(1)
    }

    val configReader = ConfigReaderImpl(args[0])

    val fisheyeHandler = FisheyeHandler(configReader.getFisheyeConfig())
    val slackHandler = SlackHandler(configReader.getSlackConfig())

    while(true) {

        // getting data from fisheye
        var reviewDataListFromServer: List<ReviewData>
        try {
            reviewDataListFromServer = fisheyeHandler.getReviewDataListFromServer()
        } catch (ex: Exception) {
            println("Error getting data from Fisheye: " + ex.message)
            println("Last call: " + fisheyeHandler.lastHttpCall)
            println("Sleeping " + SLEEP_IN_SECS*4 + " secs")
            Thread.sleep(SLEEP_IN_MILIS*4)
            continue
        }

        if (reviewDataListFromServer.isEmpty()) {
            println("New reviews not found")
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
                println("Error sending data to Slack")
                println("Last call: " + slackHandler.lastHttpCall)
                println("Sleeping " + SLEEP_IN_SECS*4 + " secs")
                Thread.sleep(SLEEP_IN_MILIS*4)
                continue
            }
        }

        println("Fisojo is going to sleep $SLEEP_IN_SECS secs")
        Thread.sleep(SLEEP_IN_MILIS)
        println("Fisojo is awaken")
    }

}
