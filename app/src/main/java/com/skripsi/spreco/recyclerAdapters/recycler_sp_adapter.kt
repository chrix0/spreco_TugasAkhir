package com.skripsi.spreco.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.data
import com.squareup.picasso.Picasso

class recycler_sp_adapter(data : List<Smartphone>,
                          private val clickListener: (Smartphone) -> Unit) : RecyclerView.Adapter<recycler_sp_adapter.myHolder>() {

    private var myData = data
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nama: TextView = itemView.findViewById(R.id.title)
        val harga: TextView = itemView.findViewById(R.id.price)
        val gambar: ImageView = itemView.findViewById(R.id.photo)
        val area : CardView = itemView.findViewById(R.id.areaitem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_sp,parent,false)
        return myHolder(inflate)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.nama.text = myData[position].namaSP
        holder.harga.text = "Rp." + data.formatHarga(myData[position].harga)
        Picasso.get().load(myData[position].picURL).into(holder.gambar)
        holder.itemView.setOnClickListener{
            clickListener(myData[position])
        }
    }

    override fun getItemCount(): Int = myData.size

}