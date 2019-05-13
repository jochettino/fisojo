package com.jmlopez.fisojo.dto

import com.google.gson.annotations.SerializedName

data class Json4Kotlin_Base (

    @SerializedName("reviewData") val reviewData : List<ReviewData>
)

data class ReviewData (

    @SerializedName("allowReviewersToJoin") val allowReviewersToJoin : Boolean,
    @SerializedName("description") val description : String,
    @SerializedName("permaId") val permaId : PermaId,
    @SerializedName("metricsVersion") val metricsVersion : Int,
    @SerializedName("type") val type : String,
    @SerializedName("creator") val creator : Creator,
    @SerializedName("state") val state : String,
    @SerializedName("createDate") val createDate : String,
    @SerializedName("name") val name : String,
    @SerializedName("dueDate") val dueDate : String,
    @SerializedName("author") val author : Author,
    @SerializedName("projectKey") val projectKey : String,
    @SerializedName("permaIdHistory") val permaIdHistory : List<String>
)

data class PermaId (

    @SerializedName("id") val id : String
)

data class Creator (

    @SerializedName("displayName") val displayName : String,
    @SerializedName("url") val url : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("avatarUrl") val avatarUrl : String
)

data class Author (

    @SerializedName("avatarUrl") val avatarUrl : String,
    @SerializedName("userName") val userName : String,
    @SerializedName("url") val url : String,
    @SerializedName("displayName") val displayName : String
)