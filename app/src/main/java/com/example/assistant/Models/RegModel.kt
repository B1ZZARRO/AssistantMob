package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class RegModel (
    @SerializedName("login") var login : String?,
    @SerializedName("password") var password : String?,
    @SerializedName("name") var name : String?
)