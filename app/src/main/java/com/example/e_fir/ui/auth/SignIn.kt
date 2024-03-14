package com.example.e_fir.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_fir.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {

    private val binding by lazy {
        ActivitySignInBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}