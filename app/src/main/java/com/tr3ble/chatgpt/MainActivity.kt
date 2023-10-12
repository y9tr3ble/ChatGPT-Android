package com.tr3ble.chatgpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etQuestion = findViewById<EditText>(R.id.etQuestion)
        val btnSubmit = findViewById<FloatingActionButton>(R.id.btnSubmit)
        val txtResponse = findViewById<TextView>(R.id.txtResponse)

        val apiClient = ApiClient()

        btnSubmit.setOnClickListener {
            val question = etQuestion.text.toString()
            apiClient.getResponse(question) { response ->
                runOnUiThread {
                    txtResponse.text = response
                }
            }
        }
    }
}