package com.skripsi.spreco.recyclerAdapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.rotationMatrix
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.skripsi.spreco.R
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.data
import com.squareup.picasso.Picasso

class recycler_sp_adapter(data : List<Smartphone>, context : Context,
                          private val clickListener: (Smartphone) -> Unit) : RecyclerView.Adapter<recycler_sp_adapter.myHolder>() {

    private var context = context
    var myData = data
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val nama: TextView = itemView.findViewById(R.id.title)
        val harga: TextView = itemView.findViewById(R.id.price)
        val gambar: ImageView = itemView.findViewById(R.id.photo)
        val ramRomText: TextView = itemView.findViewById(R.id.ramRomText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_sp,parent,false)
        return myHolder(inflate)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.nama.text = myData[position].namaSP
        holder.harga.text = "Rp." + data.formatHarga(myData[position].harga)

        var ram = "${myData[position].ram} GB"
        //Jika ROM lebih atau sama dengan 1024 GB, maka diubah menjadi TB
        var rom = if (myData[position].rom < 1024){ "${myData[position].rom} GB" }
                    else{ "${myData[position].rom / 1024} TB" }
        holder.ramRomText.text = "RAM: ${ram}\nROM: ${rom}"

        Glide
            .with(holder.itemView.context).asBitmap()
            .load(myData[position].picURL)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .override(600, 600)
            .into(holder.gambar);

        holder.itemView.setOnClickListener{
            clickListener(myData[position])
        }
    }

    override fun getItemCount(): Int = myData.size

}