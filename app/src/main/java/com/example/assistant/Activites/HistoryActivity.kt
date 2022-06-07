package com.example.assistant.Activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assistant.API.ApiInterface
import com.example.assistant.Models.HistoryBodyModel
import com.example.assistant.Models.HistoryModel
import com.example.assistant.Models.UsersModel
import com.example.assistant.R
import com.example.assistant.Recycler.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.btn_auth
import kotlinx.android.synthetic.main.recyclerview_item.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import java.lang.Exception


class HistoryActivity : AppCompatActivity() {

    var id : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        btn_auth.setOnClickListener {
            startActivity(Intent(this@HistoryActivity, AuthActivity::class.java))
        }
        loadData()
        if (id != -1) {
            txt_info.visibility = View.INVISIBLE
            btn_auth.visibility = View.INVISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)
            history()
        } else {
            recyclerView.visibility = View.INVISIBLE
        }
    }

    fun history() {
        val builder = Retrofit.Builder()
            .baseUrl("http://gopher-server.xyz:49166/api/Main/")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val apiInterface : ApiInterface = retrofit.create<ApiInterface>(ApiInterface::class.java)
        val call : Call<HistoryModel> = apiInterface.History(id)
        call.enqueue(object : Callback<HistoryModel> {
            override fun onFailure(call: Call<HistoryModel>, t: Throwable) {
                Log.i("TAGGetRepFail", t.message.toString())
            }
            override fun onResponse(call: Call<HistoryModel>, response: Response<HistoryModel>) {
                val statusResponse = response.body()!!
                val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(applicationContext)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager
                recyclerView.adapter = RecyclerAdapter(statusResponse.body)
                (0..statusResponse.body.size - 1).forEach { i ->
                    Log.i("TAGGetHis", "ID: ${statusResponse.body[i].historyID.toString()}")
                    Log.i("TAGGetHis", "Здание: ${statusResponse.body[i].query.toString()}")
                    Log.i("TAGGetHis", "Пара: ${statusResponse.body[i].response.toString()}")
                    Log.i("TAGGetHis", "Кабинет: ${statusResponse.body[i].userID.toString()}")
                    Log.i("TAGGetHis", "Описание: ${statusResponse.body[i].date.toString()}")
                }
            }
        })
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val SavedId = sharedPreferences.getInt("Id_KEY", -1)
        id =  SavedId
    }
}