package com.example.assistant.Activites

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.assistant.API.ApiInterface
import com.example.assistant.API.RestApi
import com.example.assistant.Models.RegModel
import com.example.assistant.Models.UsersModel
import com.example.assistant.R
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_reg.*
import kotlinx.android.synthetic.main.activity_reg.btn_auth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegActivity : AppCompatActivity() {

    var sLogin : String = ""
    var sPass : String = ""
    var sName : String = ""
    var rId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)
        loadData()

        btn_auth.setOnClickListener {
            reg()
        }
    }

    private fun saveData() {
        val insertedLogin = edt_name.text.toString()
        val insertedRegId = rId
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("stringRegName_KEY", insertedLogin)
            putInt("IntRegId_KEY", insertedRegId)
        }.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedRegLogin = sharedPreferences.getString("stringRegLogin_KEY", "")
        val savedRegPass = sharedPreferences.getString("stringRegPass_KEY", "")
        val savedRegName = sharedPreferences.getString("stringRegName_KEY", "")
        sLogin = savedRegLogin.toString()
        sPass = savedRegPass.toString()
        sName = savedRegName.toString()
    }

    fun reg() {
        val apiService = RestApi()
        val requestInfo = RegModel(
            login = sLogin,
            password = sPass,
            name = edt_name.text.toString()
        )
        apiService.reg(requestInfo) {
            if (it?.message.toString() == "Ok" && edt_name.text.toString() != "") {
                rId = it?.body?.userID!!.toInt()
                saveData()
                startActivity(Intent(this@RegActivity, MainActivity::class.java))
            }
            else {
                Toast.makeText(this, "Неверное имя пользоваателя", Toast.LENGTH_LONG).show()
            }
        }
    }
}