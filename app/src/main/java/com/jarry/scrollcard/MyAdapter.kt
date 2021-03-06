package com.jarry.scrollcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter constructor(private var mList: MutableList<String>) :
    RecyclerView.Adapter<MyAdapter.MyHd>() {

    fun noti(list: MutableList<String>) {
        this.mList.clear()
        this.mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHd {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item, parent, false)
        return MyHd(v)
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: MyHd, position: Int) {
        holder.tv.text = mList[position]
    }


    class MyHd(view: View) : RecyclerView.ViewHolder(view) {
        var tv: TextView = view.findViewById(R.id.tvName)
    }
}