package com.example.assistant.API

import com.example.assistant.Models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("/request/")
    fun Request(
        @Body musData: RequestModel
    ): Call<ResponseModel>

    @POST("Auth/")
    fun Auth(
        @Body authData: AuthModel
    ): Call<UserModel>

    @POST("Reg/")
    fun Reg(
        @Body regData: RegModel
    ): Call<UserModel>

    @GET("History/")
    fun History(
        @Query("id") id: Int
    ): Call<HistoryModel>

    @POST("AddHistory/")
    fun AddHistory(
        @Body historyData: AddHistoryModel
    ): Call<HistoryModel>

    @GET("Login/")
    fun Login(
        @Query("login") login: String
    ): Call<UsersModel>
}