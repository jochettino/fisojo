package com.jmlopez.fisojo.config

import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class ConfigHandlerImpl constructor(
    private val configFilename: String
): ConfigHandler {

    private lateinit var fisheyeConfig: FisheyeConfig

    private lateinit var slackConfig: SlackConfig

    private lateinit var props: Properties

    init {
        buildConfigObjects()
    }

    private fun buildConfigObjects() {
        props = loadPropertiesFile()
        fisheyeConfig = buildFisheyeConfig(props)
        slackConfig = buildSlackConfig(props)
    }

    private fun buildFisheyeConfig(props: Properties) =
        FisheyeConfig(
            feauth = props.getProperty(FISHEYE_FEAUTH),
            projectId = props.getProperty(FISHEYE_PROJECT_ID),
            baseServerUrl = props.getProperty(FISHEYE_BASE_SERVER_URL),
            lastCrNumber = props.getProperty(FISHEYE_LAST_CR_NUMBER).toInt(),
            pollingFrequency = props.getProperty(FISHEYE_POLLING_FREQUENCY, FISHEYE_FEAUTH_DEFAULT_VALLUE).toLong()
        )

    private fun buildSlackConfig(props: Properties) = SlackConfig(webhookUrl = props.getProperty(SLACK_WEBHOOK_URL))

    private fun loadPropertiesFile(): Properties {
        FileInputStream(configFilename).use { input ->
            return Properties().apply {
                load(input)
            }
        }
    }

    override fun getFisheyeConfig() = fisheyeConfig

    override fun getSlackConfig() = slackConfig

    override fun updateLastCrNumber(lastCrNumber: Int) {
        props.setProperty(FISHEYE_LAST_CR_NUMBER, lastCrNumber.toString())
        props.store(FileOutputStream(configFilename), null)
    }

    companion object {
        private const val FISHEYE_FEAUTH = "fisheye.feauth"
        private const val FISHEYE_PROJECT_ID = "fisheye.projectId"
        private const val FISHEYE_BASE_SERVER_URL = "fisheye.baseServerUrl"
        private const val FISHEYE_LAST_CR_NUMBER = "fisheye.lastCrNumber"
        private const val FISHEYE_POLLING_FREQUENCY = "fisheye.polling.frequency"

        private const val SLACK_WEBHOOK_URL = "slack.webhookUrl"

        private const val FISHEYE_FEAUTH_DEFAULT_VALLUE = "30"
    }
}
