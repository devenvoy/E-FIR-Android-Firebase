package com.example.e_fir.ui.functionality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_fir.R
import com.example.e_fir.databinding.FragmentBlankBinding
import com.example.e_fir.databinding.FragmentDownloadFirBinding

class DownloadFirFragment : Fragment() {

    private lateinit var binding: FragmentDownloadFirBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDownloadFirBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}