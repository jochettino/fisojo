package com.github.jochettino.fisojo.config

import org.junit.Test
import kotlin.test.assertEquals

class ConfigReaderImplTest {
    @Test
    fun readConfigHappyCase() {
        val configReaderImpl =
            FileConfigHandlerImpl("src/test/resources/config/config.properties")

        assertEquals("any_webhookUrl", configReaderImpl.getSlackConfig().webhookUrl)
        assertEquals("any_feauth", configReaderImpl.getFisheyeConfig().feauth)
        assertEquals("any_projectId", configReaderImpl.getFisheyeConfig().projectId)
        assertEquals("any_baseServerUrl", configReaderImpl.getFisheyeConfig().baseServerUrl)
        assertEquals(10, configReaderImpl.getFisheyeConfig().pollingFrequency)
    }
}
