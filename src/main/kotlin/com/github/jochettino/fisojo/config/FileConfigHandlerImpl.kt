package com.github.jochettino.fisojo.config

import java.io.FileInputStream
import java.time.Instant
import java.util.*

class FileConfigHandlerImpl constructor(
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
            lastCrTime = Instant.now(),
            pollingFrequency = props.getProperty(
                FISHEYE_POLLING_FREQUENCY,
                FISHEYE_FEAUTH_DEFAULT_VALLUE
            ).toLong()
        )

    private fun buildSlackConfig(props: Properties) =
        SlackConfig(webhookUrl = props.getProperty(SLACK_WEBHOOK_URL))

    private fun loadPropertiesFile(): Properties {
        FileInputStream(configFilename).use { input ->
            return Properties().apply {
                load(input)
            }
        }
    }

    override fun getFisheyeConfig() = fisheyeConfig

    override fun getSlackConfig() = slackConfig

    companion object {
        private const val FISHEYE_FEAUTH = "fisheye.feauth"
        private const val FISHEYE_PROJECT_ID = "fisheye.projectId"
        private const val FISHEYE_BASE_SERVER_URL = "fisheye.baseServerUrl"
        private const val FISHEYE_POLLING_FREQUENCY = "fisheye.polling.frequency"

        private const val SLACK_WEBHOOK_URL = "slack.webhookUrl"

        private const val FISHEYE_FEAUTH_DEFAULT_VALLUE = "30"
    }
}
