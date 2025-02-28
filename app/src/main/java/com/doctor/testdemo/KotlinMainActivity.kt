package com.doctor.testdemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KotlinMainActivity : AppCompatActivity() {
    private val testString = "大大大"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = testString
        setContentView(textView)
    }
}