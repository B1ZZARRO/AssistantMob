package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class RequestModel (
    @SerializedName("query") var query : String? = null,
    @SerializedName("key") var key : String? = null,
    @SerializedName("unit") var unit : String? = null
)