package com.zeinab.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       var splash: Button=findViewById(R.id.splash);
        splash.setOnClickListener {
            val i = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(i)
        }
    }
}