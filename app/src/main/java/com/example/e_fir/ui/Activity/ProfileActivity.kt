package com.example.e_fir.ui.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.e_fir.R
import com.example.e_fir.data.constants
import com.example.e_fir.data.modal.User
import com.example.e_fir.databinding.ActivityProfileBinding
import com.example.e_fir.ui.home.HomePage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.util.UUID

class ProfileActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var currentUser: FirebaseUser

    private lateinit var storage: FirebaseStorage

    lateinit var name: String
    lateinit var number: String
    lateinit var email: String
    lateinit var age: String
    lateinit var gender: String
    lateinit var address: String
    lateinit var citizen: String
    lateinit var country: String
    lateinit var pincode: String
    lateinit var idprooftype: String
    lateinit var idproofNum: String
    lateinit var state: String

    var imageUrl: String = ""

    val proofList = listOf("Aadhar Card", "Pan Card")

    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        storage = Firebase.storage
        database = Firebase.database
        // get current user data object
        currentUser = auth.currentUser!!

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        editor = sharedPref.edit()

        val json = sharedPref.getString("user", null)
        var user = Gson().fromJson(json, User::class.java)

        if (user == null) {
            user = User()
        }
        Log.e("====", user.toString())

        Glide.with(this@ProfileActivity).load(user.userDp).into(binding.profileImage)
        binding.name.setText(user.NAME)
        binding.number.setText(user.NUMBER)
        binding.email.setText(user.EMAIL)
        binding.age.setText(user.AGE)
        binding.country.setText(user.COUNTRY)
        binding.citizen.setText(user.CITIZENSHIP)
        binding.state.setText(user.STATE)
        binding.pincode.setText(user.PINCODE)
        binding.address.setText(user.ADDRESS)
        binding.idProofType.setText(user.IDPROOFTYPE)
        binding.idNum.setText(user.IDPROOFNUM)
        imageUrl = user.userDp

        if (user.GENDER.equals("Female")) {
            binding.rbtnmale.isChecked = true
        } else if (user.GENDER.equals("TransGender")) {
            binding.rbtntrans.isChecked = true
        } else {
            binding.rbtnmale.isChecked = true
        }

        val stateAdapter =
            ArrayAdapter(this@ProfileActivity, R.layout.spinner_item, constants.stateList)
        //  set adapter to create state drop down
        binding.state.setAdapter(stateAdapter)

        val proofadapter =
            ArrayAdapter(this@ProfileActivity, R.layout.spinner_item, proofList)
        binding.idProofType.setAdapter(proofadapter)


        binding.profileImage.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        binding.btnSubmit.setOnClickListener {
            // validate form
                Thread.sleep(2000)
            if (validateForm()) {

                // data reference path to store object
                val myDBRef = database.getReference("USERS/Data/${currentUser.uid}")

//                uploadImageToFirebaseStorage()

                // fir object with data
                val user = User(
                    ID = currentUser.uid,
                    NAME = name,
                    NUMBER = number,
                    EMAIL = email,
                    GENDER = gender,
                    AGE = age,
                    CITIZENSHIP = citizen,
                    COUNTRY = country,
                    STATE = state,
                    PINCODE = pincode,
                    ADDRESS = address,
                    userDp = imageUrl,
                    IDPROOFTYPE = idprooftype,
                    IDPROOFNUM = idproofNum,
                    filled = true,
                )

                // if object is stored / submitted than close page
                myDBRef.setValue(user).addOnSuccessListener {
                    showToast("Profile Updated")
                    val gson = Gson()
                    val json = gson.toJson(user)
                    editor.putString("user", json)
                    editor.commit()
                    startActivity(Intent(this@ProfileActivity, HomePage::class.java))
                    finish()
                    // else error message
                }.addOnFailureListener {
                    showToast("Something Went Wrong")
                }
            } else {
                showToast("Fill Form Properly")
            }
        }

    }


    // function to validate form
    private fun validateForm(): Boolean {

        resetError()
        name = binding.name.text.toString()
        email = binding.email.text.toString()
        number = binding.number.text.toString()
        age = binding.age.text.toString()
        val rbtn: RadioButton = findViewById(binding.gendergroup.checkedRadioButtonId)
        gender = rbtn.text.toString()
        address = binding.address.text.toString()
        citizen = binding.citizen.text.toString()
        country = binding.country.text.toString()
        pincode = binding.pincode.text.toString()
        idprooftype = binding.idProofType.text.toString()
        idproofNum = binding.idNum.text.toString()
        state = if (binding.state.text.toString()
                .isEmpty()
        ) "Gujarat" else binding.state.text.toString()


        return when {
            name.isEmpty() -> {
                binding.namel.error = "Enter Your Name"
                false
            }

            number.isEmpty() -> {
                binding.numberl.error = "Enter Your Phone Number"
                false
            }

            email.isEmpty() -> {
                binding.emaill.error = "Enter Email Address"
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emaill.error = "Enter Valid Email Addresss"
                false
            }

            !Patterns.PHONE.matcher(number).matches() -> {
                binding.numberl.error = "Enter Valid Phone Number"
                false
            }

            age.isEmpty() -> {
                binding.agel.error = "Enter Your Age"
                false
            }

            gender.isEmpty() -> {
                showToast("Must Need to Select Gender")
                false
            }


            citizen.isEmpty() -> {
                binding.citizenl.error = "Enter Your CitizenShip"
                false
            }

            country.isEmpty() -> {
                binding.countryl.error = "Enter Your Contry Name"
                false
            }

            state.isEmpty() -> {
                showToast("Select State To Proceed")
                false
            }

            pincode.isEmpty() -> {
                binding.pincodel.error = "Enter Pincode"
                false
            }

            address.isEmpty() -> {
                binding.addressl.error = "Enter Your Current Living Address"
                false
            }

            idprooftype.isEmpty() -> {
                showToast("Select Identity Proof Type")
                false
            }

            idproofNum.isEmpty() -> {
                binding.idNuml.error = "Enter $idprooftype Number"
                false
            }

            imageUrl.isEmpty() -> {
                showToast("Upload Your signature image")
                false
            }

            binding.chkTermsCondition.isChecked == false -> {
                showToast("You Must Agree On Terms And Conditions")
                false
            }

            else -> {
                true
            }
        }

    }


    // function to show toast message
    private fun showToast(msg: String) {
        Toast.makeText(this@ProfileActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun uploadImageToFirebaseStorage() {

        binding.profileImage.isDrawingCacheEnabled = true
        binding.profileImage.buildDrawingCache()
        val bitmap = (binding.profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val imageBytes = baos.toByteArray()

        // Create a reference to the file in Firebase Storage
        val storageRef =
            storage.reference.child("Users/${currentUser.uid}/${generateRandomName()}")

        // Upload the file to Firebase Storage
        val uploadTask = storageRef.putBytes(imageBytes)


        uploadTask.addOnSuccessListener { taskSnapShot ->

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    // uploaded image url
                    val downloadUri = task.result
                    imageUrl = downloadUri.toString()
                    showToast("Image Selected to Upload")
                }
            }

        }.addOnFailureListener { exception ->

            Log.e("====", "error: $exception")

        }
    }


    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.profileImage.setImageURI(uri)
                uploadImageToFirebaseStorage()
            }
        }


    fun generateRandomName(): String {
        val timestamp = System.currentTimeMillis()
        val randomString = UUID.randomUUID().toString().substring(0, 8) // Generate a random string
        return "$timestamp-$randomString.jpg" // Append ".jpg" at the end
    }

    // clear form fields error
    private fun resetError() {
        binding.namel.error = ""
        binding.numberl.error = ""
        binding.emaill.error = ""
        binding.agel.error = ""
        binding.citizenl.error = ""
        binding.addressl.error = ""
        binding.countryl.error = ""
        binding.pincodel.error = ""
        binding.idNuml.error = ""
    }

}