package com.example.e_fir.ui.functionality

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.e_fir.databinding.FragmentDownloadFirBinding
import java.time.LocalDate

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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.startDate.setText(getCurrentDate().toString())
        binding.endDate.setText(getCurrentDate().toString())

        binding.startDate.setOnClickListener {
            showDatePickerDialog(requireActivity(), binding.startDate)
            changeFirList()
        }

        binding.endDate.setOnClickListener {
            showDatePickerDialog(requireActivity(), binding.endDate)
            changeFirList()
        }

    }

    private fun changeFirList() {

    }


    fun getCurrentDate(): LocalDate {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePickerDialog(context: Context, editText: EditText) {
        val currentDate = getCurrentDate()
        val year = currentDate.year
        val month = currentDate.monthValue - 1 // Month value is 1-based
        val day = currentDate.dayOfMonth

        val datePickerDialog =
            DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                // Update the EditText with the selected date
                val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                if (selectedDate < getCurrentDate()) {
                    editText.setText(selectedDate.toString())
                } else {
                    Toast.makeText(activity, "This Date Can't Be selected Now", Toast.LENGTH_SHORT)
                        .show()
                }
            }, year, month, day)

        datePickerDialog.show()
    }

}