package com.example.assistant.API

import android.util.Log
import com.example.assistant.Models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestApi {
    fun newRequest(requestData: RequestModel, onResult: (ResponseModel?) -> Unit) {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.Request(requestData).enqueue(
            object : Callback<ResponseModel> {
                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    onResult(response.body())
                }
            }
        )
    }

    fun auth(requestData: AuthModel, onResult: (UserModel?) -> Unit) {
        val retrofit = ServiceBuilderUser.buildServiceUser(ApiInterface::class.java)
        retrofit.Auth(requestData).enqueue(
            object : Callback<UserModel> {
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    onResult(response.body())
                }
            }
        )
    }

    fun reg(requestData: RegModel, onResult: (UserModel?) -> Unit) {
        val retrofit = ServiceBuilderUser.buildServiceUser(ApiInterface::class.java)
        retrofit.Reg(requestData).enqueue(
            object : Callback<UserModel> {
                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    onResult(response.body())
                }
            }
        )
    }

    fun addHistory(requestData: AddHistoryModel, onResult: (HistoryModel?) -> Unit) {
        val retrofit = ServiceBuilderUser.buildServiceUser(ApiInterface::class.java)
        retrofit.AddHistory(requestData).enqueue(
            object : Callback<HistoryModel> {
                override fun onFailure(call: Call<HistoryModel>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<HistoryModel>, response: Response<HistoryModel>) {
                    onResult(response.body())
                }
            }
        )
    }
}