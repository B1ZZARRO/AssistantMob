package com.example.assistant.Models

import com.google.gson.annotations.SerializedName

data class UsersModel(
    @SerializedName("body") var body : ArrayList<UserBodyModel> = arrayListOf(),
    @SerializedName("message") var message : String?
)