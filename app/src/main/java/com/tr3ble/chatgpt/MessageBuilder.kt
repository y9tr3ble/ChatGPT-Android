package com.tr3ble.chatgpt

import org.json.JSONArray
import org.json.JSONObject

object MessageBuilder {
    fun buildJsonMessage(question: String): JSONObject {
        return JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put(
                "messages", JSONArray().put(
                    JSONObject()
                        .put("role", "system")
                        .put("content", "You are a helpful assistant")
                ).put(
                    JSONObject()
                        .put("role", "user")
                        .put("content", question)
                )
            )
        }
    }
}