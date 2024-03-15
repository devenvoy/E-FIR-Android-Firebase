package com.example.e_fir.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.e_fir.R
import com.example.e_fir.data.Singletons.StatesDbHandler
import com.example.e_fir.data.modal.States
import com.example.e_fir.ui.auth.SignIn
import com.example.e_fir.ui.home.HomePage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.math.log

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = Firebase.auth

        val dbhelper = StatesDbHandler.getDb(this@SplashScreen)

        val stateList = dbhelper.statesDao.getAllStateData()

        Log.e("====", stateList.toString())

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