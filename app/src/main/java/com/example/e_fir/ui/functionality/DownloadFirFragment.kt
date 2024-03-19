package com.example.e_fir.ui.functionality

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.e_fir.R
import com.example.e_fir.data.modal.FIR
import com.example.e_fir.databinding.FragmentDownloadFirBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import java.time.LocalDate


class DownloadFirFragment : Fragment() {

    private lateinit var binding: FragmentDownloadFirBinding
    private val auth = Firebase.auth
    private val cUser = auth.currentUser

    var query: Query = FirebaseDatabase.getInstance()
        .getReference()
        .child("FIR/Data/${cUser!!.uid}")

    var options: FirebaseRecyclerOptions<FIR?> = FirebaseRecyclerOptions.Builder<FIR>()
        .setQuery(query, FIR::class.java)
        .build()

    var adapter = object : FirebaseRecyclerAdapter<FIR, FirHolder?>(this.options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.fir_item, parent, false)
            return FirHolder(view)
        }

        override fun onBindViewHolder(holder: FirHolder, position: Int, model: FIR) {
            holder.txtid.text = model.ID
            holder.txtName.text = model.name
            holder.txtComplaint.text = model.complaint
            holder.txtDate.text = model.registerDate
            holder.txtStatus.text = model.status
            when (model.status) {
                "Pending" -> holder.txtStatus.setTextColor(resources.getColor(R.color.red))
                "Accepted" -> holder.txtStatus.setTextColor(resources.getColor(R.color.green))
                "Completed" -> holder.txtStatus.setTextColor(resources.getColor(R.color.blue))
            }
        }
    }

    class FirHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtid: TextView
        var txtName: TextView
        var txtComplaint: TextView
        var txtDate: TextView
        var txtStatus: TextView

        init {
            txtid = itemView.findViewById(R.id.fid)
            txtName = itemView.findViewById(R.id.fname)
            txtComplaint = itemView.findViewById(R.id.fcomtype)
            txtDate = itemView.findViewById(R.id.frdate)
            txtStatus = itemView.findViewById(R.id.fstatus)
        }
    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }

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
        }

        binding.endDate.setOnClickListener {
            showDatePickerDialog(requireActivity(), binding.endDate)
        }

        binding.firRecyclerView.adapter = adapter
        changeFirList()

    }


    private fun changeFirList() {

        val sDate = binding.startDate.text.toString()
        val eDate = binding.endDate.text.toString()

        query = FirebaseDatabase.getInstance()
            .getReference()
            .child("FIR/Data/${cUser!!.uid}")
            .orderByChild("registerDate")
            .startAt(sDate)
            .endAt(eDate)


        options = FirebaseRecyclerOptions.Builder<FIR>()
            .setQuery(query, FIR::class.java)
            .build()


        adapter.updateOptions(options)
        adapter.notifyDataSetChanged()


    }


    private fun getCurrentDate(): LocalDate {
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
                val selectedDate =
                    LocalDate.of(selectedYear, selectedMonth + 1, selectedDay).toString()
                if ((selectedDate <= getCurrentDate().toString()) || (selectedDate <= binding.endDate.text.toString())) {
                    editText.setText(selectedDate)
                    changeFirList()
                } else {
                    Toast.makeText(activity, "This Date Can't Be selected Now", Toast.LENGTH_SHORT)
                        .show()
                }
            }, year, month, day)

        datePickerDialog.show()
    }

}