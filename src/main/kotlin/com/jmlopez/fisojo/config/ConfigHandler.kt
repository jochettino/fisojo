package com.jmlopez.fisojo.config

interface ConfigHandler {
    fun getSlackConfig(): SlackConfig
    fun getFisheyeConfig(): FisheyeConfig
    fun updateLastCrNumber(lastCrNumber: Int)
}

data class FisheyeConfig (
    val feauth: String,
    // CR-TEAM
    val projectId: String,
    val baseServerUrl: String,
    // last review now in fisheye
    val lastCrNumber: Int,
    val pollingFrequency: Long
)

data class SlackConfig (val webhookUrl: String)