package com.jmlopez.fisojo.config

import java.io.FileInputStream
import java.util.*

class ConfigReaderImpl constructor(
    private val configFilename: String
): ConfigReader {

    private lateinit var fisheyeConfig: FisheyeConfig
    private lateinit var slackConfig: SlackConfig

    init {
        buildConfigObjects()
    }

    private fun buildConfigObjects() {
        val props = loadPropertiesFile()
        fisheyeConfig = buildFisheyeConfig(props)
        slackConfig = buildSlackConfig(props)
    }

    private fun buildFisheyeConfig(props: Properties) =
        FisheyeConfig(
            feauth = props.getProperty("fisheye.feauth"),
            projectId = props.getProperty("fisheye.projectId"),
            baseServerUrl = props.getProperty("fisheye.baseServerUrl"),
            lastCrNumber = props.getProperty("fisheye.lastCrNumber").toInt(),
            pollingFrequency = props.getProperty("fisheye.polling.frequency", "30").toLong()
        )

    private fun buildSlackConfig(props: Properties) = SlackConfig(webhookUrl = props.getProperty("slack.webhookUrl"))

    private fun loadPropertiesFile(): Properties {
        FileInputStream(configFilename).use { input ->
            return Properties().apply {
                load(input)
            }
        }
    }

    override fun getFisheyeConfig() = fisheyeConfig
    override fun getSlackConfig() = slackConfig
}