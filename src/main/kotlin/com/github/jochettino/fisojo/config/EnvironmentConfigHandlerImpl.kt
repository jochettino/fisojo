package com.github.jochettino.fisojo.config

import java.time.Instant

class EnvironmentConfigHandlerImpl: ConfigHandler {

    private lateinit var fisheyeConfig: FisheyeConfig

    private lateinit var slackConfig: SlackConfig

    init {
        buildConfigObjects()
    }

    private fun buildConfigObjects() {
        fisheyeConfig = buildFisheyeConfig()
        slackConfig = buildSlackConfig()
    }

    private fun buildFisheyeConfig() =
        FisheyeConfig(
            feauth = System.getenv(FISHEYE_FEAUTH) ?: "",
            projectId = System.getenv(FISHEYE_PROJECT_ID) ?: "",
            baseServerUrl = System.getenv(FISHEYE_BASE_SERVER_URL) ?: "",
            lastCrTime = Instant.now(),
            pollingFrequency = System.getenv(FISHEYE_POLLING_FREQUENCY).toLong()
        )

    private fun buildSlackConfig() =
        SlackConfig(webhookUrl = System.getenv(SLACK_WEBHOOK_URL))

    override fun getFisheyeConfig() = fisheyeConfig

    override fun getSlackConfig() = slackConfig

    companion object {
        private const val FISHEYE_FEAUTH = "FISHEYE_FEAUTH"
        private const val FISHEYE_PROJECT_ID = "FISHEYE_PROJECT_ID"
        private const val FISHEYE_BASE_SERVER_URL = "FISHEYE_BASE_SERVER_URL"
        private const val FISHEYE_POLLING_FREQUENCY = "FISHEYE_POLLING_FREQUENCY"
        private const val SLACK_WEBHOOK_URL = "SLACK_WEBHOOK_URL"
    }
}
