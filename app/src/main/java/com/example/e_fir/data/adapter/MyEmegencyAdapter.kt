package com.example.e_fir.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.e_fir.R
import com.example.e_fir.ui.Activity.EmergencyNumActivity

class MyEmegencyAdapter(
    val emergencyNumActivity: EmergencyNumActivity,
    val eNumList: List<EmergencyNumActivity.EmergencyNum>
) : BaseAdapter() {
    override fun getCount(): Int = eNumList.size

    override fun getItem(position: Int): Any = position
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val vv =
            LayoutInflater.from(emergencyNumActivity).inflate(R.layout.e_num_item, parent, false)
        val e_icon: ImageView = vv.findViewById(R.id.e_icon)
        val e_name: TextView = vv.findViewById(R.id.e_name)
        val e_number: TextView = vv.findViewById(R.id.e_number)

        e_icon.setImageResource(eNumList[position].icon)
        e_name.text = eNumList[position].name
        e_number.text = eNumList[position].number.toString()

        return vv
    }
}