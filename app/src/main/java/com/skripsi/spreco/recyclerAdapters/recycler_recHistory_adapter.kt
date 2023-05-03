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
import com.skripsi.spreco.classes.recHistory
import com.skripsi.spreco.data
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class recycler_recHistory_adapter(data : List<recHistory>, private val clickListener: (recHistory) -> Unit) : RecyclerView.Adapter<recycler_recHistory_adapter.myHolder>() {
    private var myData = data
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val noRec: TextView = itemView.findViewById(R.id.noRec)
        val tglWaktuRec : TextView = itemView.findViewById(R.id.tglWaktuRec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_rechistory,parent,false)
        return myHolder(inflate)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.noRec.text = "Rekomendasi ${position + 1}"
        holder.tglWaktuRec.text = myData[position].datetime
        holder.itemView.setOnClickListener{
            clickListener(myData[position])
        }
    }

    override fun getItemCount(): Int = myData.size

}