package com.minor_project.flaamandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        val btnSignInIntro = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_sign_in_intro)
        val btnRegisterIntro = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_register_intro)

        btnRegisterIntro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnSignInIntro.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }
}