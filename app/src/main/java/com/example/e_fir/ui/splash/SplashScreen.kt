package com.example.e_fir.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.e_fir.R
import com.example.e_fir.data.Singletons.StatesDbHandler
import com.example.e_fir.data.constants.Companion.complaintList
import com.example.e_fir.data.constants.Companion.stateList
import com.example.e_fir.data.modal.User
import com.example.e_fir.ui.Activity.ProfileActivity
import com.example.e_fir.ui.auth.SignIn
import com.example.e_fir.ui.home.HomePage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.gson.Gson

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var sharedPref: SharedPreferences
    lateinit var editor: Editor
    lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // initialize auth instance
        auth = Firebase.auth

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        editor = sharedPref.edit()

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

                val json = sharedPref.getString("user", null)
                user = Gson().fromJson(json, User::class.java)

                if(user == null){
                    user = User()
                }

                if (!user.filled) {
                    startActivity(Intent(this@SplashScreen, ProfileActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashScreen, HomePage::class.java))
                    finish()
                }

            }
        }, 2000)
    }
}