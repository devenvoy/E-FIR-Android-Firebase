package com.example.e_fir.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.e_fir.R
import com.example.e_fir.databinding.FragmentPhoneNumberBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class PhoneNumber : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentPhoneNumberBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneNumberBinding.inflate(layoutInflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        binding.btnGetOtp.setOnClickListener {
            if (binding.edtNumber.text.toString().length != 10) {
                Toast.makeText(activity, "Enter Valid Number", Toast.LENGTH_LONG).show()
            } else {
                val bundle = Bundle()
                bundle.putString("phonenumber", "+91${binding.edtNumber.text.toString()}")
                navController.navigate(R.id.action_phoneNumber_to_otpNumber, bundle)
            }
        }

    }

}