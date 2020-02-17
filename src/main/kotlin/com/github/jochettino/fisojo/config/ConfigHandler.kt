package com.github.jochettino.fisojo.config

import java.time.Instant

/**
 * Config handler interface
 */
interface ConfigHandler {
    fun getSlackConfig(): SlackConfig
    fun getFisheyeConfig(): FisheyeConfig

}

data class FisheyeConfig (
    val feauth: String,
    // eg. CR-TEAM
    val projectId: String,
    val baseServerUrl: String,
    var lastCrTime: Instant,
    val pollingFrequency: Long
)

data class SlackConfig (val webhookUrl: String)