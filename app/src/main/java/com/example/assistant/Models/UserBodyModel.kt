package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class UserBodyModel (
    @SerializedName("userID") var userID : Int?,
    @SerializedName("login") var login : String?,
    @SerializedName("password") var password : String?,
    @SerializedName("name") var name : String?
)