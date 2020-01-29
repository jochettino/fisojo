package com.github.jochettino.fisojo.config

import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

class EnvironmentConfigHandlerImpl: ConfigHandler {

    private lateinit var fisheyeConfig: FisheyeConfig

    private lateinit var slackConfig: SlackConfig

    private lateinit var props: Properties

    init {
        buildConfigObjects()
    }

    private fun buildConfigObjects() {
        props = loadLastCrNumberFile()
        fisheyeConfig = buildFisheyeConfig()
        slackConfig = buildSlackConfig()
    }

    private fun buildFisheyeConfig() =
        FisheyeConfig(
            feauth = System.getenv(FISHEYE_FEAUTH) ?: "",
            projectId = System.getenv(FISHEYE_PROJECT_ID) ?: "",
            baseServerUrl = System.getenv(FISHEYE_BASE_SERVER_URL) ?: "",
            lastCrNumber = props.getProperty(FISHEYE_LAST_CR_NUMBER).toInt(), // TODO: store last cr number in some storage
            pollingFrequency = System.getenv(FISHEYE_POLLING_FREQUENCY).toLong()
        )

    private fun buildSlackConfig() =
        SlackConfig(webhookUrl = System.getenv(SLACK_WEBHOOK_URL))

    override fun getFisheyeConfig() = fisheyeConfig

    override fun getSlackConfig() = slackConfig

    override fun updateLastCrNumber(lastCrNumber: Int) {
        /**
         * TODO: store last cr number in some storage
         */
        props.setProperty(FISHEYE_LAST_CR_NUMBER, lastCrNumber.toString())
        props.store(FileOutputStream(LAST_CR_NUMBER_FILENAME), null)
    }

    private fun loadLastCrNumberFile(): Properties {
        FileInputStream(LAST_CR_NUMBER_FILENAME).use { input ->
            return Properties().apply {
                load(input)
            }
        }
    }

    companion object {
        private const val LAST_CR_NUMBER_FILENAME = "last-cr-number.txt"

        private const val FISHEYE_FEAUTH = "FISHEYE_FEAUTH"
        private const val FISHEYE_PROJECT_ID = "FISHEYE_PROJECT_ID"
        private const val FISHEYE_BASE_SERVER_URL = "FISHEYE_BASE_SERVER_URL"
        private const val FISHEYE_LAST_CR_NUMBER = "FISHEYE_LAST_CR_NUMBER"
        private const val FISHEYE_POLLING_FREQUENCY = "FISHEYE_POLLING_FREQUENCY"

        private const val SLACK_WEBHOOK_URL = "SLACK_WEBHOOK"
    }
}
