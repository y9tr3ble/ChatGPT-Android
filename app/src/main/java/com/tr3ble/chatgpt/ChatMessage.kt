package com.tr3ble.chatgpt

data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long
)