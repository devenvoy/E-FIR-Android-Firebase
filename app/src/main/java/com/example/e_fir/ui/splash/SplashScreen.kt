package com.example.e_fir.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.e_fir.R
import com.example.e_fir.data.Singletons.StatesDbHandler
import com.example.e_fir.data.constants.Companion.complaintList
import com.example.e_fir.data.constants.Companion.stateList
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

        // initialize auth instance
        auth = Firebase.auth

        // room database object
        val dbhelper = StatesDbHandler.getDb(this@SplashScreen)

        // get from db and initialize state list
        stateList = dbhelper.statesDao.getAllStateData()

        // get from db and initialize complaint list
        complaintList = dbhelper.statesDao.getComplaints()

        // splash screen method
        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser == null) {
                startActivity(Intent(this@SplashScreen, SignIn::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreen, HomePage::class.java))
                finish()
            }
        }, 2000)

    }


}