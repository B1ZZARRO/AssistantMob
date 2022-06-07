package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class AuthModel (
    @SerializedName("login") var login : String?,
    @SerializedName("password") var password : String?
)