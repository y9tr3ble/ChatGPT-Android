package com.tr3ble.chatgpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.color.DynamicColors
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val buttonSubmit = findViewById<FloatingActionButton>(R.id.buttonSubmit)
        val textResponse = findViewById<TextView>(R.id.textResponse)

        val apiClient = ApiClient()

        buttonSubmit.setOnClickListener {
            val question = etQuestion.text.toString()
            apiClient.getResponse(question) { response ->
                runOnUiThread {
                    textResponse.text = response
                }
            }
        }
    }
}