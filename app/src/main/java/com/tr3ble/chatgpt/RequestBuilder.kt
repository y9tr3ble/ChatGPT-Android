package com.tr3ble.chatgpt

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object RequestBuilder {

    fun buildRequest(apiKey: String, gptUrl: String, requestBody: String): Request {
        return Request.Builder()
            .url(gptUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
    }
}