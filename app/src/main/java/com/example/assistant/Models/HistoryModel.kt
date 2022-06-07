package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class HistoryModel(
    @SerializedName("body") var body : ArrayList<HistoryBodyModel> = arrayListOf(),
    @SerializedName("message") var message : String?
)