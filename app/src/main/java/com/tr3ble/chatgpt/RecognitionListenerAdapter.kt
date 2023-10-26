package com.tr3ble.chatgpt

import android.os.Bundle
import android.speech.RecognitionListener

abstract class RecognitionListenerAdapter : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
}
