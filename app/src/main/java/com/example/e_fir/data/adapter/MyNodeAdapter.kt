package com.example.e_fir.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_fir.R
import com.example.e_fir.data.modal.PageNode
import com.example.e_fir.ui.home.HomePage

class MyNodeAdapter(var homePage: HomePage, var mainList: Array<PageNode>) :
    RecyclerView.Adapter<MyNodeAdapter.MyViewHolder>() {

    var subRvVisible = false

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nodeIcon: ImageView
        var nodeTitle: TextView
        var nodeViewIcon: ImageView
        var subNodeRV: RecyclerView

        init {
            nodeIcon = itemView.findViewById(R.id.pageIcon)
            nodeTitle = itemView.findViewById(R.id.pageName)
            nodeViewIcon = itemView.findViewById(R.id.pageViewIcon)
            subNodeRV = itemView.findViewById(R.id.subRecyclerView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view = homePage.layoutInflater.inflate(R.layout.main_item, parent, false)
        val view = LayoutInflater.from(homePage).inflate(R.layout.main_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = mainList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(homePage).load(mainList[position].pageIcon).into(holder.nodeIcon)
        Glide.with(homePage).load(R.drawable.arrow_down).into(holder.nodeViewIcon)
        holder.nodeTitle.text = mainList[position].title

        val subNodeAdapter = MySubNodeAdapter(homePage, mainList[position].subNodes)
        holder.subNodeRV.adapter = subNodeAdapter

        holder.itemView.setOnClickListener {
            if (subRvVisible) {
                subRvVisible = false
                Glide.with(homePage).load(R.drawable.arrow_down).into(holder.nodeViewIcon)
                holder.subNodeRV.visibility = View.GONE
            } else {
                subRvVisible = true
                Glide.with(homePage).load(R.drawable.arrow_up).into(holder.nodeViewIcon)
                holder.subNodeRV.visibility = View.VISIBLE
            }
        }

    }

}
