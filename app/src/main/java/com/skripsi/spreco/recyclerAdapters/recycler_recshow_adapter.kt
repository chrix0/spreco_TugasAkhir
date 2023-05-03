package com.skripsi.spreco.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.classes.SP_rank
import com.skripsi.spreco.classes.recHistory
import com.skripsi.spreco.data

class recycler_recshow_adapter(data : List<SP_rank>, private val clickListener: (SP_rank) -> Unit) : RecyclerView.Adapter<recycler_recshow_adapter.myHolder>() {
    private var myData = data
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val rank: TextView = itemView.findViewById(R.id.rank)
        val nama : TextView = itemView.findViewById(R.id.nama)
        val harga : TextView = itemView.findViewById(R.id.harga)
        val skor : TextView = itemView.findViewById(R.id.skor)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_rekomendasi_res,parent,false)
        return myHolder(inflate)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.rank.text = "${myData[position].rank}"
        holder.nama.text = "${myData[position].obj_sp.namaSP}"
        holder.harga.text = "Rp.${data.formatHarga(myData[position].obj_sp.harga)}"
        holder.skor.text = "${String.format("%.2f", myData[position].score * 100)}"
        holder.itemView.setOnClickListener{
            clickListener(myData[position])
        }
    }

    override fun getItemCount(): Int = myData.size
}