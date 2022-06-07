package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("query") var query : String? = null,
    @SerializedName("text") var text : String? = null,
    @SerializedName("action") var action : String? = null,
    @SerializedName("intent") var intent : String? = null,
    @SerializedName("question") var question : Boolean? = null,
    @SerializedName("data") var data : DataModel?  = DataModel()
)