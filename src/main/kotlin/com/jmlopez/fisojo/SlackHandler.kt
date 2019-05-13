package com.jmlopez.fisojo

import com.jmlopez.fisojo.config.SlackConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients

class SlackHandler constructor(
    private val config: SlackConfig
){

    var lastHttpCall = ""

    fun sendMessageToSlack(message: String) {
        val httpPostRequest = buildHttpPostRequest(messageToJson(message))
        val httpClient = HttpClients.createDefault()
        val response = httpClient.execute(httpPostRequest)
        if (response.statusLine.statusCode != org.apache.http.HttpStatus.SC_OK) {
            throw Exception("HTTP POST to $httpPostRequest returned ${response.statusLine.statusCode} status code")
        }
    }

    private fun buildHttpPostRequest(json: String): HttpPost {
        val request = HttpPost(config.webhookUrl)
        request.addHeader("Content-type", "application/json")
        request.entity = StringEntity(json)
        lastHttpCall = request.uri.toString()
        return request
    }

    private fun messageToJson(message: String) =
            """{"text":"$message"}"""

}