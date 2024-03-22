package com.example.e_fir.ui.Activity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_fir.data.modal.User
import com.example.e_fir.databinding.ActivityShowProfileBinding
import com.google.gson.Gson

class showProfileActivity : AppCompatActivity() {

    private val binding: ActivityShowProfileBinding by lazy {
        ActivityShowProfileBinding.inflate(layoutInflater)
    }

    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        editor = sharedPref.edit()

        val json = sharedPref.getString("user", null)
        user = Gson().fromJson(json, User::class.java)

        Glide.with(this@showProfileActivity).load(user.userDp).into(binding.profileImage)
        binding.pName.text = user.NAME
        binding.pNumber.text = "+91 " + user.NUMBER
        binding.pEmail.text = user.EMAIL
        binding.pAge.text = user.AGE
        binding.pGender.text = user.GENDER
        binding.pCitizen.text = user.CITIZENSHIP
        binding.pAddress.text = "${user.ADDRESS} , ${user.STATE} , ${user.COUNTRY}"
        binding.pPincode.text = user.PINCODE
        binding.pProof.text = user.IDPROOFTYPE
        binding.pProofNum.text = user.IDPROOFNUM

        binding.backArrow.setOnClickListener {
            finish()
        }


        binding.btnUpdate.setOnClickListener {
            user.filled = false
            val gson = Gson()
            val json = gson.toJson(user)
            editor.putString("user", json)
            editor.commit()
            Toast.makeText(
                this@showProfileActivity,
                "Proper Restart Application to Update Profile",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

    }

}