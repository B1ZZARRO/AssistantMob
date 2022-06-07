package com.example.assistant.Activites

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.assistant.API.RestApi
import com.example.assistant.Models.AddHistoryModel
import com.example.assistant.Models.AuthModel
import com.example.assistant.Models.HistoryBodyModel
import com.example.assistant.Models.RequestModel
import com.example.assistant.R
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_reg.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private val RQ_SPEECH_REC = 102
    val mediaPlayer = MediaPlayer()
    var sName : String = ""
    var rName : String = ""
    var sId : Int = -1
    var rId : Int = -1
    var id : Int = -1
    var sResponse : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
        txt_query.text = ""
        txt_response.text = ""
        if (sId != -1) id = sId
        if (rId != -1) id = rId
        if (sName == "") txt_welcome.text = "Здравсвйте ${rName}! Чем могу помочь?"
        if (rName == "") txt_welcome.text = "Здравсвйте ${sName}! Чем могу помочь?"
        btn_mus.visibility = View.INVISIBLE
        btn_send.setImageResource(R.drawable.ic_baseline_send_24)
        btn_voice.setImageResource(R.drawable.ic_baseline_mic_24)
        btn_mus.setImageResource(R.drawable.ic_baseline_pause_24)
        img_exit.setImageResource(R.drawable.ic_baseline_exit_to_app_24)
        img_history.setImageResource(R.drawable.ic_baseline_article_24)
        btn_voice.setOnClickListener{
            askSpeechInput()
        }
        img_exit.setOnClickListener {
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
        }
        img_history.setOnClickListener {
            saveData()
            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        btn_send.setOnClickListener {
            if (edt_query.text.toString() == "") {
                Toast.makeText(this, "Поле запроса было пустым, отправьте запрос ещё раз", Toast.LENGTH_LONG).show()
            }
            else {
                txt_query.text = edt_query.text.toString().replace('х', 'x')
                edt_query.setText("")
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                }
                btn_mus.setImageResource(R.drawable.ic_baseline_pause_24)
                mediaPlayer.reset()
                txt_response.text = ""
                addNewRequest()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            txt_query.text = result?.get(0).toString().replace('х', 'x')
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
            btn_mus.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer.reset()
            txt_response.text = ""
            addNewRequest()
        }
    }

    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Распознание речи недоступно", Toast.LENGTH_SHORT).show()
        }
        else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ваш запрос")
            startActivityForResult(i,RQ_SPEECH_REC)
        }
    }

    fun addNewRequest() {
        val apiService = RestApi()
        val requestInfo = RequestModel(
            query = txt_query.text.toString(),
            key = "yznU4jvZPKnvAGyLw1jiDbFXU5zdMC8b",
            unit = UUID.randomUUID().toString()
        )
        apiService.newRequest(requestInfo) {
            if (it?.text.toString() == "") {
                txt_response.text = "Запрос не найден, попробуйте найти что-то ещё"
            }
            else {
                txt_response.text = it?.text.toString()
                sResponse = it?.text.toString()
            }
            if (it?.intent.toString() == "/Date and Time/Current time") {
                txt_response.text = SimpleDateFormat("HH:mm").format(Date())
                sResponse = SimpleDateFormat("HH:mm").format(Date())
            }
            if (it?.intent.toString() == "/Music/Init" && it?.text.toString() != "Что-то я не могу найти подходящую музыку. Может включим что-нибудь другое?") {
                btn_mus.visibility = View.VISIBLE
                try {
                    mediaPlayer.setDataSource(it?.data?.stream.toString())
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        mediaPlayer.start()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this, "Ошибка воспроизведения", Toast.LENGTH_LONG).show()
                }
            }
            else btn_mus.visibility = View.INVISIBLE
            btn_mus.setOnClickListener {
                if(mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    btn_mus.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
                else {
                    mediaPlayer.start()
                    btn_mus.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }
            addHistory()
        }
    }

    fun addHistory() {
        val sdf = SimpleDateFormat("dd.MM.yy HH:mm")
        val currentDate = sdf.format(Date())
        val apiService = RestApi()
        val requestInfo = AddHistoryModel(
            query = txt_query.text.toString(),
            response = sResponse,
            userID = id,
            date = currentDate
        )
        apiService.addHistory(requestInfo){
        }
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedName = sharedPreferences.getString("stringName_KEY", "")
        val savedRegName = sharedPreferences.getString("stringRegName_KEY", "")
        val SavedId = sharedPreferences.getInt("IntId_KEY", -1)
        val SavedRegId = sharedPreferences.getInt("IntRegId_KEY", -1)
        sName = savedName.toString()
        rName = savedRegName.toString()
        sId = SavedId
        rId = SavedRegId
    }

    private fun saveData() {
        val insertedId = id
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putInt("Id_KEY", insertedId)
        }.apply()
    }
}