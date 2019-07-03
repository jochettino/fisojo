package com.github.jochettino.fisojo

import com.google.gson.Gson
import com.github.jochettino.fisojo.config.ConfigHandler
import com.github.jochettino.fisojo.config.FisheyeConfig
import com.github.jochettino.fisojo.dto.Json4Kotlin_Base
import com.github.jochettino.fisojo.dto.ReviewData
import com.github.jochettino.fisojo.logger.LoggerProvider
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils


class FisheyeHandler constructor(
    private val configHandler: ConfigHandler,
    loggerProvider: LoggerProvider
){
    private val logger = loggerProvider.getLogger(FisheyeHandler::class.simpleName!!)

    private var config: FisheyeConfig = configHandler.getFisheyeConfig()

    private var baseQueryString: String = "?states=Review&project=${config.projectId}"

    private var lastCrNumberSeen: Int = config.lastCrNumber

    var lastHttpCall: String = ""


    fun getReviewDataListFromServer(): List<ReviewData> {
        val jsonStr = getRawReviewDataFromServer()
        return jsonToReviewDataList(jsonStr)
    }

    private fun getRawReviewDataFromServer(): String {
        val httpGetReq = buildHttpGetRequest()
        val httpClient = HttpClients.createDefault()
        val response = httpClient.execute(httpGetReq)
        return if (response.statusLine.statusCode == org.apache.http.HttpStatus.SC_OK) {
            EntityUtils.toString(response.entity)
        } else {
            throw Exception("HTTP GET from $httpGetReq returned ${response.statusLine.statusCode} status code")
        }
    }

    private fun buildHttpGetRequest(): HttpGet {
        var queryStringParameters = addFromDateToQueryString(baseQueryString)
        queryStringParameters = addFeauthToQueryString(queryStringParameters)
        val request = HttpGet("${config.baseServerUrl}$API_FILTER_URL$queryStringParameters")
        logger.debug("(buildHttpGetRequest) ${request.uri}")
        request.addHeader("Content-type", "application/json")
        request.addHeader("Accept", "application/json")
        lastHttpCall = request.uri.toString()
        return request
    }

    private fun addFromDateToQueryString(queryString: String): String {
        val fromDateValue = System.currentTimeMillis() - (MINUTES_TO_LOOK_TO_THE_PAST * 60 * 1000)
        return "$queryString&fromDate=$fromDateValue"
    }

    private fun addFeauthToQueryString(queryString: String) = "$queryString&FEAUTH=${config.feauth}"

    private fun jsonToReviewDataList(jsonStr: String): List<ReviewData> {
        val base = Gson().fromJson(jsonStr, Json4Kotlin_Base::class.java)
        return base.reviewData.filter { isNewCr(it.permaId.id) }
    }

    private fun isNewCr(id: String): Boolean {
        val idNumber = getIdNumber(id)
        return if (idNumber > lastCrNumberSeen) {
            lastCrNumberSeen = idNumber
            configHandler.updateLastCrNumber(lastCrNumberSeen)
            logger.debug("(isNewCr) updated las ID number seen to $idNumber")
            true
        } else {
            logger.debug("(isNewCr) $idNumber has been published yet, skipping it")
            false
        }
    }

    private fun getIdNumber(id: String) = id.substring(config.projectId.length + 1).toInt()

    companion object {
        // Api doc site
        // https://docs.atlassian.com/fisheye-crucible/latest/wadl/crucible.html?_ga=2.248528775.1036500565.1544699056-786505459.1542885403#d1e897
        private const val API_FILTER_URL = "/rest-service/reviews-v1/filter"
        private const val MINUTES_TO_LOOK_TO_THE_PAST = 1
    }
}