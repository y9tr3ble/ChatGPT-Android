package com.tr3ble.chatgpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val txtResponse = findViewById<TextView>(R.id.txtResponse)

        btnSubmit.setOnClickListener {
            val question = etQuestion.text.toString()
            getResponse(question) { response ->
                runOnUiThread {
                    txtResponse.text = response
                }
            }
        }
    }

    fun getResponse(question: String, callback: (String) -> Unit) {
        val apiKey = "ENTER YOUR API KEY"
        val gptUrl = "https://api.openai.com/v1/chat/completions"

        val json = JSONObject()
        json.put("model", "gpt-3.5-turbo")
        json.put("messages", JSONArray().put(
            JSONObject()
                .put("role", "system")
                .put("content", "You are a helpful assistant")
        ).put(
            JSONObject()
                .put("role", "user")
                .put("content", question)
        ))

        val requestBody = json.toString()

        val request = Request.Builder()
            .url(gptUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "FAILED", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("error", "Response unsuccessful: ${response.code}")
                    return
                }

                val body = response.body?.string()
                if (body != null) {
                    Log.d("data", body)
                    val jsonObject = JSONObject(body)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                    val textResult = jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                    callback(textResult)
                } else {
                    Log.e("data", "empty")
                }
            }
        })
    }
}