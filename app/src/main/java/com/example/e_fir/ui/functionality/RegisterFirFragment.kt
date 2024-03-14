package com.example.e_fir.ui.functionality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.e_fir.R
import com.example.e_fir.databinding.FragmentRegisterFirBinding

class RegisterFirFragment : Fragment() {

    private lateinit var binding: FragmentRegisterFirBinding


    val stateList = arrayOf(
        "Gujarat",
        "Maharashtra",
        "Rajasthan",
        "Delhi",
        "Madhya Pradesh",
        "Punjab",
        "Jammu & Kashmir",
        "Karnataka",
        "Kolkata",
        "Uttar Pradesh"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterFirBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val stateAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, stateList)
        binding.state.setAdapter(stateAdapter)

    }


}