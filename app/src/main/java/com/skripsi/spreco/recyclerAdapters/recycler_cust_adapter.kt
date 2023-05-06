package com.skripsi.spreco.recyclerAdapters

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.*
import com.skripsi.spreco.accountAuth.login
import com.skripsi.spreco.activityAdmin.admin_editCust
import com.skripsi.spreco.activityUser.user_editprofile
import com.skripsi.spreco.classes.Account
import kotlinx.android.synthetic.main.activity_admin_pass_setting.*

class recycler_cust_adapter(data : MutableList<Account>, curContext : Context, val update: () -> Unit ) : RecyclerView.Adapter<recycler_cust_adapter.myHolder>() {
    var myData = data
    var context = curContext
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val customerInfo = itemView.findViewById<TextView>(R.id.customerInfo)
        val deleteUser =  itemView.findViewById<ImageView>(R.id.deleteUser)
        val editUser =  itemView.findViewById<ImageView>(R.id.editUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_manage_customer,parent,false)
        return myHolder(inflate)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        var db = data.getRoomHelper(context)
        holder.customerInfo.text = "${myData[position].username} (${myData[position].idAcc})"
        holder.deleteUser.setOnClickListener{
            var dialog = AlertDialog.Builder(context)
                .setTitle("Pemberitahuan")
                .setMessage("Akun customer dengan username ${myData[position].username} akan dihapus. \n" +
                        "Apakah Anda yakin?")
                .setPositiveButton("Ya") { _, _ ->
                    db.daoAccount().deleteById(myData[position].idAcc)
                    Toast.makeText(context,"Akun customer dengan username ${myData[position].username} telah dihapus.", Toast.LENGTH_SHORT).show()
                    myData.remove(myData[position])
                    update()
                }
                .setNegativeButton("Tidak"){ _, _ -> } //Tutup dialog

            dialog.show()
        }
        holder.editUser.setOnClickListener{
            var intent = Intent(context, admin_editCust::class.java)
            intent.putExtra(SHOW_PROFILE_TO_EDIT, myData[position] as Parcelable)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = myData.size

}