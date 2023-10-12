package com.tr3ble.chatgpt

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ApiClient {
    private val client = OkHttpClient()

    fun getResponse(question: String, callback: (String) -> Unit) {
        val json = MessageBuilder.buildJsonMessage(question)
        val requestBody = json.toString()
        val request = RequestBuilder.buildRequest(Constants.API_KEY, Constants.GPT_URL, requestBody)

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
                    val textResult =
                        jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                    callback(textResult)
                } else {
                    Log.e("data", "empty")
                }
            }
        })
    }
}