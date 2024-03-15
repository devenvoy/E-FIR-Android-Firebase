package com.example.e_fir.ui.functionality

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.e_fir.R
import com.example.e_fir.data.constants.Companion.complaintList
import com.example.e_fir.data.constants.Companion.gujratDistrict
import com.example.e_fir.data.constants.Companion.stateList
import com.example.e_fir.data.constants.Companion.subComplaintList
import com.example.e_fir.data.constants.Companion.suratpolicestnList
import com.example.e_fir.data.modal.FIR
import com.example.e_fir.databinding.FragmentRegisterFirBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.time.LocalDate


class RegisterFirFragment : Fragment() {

    private lateinit var currentUser: FirebaseUser
    private lateinit var binding: FragmentRegisterFirBinding

    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    lateinit var name: String
    lateinit var number: String
    lateinit var email: String
    lateinit var age: String
    lateinit var gender: String
    lateinit var fname: String
    lateinit var pAddress: String
    lateinit var state: String
    lateinit var district: String
    lateinit var complaintType: String
    lateinit var subComplaintType: String
    lateinit var firDesc: String
    lateinit var policStation: String

    lateinit var imageUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterFirBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = Firebase.auth
        storage = Firebase.storage
        database = Firebase.database
        currentUser = auth.currentUser!!

        val stateAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, stateList)
        val districtAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, gujratDistrict)
        val psAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, suratpolicestnList)
        val complaintAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, complaintList)
        val subComAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, subComplaintList)
        binding.state.setAdapter(stateAdapter)
        binding.district.setAdapter(districtAdapter)
        binding.policestn.setAdapter(psAdapter)
        binding.compNature.setAdapter(complaintAdapter)
        binding.subcompNature.setAdapter(subComAdapter)

        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {

                val myDBRef = database.getReference("FIR/Data/${currentUser.uid}").push()

                val today = LocalDate.now()

                val fir = FIR(
                    name = name,
                    number = number,
                    email = email,
                    age = age,
                    gender = gender,
                    fatherName = fname,
                    address = pAddress,
                    state = state,
                    district = district,
                    polishStation = policStation,
                    complaint = complaintType,
                    subComType = subComplaintType,
                    firContent = firDesc,
                    signImg = imageUrl,
                    registerDate = today.toString(),
                    status = "PENDING"
                )

                myDBRef.setValue(fir)

                showToast("Success")
                requireActivity().finish()
            } else {
                showToast("Fill Form Properly")
            }
        }

        binding.upldSign.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

    }


    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uploadImageToFirebaseStorage(it)
            }
        }


    private fun validateForm(): Boolean {
        resetError()
        name = binding.name.text.toString()
        email = binding.email.text.toString()
        number = binding.number.text.toString()
        age = binding.age.text.toString()
        val rbtn: RadioButton =
            requireActivity().findViewById(binding.gendergroup.checkedRadioButtonId)
        gender = rbtn.text.toString()
        fname = binding.fname.text.toString()
        pAddress = binding.address.text.toString()
        state = binding.state.text.toString()
        district = binding.district.text.toString()
        complaintType = binding.compNature.text.toString()
        subComplaintType = binding.subcompNature.text.toString()
        policStation = binding.policestn.text.toString()
        firDesc = binding.firContent.text.toString()

        Log.e(
            "====",
            "validateForm: \n$name \n$email \n$number \n$age \n$gender \n$fname \n$pAddress \n$state \n$district \n$complaintType \n$subComplaintType \n$policStation \n$firDesc"
        )

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

            fname.isEmpty() -> {
                binding.fnamel.error = "Enter Fanther's name"
                false
            }

            pAddress.isEmpty() -> {
                binding.addressl.error = "Enter Your Current Living Address"
                false
            }

            state.isEmpty() -> {
                showToast("Select State To Proceed")
                false
            }

            district.isEmpty() -> {
                showToast("Select District To Proceed")
                false
            }

            policStation.isEmpty() -> {
                showToast("Select Police Station Register FIR")
                false
            }

            complaintType.isEmpty() -> {
                showToast("Select Complaint Type")
                false
            }

            subComplaintType.isEmpty() -> {
                showToast("Select Sub Complaint Type")
                false
            }

            else -> {
                true
            }
        }

    }

    private fun resetError() {
        binding.namel.error = ""
        binding.numberl.error = ""
        binding.emaill.error = ""
        binding.agel.error = ""
        binding.fnamel.error = ""
        binding.addressl.error = ""
        binding.firContentl.error = ""
    }


    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun uploadImageToFirebaseStorage(uri: Uri) {


        val contentResolver = requireContext().contentResolver
        val imageBytes = uri.toBytes(contentResolver)

        // Create a reference to the file in Firebase Storage
        val storageRef = storage.reference.child("images/${File(uri.path!!).name}")

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
                    val downloadUri = task.result
                    imageUrl = downloadUri.toString()
                    Log.e("====", "submit: $imageUrl")
                }

            }


        }.addOnFailureListener { exception ->

            Log.e("====", "error: $exception")

        }
    }


    fun Uri.toBytes(contentResolver: ContentResolver): ByteArray {
        val inputStream: InputStream? = contentResolver.openInputStream(this)
        val byteBuffer = ByteArrayOutputStream()

        inputStream?.use { input ->
            val buffer = ByteArray(1024)
            var len: Int
            while (input.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
        }

        return byteBuffer.toByteArray()
    }


}