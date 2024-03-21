package com.example.e_fir.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private lateinit var database: FirebaseDatabase

    lateinit var sharedPref: SharedPreferences
    lateinit var editor: Editor
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // initialize auth instance
        auth = Firebase.auth
        database = Firebase.database

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        editor = sharedPref.edit()

        val dbRef = database.getReference()
        val currentUser = auth.currentUser!!

        dbRef.child("USERS/Data/${currentUser.uid}").get().addOnSuccessListener { dataSnapshot ->
//            Log.i("firebase", "Got value ${dataSnapshot.value}")
            val userData = dataSnapshot.value as? HashMap<String, Any>
            if (userData != null) {
                user = User(
                    ID = userData["id"] as String? ?: "",
                    NAME = userData["name"] as String? ?: "",
                    NUMBER = userData["number"] as String? ?: "",
                    EMAIL = userData["email"] as String? ?: "",
                    GENDER = userData["gender"] as String? ?: "",
                    AGE = userData["age"] as String? ?: "",
                    CITIZENSHIP = userData["citizenship"] as String? ?: "",
                    COUNTRY = userData["country"] as String? ?: "",
                    STATE = userData["state"] as String? ?: "",
                    PINCODE = userData["pincode"] as String? ?: "",
                    ADDRESS = userData["address"] as String? ?: "",
                    userDp = userData["userDp"] as String? ?: "",
                    IDPROOFTYPE = userData["idprooftype"] as String? ?: "",
                    IDPROOFNUM = userData["idproofnum"] as String? ?: "",
                    filled = userData["filled"] as Boolean? ?: false,
                )
//                Log.e("=====", user.toString())
                val gson = Gson()
                val json = gson.toJson(user)
                editor.putString("user", json)
                editor.commit()
            } else {
                Log.e("firebase", "User data is null")
            }
        }.addOnFailureListener { exception ->
            Log.e("firebase", "Error getting data", exception)
        }

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