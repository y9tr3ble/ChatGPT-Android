package com.tr3ble.chatgpt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VoiceInputHelper(
    private val context: Context,
    private val etQuestion: EditText,
    private val submitButton: FloatingActionButton
) {
    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListenerAdapter() {
                override fun onBeginningOfSpeech() = submitButton.setImageResource(R.drawable.ic_microphone_none_icon)
                override fun onEndOfSpeech() = submitButton.setImageResource(R.drawable.ic_microphone_icon)
                override fun onError(error: Int) = submitButton.setImageResource(R.drawable.ic_microphone_icon)
                override fun onResults(results: Bundle?) {
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        ?.firstOrNull()
                        ?.let(etQuestion::setText)
                }
            })
        }
    }

    fun startVoiceInput() = speechRecognizer.startListening(context.voiceIntent())
    fun destroy() = speechRecognizer.destroy()
}

private fun Context.voiceIntent() = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
}