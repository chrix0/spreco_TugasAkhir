package com.skripsi.spreco.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.classes.SPSource
import com.skripsi.spreco.classes.recHistory

class recycler_link_pembelian (data : List<SPSource>, private val clickListener: (SPSource) -> Unit) : RecyclerView.Adapter<recycler_link_pembelian.myHolder>() {

    private var myData = data
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val namaLink: TextView = itemView.findViewById(R.id.namaLink)
        val tglBuatLink : TextView = itemView.findViewById(R.id.tglBuatLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_link_pembelian,parent,false)
        return myHolder(inflate)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.namaLink.text = "${myData[position].source}"
        holder.tglBuatLink.text = "${myData[position].dateAdded}"
        holder.itemView.setOnClickListener{
            clickListener(myData[position])
        }
    }

    override fun getItemCount(): Int = myData.size

}