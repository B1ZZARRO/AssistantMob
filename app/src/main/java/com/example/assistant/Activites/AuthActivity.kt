package com.example.assistant.Activites

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assistant.API.ApiInterface
import com.example.assistant.API.RestApi
import com.example.assistant.Models.*
import com.example.assistant.R
import com.example.assistant.Recycler.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.btn_auth
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_reg.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class AuthActivity : AppCompatActivity() {

    var sLogin : String = ""
    var sPass : String = ""
    var sName : String = ""
    var sId : Int = -1
    var rId : Int = -1
    var rLogin : String = ""
    var rPass : String = ""
    var rName : String = ""
    var sChecked : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        loadData()
        txt_reg.setOnClickListener {
            login()
        }
        btn_auth.setOnClickListener {
            if (chb_remember.isChecked) {
                sLogin = edt_login.text.toString()
                sPass = edt_password.text.toString()
                sChecked = true
                rName = ""
                rId = -1
                saveData()
            }
            else {
                sLogin = ""
                sPass = ""
                rName = ""
                rId = -1
                sChecked = false
                saveData()
            }
            auth()
        }
        txt_enter.setOnClickListener {
            sId = -1
            rId = -1
            sName = ""
            rName = ""
            saveData()
            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
        }
    }

    fun auth() {
        val apiService = RestApi()
        val requestInfo = AuthModel(
            login = edt_login.text.toString(),
            password = edt_password.text.toString()
        )
        apiService.auth(requestInfo) {
            if (it?.message.toString() == "Ok") {
                sName = it?.body?.name.toString()
                sId = it?.body?.userID!!
                saveData()
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
            }
            else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun login() {
        val builder = Retrofit.Builder()
            .baseUrl("http://gopher-server.xyz:49166/api/Main/")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val apiInterface : ApiInterface = retrofit.create<ApiInterface>(ApiInterface::class.java)
        val call : Call<UsersModel> = apiInterface.Login(edt_login.text.toString())
        call.enqueue(object : Callback<UsersModel> {
            override fun onFailure(call: Call<UsersModel>, t: Throwable) {
                Log.i("TAGGetRepFail", t.message.toString())
            }
            override fun onResponse(call: Call<UsersModel>, response: Response<UsersModel>) {
                val statusResponse = response.body()!!
                if (edt_login.text.toString() == "" || edt_password.text.toString() == "") {
                    Toast.makeText(this@AuthActivity, "Не все поля заполнены", Toast.LENGTH_LONG).show()
                }
                else {
                    try {
                        if (edt_login.text.toString() == statusResponse.body[0].login) {
                            Toast.makeText(this@AuthActivity, "Данный логин уже используется", Toast.LENGTH_LONG).show()
                        }
                    }
                    catch (e: Exception) {
                        rLogin = edt_login.text.toString()
                        rPass = edt_password.text.toString()
                        sId = -1
                        saveData()
                        startActivity(Intent(this@AuthActivity, RegActivity::class.java))
                    }
                }
            }
        })
    }

    private fun saveData() {
        val insertedLogin = sLogin
        val insertedPass = sPass
        val insertedName = sName
        val insertedId = sId
        val insertedRegId = rId
        val insertedRegLogin = rLogin
        val insertedRegPass = rPass
        val insertedRegName = rName
        val insertedChecked = sChecked
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("stringLogin_KEY", insertedLogin)
            putString("stringPass_KEY", insertedPass)
            putString("stringName_KEY", insertedName)
            putInt("IntId_KEY", insertedId)
            putInt("IntRegId_KEY", insertedRegId)
            putString("stringRegLogin_KEY", insertedRegLogin)
            putString("stringRegPass_KEY", insertedRegPass)
            putString("stringRegName_KEY", insertedRegName)
            putBoolean("stringChecked_KEY", insertedChecked)
        }.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedLogin = sharedPreferences.getString("stringLogin_KEY", "")
        val savedPass = sharedPreferences.getString("stringPass_KEY", "")
        val savedChecked = sharedPreferences.getBoolean("stringChecked_KEY", false)
        edt_login.setText(savedLogin)
        edt_password.setText(savedPass)
        if (savedChecked == true) {
            chb_remember.isChecked = true
        }
    }
}