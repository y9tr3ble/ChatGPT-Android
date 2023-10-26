package com.tr3ble.chatgpt

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.color.DynamicColors
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var etQuestion: EditText
    private lateinit var submitButton: FloatingActionButton
    private lateinit var textResponse: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private val apiClient = ApiClient()

    private val voiceInputHelper by lazy {
        VoiceInputHelper(this, etQuestion, submitButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this@MainActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewComponents()
        setupListeners()
        checkRecordPermission()
    }

    private fun initViewComponents() {
        etQuestion = findViewById(R.id.etQuestion)
        submitButton = findViewById(R.id.submitButton)
        textResponse = findViewById(R.id.textResponse)
        progressBar = findViewById(R.id.progressBar)

        pLauncher = registerPermissionListener()

        etQuestion.addTextChangedListener {
            updateMicOrSendIcon(it.toString().isNotEmpty())
        }
    }

    private fun setupListeners() {
        submitButton.setOnClickListener {
            val question = etQuestion.text.toString()
            if (question.isNotEmpty()) {
                startRequest()
                apiClient.getResponse(question) { response, error ->
                    handleApiResponse(response, error)
                }
            } else {
                voiceInputHelper.startVoiceInput()
            }
        }
    }

    private fun handleApiResponse(response: String?, error: String?) {
        runOnUiThread {
            finishRequest()
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            } else {
                textResponse.text = response
            }
        }
    }

    private fun startRequest() {
        progressBar.visibility = View.VISIBLE
    }

    private fun finishRequest() {
        progressBar.visibility = View.GONE
    }

    private fun checkRecordPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            pLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun registerPermissionListener() = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        Toast.makeText(this, if (it) "Record accessed to audio" else "Permission denied", Toast.LENGTH_SHORT).show()
    }

    private fun updateMicOrSendIcon(hasText: Boolean) {
        submitButton.setImageResource(if (hasText) R.drawable.baseline_send_24 else R.drawable.ic_microphone_icon)
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceInputHelper.destroy()
    }
}   