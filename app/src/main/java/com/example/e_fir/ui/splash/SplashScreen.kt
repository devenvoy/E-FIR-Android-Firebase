package com.example.e_fir.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.e_fir.R
import com.example.e_fir.ui.auth.SignIn
import com.example.e_fir.ui.home.HomePage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser == null) {
                startActivity(Intent(this@SplashScreen, SignIn::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreen, HomePage::class.java))
            }
        }, 2000)

    }
}