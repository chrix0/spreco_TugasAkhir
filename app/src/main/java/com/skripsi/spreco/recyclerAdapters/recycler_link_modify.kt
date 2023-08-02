package com.skripsi.spreco.recyclerAdapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.classes.SPSource
import com.skripsi.spreco.data

class recycler_link_modify (data : MutableList<SPSource>, curContext : Context , val update: () -> Unit ) : RecyclerView.Adapter<recycler_link_modify.myHolder>() {
    var myData = data
    var context = curContext
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val namaLink: TextView = itemView.findViewById(R.id.namaLink)
        val urlProduk : TextView = itemView.findViewById(R.id.urlProduk)
        val tglBuatLink : TextView = itemView.findViewById(R.id.tglBuatLink)
        val deleteLink : ImageView = itemView.findViewById(R.id.deleteLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.recycler_link_modify,parent,false)
        return myHolder(inflate) //menyediakan view
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.namaLink.text = "${myData[position].source}"
        holder.tglBuatLink.text = "${myData[position].dateAdded}"
        holder.urlProduk.text = "${myData[position].sourceLink}"
        holder.deleteLink.setOnClickListener{
            myData.remove(myData[position])
            Toast.makeText(context,"Link telah dihapus.", Toast.LENGTH_SHORT).show()
            update()
//            clickListener(myData[position])
        }
    }

    override fun getItemCount(): Int = myData.size
}