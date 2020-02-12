package com.github.jochettino.fisojo.config

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
            lastCrNumber = System.getenv(FISHEYE_LAST_CR_NUMBER)?.toInt() ?: 0,
            pollingFrequency = System.getenv(FISHEYE_POLLING_FREQUENCY)?.toLong() ?: ConfigDefaults.POLLING_FREQUENCY,
            secondsToLookIntoThePast = System.getenv(FISHEYE_SECONDS_TO_LOOK_INTO_THE_PAST)?.toLong()
                    ?: ConfigDefaults.SECONDS_TO_LOOK_INTO_THE_PAST
        )

    private fun buildSlackConfig() =
        SlackConfig(webhookUrl = System.getenv(SLACK_WEBHOOK_URL))

    override fun getFisheyeConfig() = fisheyeConfig

    override fun getSlackConfig() = slackConfig

    override fun updateLastCrNumber(lastCrNumber: Int) {
        /*
          No op, as no persistence is expected when running using ENV config.
         */
    }

    companion object {
        private const val FISHEYE_FEAUTH = "FISHEYE_FEAUTH"
        private const val FISHEYE_PROJECT_ID = "FISHEYE_PROJECT_ID"
        private const val FISHEYE_BASE_SERVER_URL = "FISHEYE_BASE_SERVER_URL"
        private const val FISHEYE_LAST_CR_NUMBER = "FISHEYE_LAST_CR_NUMBER"
        private const val FISHEYE_POLLING_FREQUENCY = "FISHEYE_POLLING_FREQUENCY"
        private const val FISHEYE_SECONDS_TO_LOOK_INTO_THE_PAST = "FISHEYE_SECONDS_TO_LOOK_INTO_THE_PAST"

        private const val SLACK_WEBHOOK_URL = "SLACK_WEBHOOK"
    }
}
