package com.example.e_fir.ui.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_fir.R
import com.example.e_fir.data.adapter.MyEmegencyAdapter
import com.example.e_fir.databinding.ActivityEmergencyNumBinding

class EmergencyNumActivity : AppCompatActivity() {

    private val binding: ActivityEmergencyNumBinding by lazy {
        ActivityEmergencyNumBinding.inflate(layoutInflater)
    }

    // emergency number data class
    data class EmergencyNum(
        val name: String,
        val number: Int,
        val icon: Int,
    )


    // emergency number list
    val eNumList = listOf(
        EmergencyNum("Police", 100, R.drawable.e100),
        EmergencyNum("Fire Brigade", 101, R.drawable.e101),
        EmergencyNum("Ambulance", 102, R.drawable.e102),
        EmergencyNum("Emergency Disaster Management", 108, R.drawable.e108),
        EmergencyNum("Women Helpline", 181, R.drawable.e181),
        EmergencyNum("Child Helpline", 1098, R.drawable.e1098),
        EmergencyNum("Medical Advice on COVID-19", 1075, R.drawable.e1075),
        EmergencyNum("Senior Citizen Helpline", 14567, R.drawable.e14567),
        EmergencyNum("Road Accident Emergency Service", 1033, R.drawable.e1033),
        EmergencyNum("Anti-Poison", 180011555, R.drawable.anti_poison),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // emergency number adapter
        val eAdapter = MyEmegencyAdapter(this@EmergencyNumActivity, eNumList)

        // set emergency number adapter
        binding.eNumList.adapter = eAdapter


        binding.eNumList.setOnItemClickListener { parent, view, position, id ->

            var icall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${eNumList[position].number}"))
            startActivity(icall)

        }

    }
}