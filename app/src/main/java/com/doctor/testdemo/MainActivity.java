package com.doctor.testdemo;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final String testString = "东方红太阳升";
    private final String testString1 = "东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白东方红太阳升小红小白";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(testString);
        textView.setText(testString1);
        setContentView(textView);
    }
}