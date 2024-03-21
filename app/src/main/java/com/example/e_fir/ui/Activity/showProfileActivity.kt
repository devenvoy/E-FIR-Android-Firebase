package com.example.e_fir.ui.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_fir.databinding.ActivityShowProfileBinding

class showProfileActivity : AppCompatActivity() {

    private val binding: ActivityShowProfileBinding by lazy {
        ActivityShowProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




    }
}