package com.example.e_fir.data.adapter

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_fir.R
import com.example.e_fir.data.modal.SubPageNode
import com.example.e_fir.ui.Activity.MainActivity
import com.example.e_fir.ui.home.HomePage
import kotlinx.coroutines.MainScope

class MySubNodeAdapter(var homePage: HomePage, var subList: List<SubPageNode>) :
    RecyclerView.Adapter<MySubNodeAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var subNodeIcon: ImageView
        var subNodeTitle: TextView

        init {
            subNodeIcon = itemView.findViewById(R.id.subIcon)
            subNodeTitle = itemView.findViewById(R.id.subTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = homePage.layoutInflater.inflate(R.layout.sub_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = subList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(homePage).load(subList[position].pageIcon).into(holder.subNodeIcon)
        holder.subNodeTitle.text = subList[position].title

        holder.itemView.setOnClickListener {
            homePage.startActivity(
                Intent(homePage, MainActivity::class.java).putExtra(
                    "key",
                    subList[position].key
                )
            )
        }
    }

}
