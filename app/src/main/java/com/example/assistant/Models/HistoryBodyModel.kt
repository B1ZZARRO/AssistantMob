package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class HistoryBodyModel (
    @SerializedName("historyID") var historyID : Int?,
    @SerializedName("query") var query : String?,
    @SerializedName("response") var response : String?,
    @SerializedName("userID") var userID : Int?,
    @SerializedName("date") var date : String?
)