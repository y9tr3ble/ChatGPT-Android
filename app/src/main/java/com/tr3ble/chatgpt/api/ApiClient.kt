package com.tr3ble.chatgpt.api

import android.util.Log
import com.tr3ble.chatgpt.chat.MessageBuilder
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class ApiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    fun getResponse(question: String, callback: (String?, String?) -> Unit) {
        val json = MessageBuilder.buildJsonMessage(question)
        val requestBody = json.toString()
        val request = RequestBuilder.buildRequest(Constants.API_KEY, Constants.GPT_URL, requestBody)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, "Ошибка сети. Пожалуйста, проверьте подключение.")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback(null, "Ошибка сервера: ${response.code}")
                    return
                }

                val body = response.body?.string()
                if (body != null) {
                    Log.d("data", body)
                    val jsonObject = JSONObject(body)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("choices")
                    val textResult =
                        jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                    callback(textResult, null)
                } else {
                    callback(null, "Пустой ответ от сервера.")
                }
            }
        })
    }
}