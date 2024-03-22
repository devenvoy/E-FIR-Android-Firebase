package com.example.e_fir.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.e_fir.data.modal.User
import com.example.e_fir.databinding.FragmentOtpNumberBinding
import com.example.e_fir.ui.Activity.ProfileActivity
import com.example.e_fir.ui.home.HomePage
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.gson.Gson
import `in`.aabhasjindal.otptextview.OTPListener
import java.util.concurrent.TimeUnit
import kotlin.math.log


class otpNumber : Fragment() {


    private var phonenumber: String = ""

    lateinit var verificationId: String
    private lateinit var token: PhoneAuthProvider.ForceResendingToken
    lateinit var credential: PhoneAuthCredential

    private lateinit var binding: FragmentOtpNumberBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    lateinit var user : User

    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOtpNumberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // auth instance
        auth = Firebase.auth
        database = Firebase.database
        myRef = database.getReference()
        sharedPref = requireActivity().getSharedPreferences("USER_DATA", AppCompatActivity.MODE_PRIVATE)
        editor = sharedPref.edit()



        // get phone number from fragment argument
        phonenumber = arguments?.getString("phonenumber").toString()
        Toast.makeText(activity, phonenumber, Toast.LENGTH_SHORT).show()

        binding.btnVerifyOtp.isEnabled = false
        resendTextCounter()

        binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
                binding.btnVerifyOtp.isEnabled = true
            }
        }

        startPhoneNumberVerification(phonenumber)

        // Verify Otp Button
        binding.btnVerifyOtp.setOnClickListener {
            // get Otp from View
            val otp = binding.otpView.otp
            // create credential with verification id
            val credential = PhoneAuthProvider.getCredential(verificationId, otp.toString())
            // call function to sign In
            signInWithPhoneAuthCredential(credential)
        }

        // resend otp text
        binding.resendOtp.setOnClickListener {
            resendVerificationCode(phonenumber, token)
            resendTextCounter()
        }
    }

    // timer counter function
    fun resendTextCounter() {

        binding.resendOtp.isEnabled = false
        val timew = object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.resendOtp.text = "Resend In :  ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                binding.resendOtp.isEnabled = true
            }
        }
    }


    // resend otp function
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?,
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // (optional) Activity for callback binding
            // If no activity is passed, reCAPTCHA verification can not be used.
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
        Log.e("====", "resendVerificationCode", )
    }


    // callback for verifying captcha / human verification
    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential2: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.e("====", "onVerificationCompleted:$credential2")
            // store credential
            credential = credential2
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.e("====", "onVerificationFailed", e)

            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid request
                }

                is FirebaseTooManyRequestsException -> {
                    // The SMS quota for the project has been exceeded
                }

                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    // reCAPTCHA verification attempted with null Activity
                }
            }

            // Show a message and update the UI
        }

        // add this method manually
        // method will be called when otp has been send
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            // store this data for later use
            verificationId = p0
            token = p1
            Log.e("====", "$p0    $p1")
            Log.e("====", "$verificationId    $token")
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("====", "signInWithCredential:success")


                    val currentUser = task.result?.user!!


                    myRef.child("USERS/Data/${currentUser.uid}").get()
                        .addOnSuccessListener { dataSnapshot ->
                            Log.e("firebase", "Got value ${dataSnapshot.value}")
                            val userData = dataSnapshot.value as? HashMap<String, Any>
                            if (userData != null) {
                                user = User(
                                    ID = userData["id"] as String? ?: "",
                                    NAME = userData["name"] as String? ?: "",
                                    NUMBER = userData["number"] as String? ?: "",
                                    EMAIL = userData["email"] as String? ?: "",
                                    GENDER = userData["gender"] as String? ?: "Male",
                                    AGE = userData["age"] as String? ?: "",
                                    CITIZENSHIP = userData["citizenship"] as String? ?: "",
                                    COUNTRY = userData["country"] as String? ?: "",
                                    STATE = userData["state"] as String? ?: "",
                                    PINCODE = userData["pincode"] as String? ?: "",
                                    ADDRESS = userData["address"] as String? ?: "",
                                    userDp = userData["userDp"] as String?
                                        ?: "https://firebasestorage.googleapis.com/v0/b/e-fir-434f7.appspot.com/o/Users%2Fuser.jpeg?alt=media&token=31579f13-6e85-49e1-85d2-035c7c4965f4",
                                    IDPROOFTYPE = userData["idprooftype"] as String? ?: "",
                                    IDPROOFNUM = userData["idproofnum"] as String? ?: "",
                                    filled = userData["filled"] as Boolean? ?: false,
                                )
                                Log.e("=====", user.toString())
                                val gson = Gson()
                                val json = gson.toJson(user)
                                editor.putString("user", json)
                                editor.commit()

                                if (!user.filled) {
                                    requireActivity().startActivity(Intent(activity, ProfileActivity::class.java))
                                    requireActivity().finish()
                                } else {
                                    requireActivity().startActivity(Intent(activity, HomePage::class.java))
                                    requireActivity().finish()
                                }
                            } else {
                                Log.e("firebase", "User data is null")
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("firebase", "Error getting data", exception)
                        }



                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("====", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

}