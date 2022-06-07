package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class DataModel (
    @SerializedName("question") var question : Boolean? = null,
    @SerializedName("stream") var stream : String? = null,
    @SerializedName("trackId") var trackId : Int? = null,
    @SerializedName("newSessionStarted") var newSessionStarted : Boolean? = null,
    @SerializedName("action") var action : String? = null
)