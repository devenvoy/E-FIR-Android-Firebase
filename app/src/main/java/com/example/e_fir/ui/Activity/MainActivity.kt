package com.example.e_fir.ui.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.e_fir.R
import com.example.e_fir.databinding.ActivityMainBinding
import com.example.e_fir.ui.functionality.BlankFragment
import com.example.e_fir.ui.functionality.DownloadFirFragment
import com.example.e_fir.ui.functionality.NearestStationFragment
import com.example.e_fir.ui.functionality.RegisterFirFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        if (auth.currentUser != null) {
            val key = intent.getStringExtra("key")
            when (key) {
                "11" -> {
                    replaceFragment(RegisterFirFragment())
                }

                "12" -> {
                    replaceFragment(DownloadFirFragment())
                }

                "51" -> {
                    replaceFragment(NearestStationFragment())
                }

                else -> {
                    replaceFragment(BlankFragment())
                }
            }
        } else {
            replaceFragment(BlankFragment())
        }
    }

    fun replaceFragment(currentFrag: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, currentFrag)
            .commit()
    }

}