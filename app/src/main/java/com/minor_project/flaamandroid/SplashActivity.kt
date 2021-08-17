package com.minor_project.flaamandroid

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        val tvSplashAppName = findViewById<TextView>(R.id.tv_splash_app_name)
        tvSplashAppName.typeface = typeFace


        Handler().postDelayed({


            startActivity(Intent(this, IntroActivity::class.java))

            finish()

        }, 2500)
    }

}