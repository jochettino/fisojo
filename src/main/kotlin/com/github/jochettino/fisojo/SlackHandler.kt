package com.github.jochettino.fisojo

import com.github.jochettino.fisojo.config.FisheyeConfig
import com.github.jochettino.fisojo.config.SlackConfig
import com.github.jochettino.fisojo.dto.ReviewData
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


class SlackHandler constructor(
    private val config: SlackConfig,
    fisheyeConfig: FisheyeConfig
){

    var lastHttpCall = ""
    val fisheyeBaseUrl = fisheyeConfig.baseServerUrl

    fun sendMessageToSlack(review: List<ReviewData>) {
        val formData = review.joinToString(",", """{"attachments":[""", "]}") { it.toSlack() }
        val httpPostRequest = buildHttpPostRequest(formData)
        val httpClient = HttpClients.createDefault()
        val response = httpClient.execute(httpPostRequest)
        if (response.statusLine.statusCode != org.apache.http.HttpStatus.SC_OK) {
            throw Exception("HTTP POST with payload $formData returned ${response.statusLine.statusCode} status code")
        }
    }

    private fun buildHttpPostRequest(json: String): HttpPost {
        val request = HttpPost(config.webhookUrl)
        request.addHeader("Content-type", "application/json")
        request.entity = StringEntity(json)
        lastHttpCall = request.uri.toString()
        return request
    }


    private fun ReviewData.toSlack() =
        """{"title":"${permaId.id}","title_link":"${url()}","author_name":"${authorLink()}","text":"$name"
            |${appendDueDateIfExists()}}""".trimMargin()

    private fun ReviewData.url() = "$fisheyeBaseUrl/cru/${this.permaId.id}"

    private fun ReviewData.appendDueDateIfExists(): String {
        return if(dueDate.isNullOrBlank()) {
            ""
        } else {
            """, "footer":"<!date^${due()}^Due on {date_short_pretty} {time}|a>""""
        }

    }

    private fun ReviewData.due(): Long =
        LocalDateTime.parse(dueDate, FISHEYE_FORMATTER).atZone(ZoneOffset.UTC).toEpochSecond()

    private fun ReviewData.authorLink() = "<$fisheyeBaseUrl/user/${creator.userName}|${creator.userName}>"

    companion object {
        val FISHEYE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)!!
    }
}
