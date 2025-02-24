package com.doctor.aistring

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val testString = "这是一个测试字符串"
    private val anotherString = "这是另一个测试字符串"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = testString + "\n" + anotherString
        setContentView(textView)
    }
}

