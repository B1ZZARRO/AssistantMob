package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class UserModel (
    @SerializedName("body") var body : UserBodyModel?,
    @SerializedName("message") var message : String?
)