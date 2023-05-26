package com.skripsi.spreco.recyclerAdapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.*
import com.skripsi.spreco.accountAuth.login
import com.skripsi.spreco.activityUser.user_editprofile
import com.skripsi.spreco.classes.Account
import kotlinx.android.synthetic.main.activity_admin_pass_setting.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
class recycler_cust_adapter(data : MutableList<Account>, curContext : Context, val update: () -> Unit ) : RecyclerView.Adapter<recycler_cust_adapter.myHolder>() {
    var myData = data
    var context = curContext
    class myHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val idInfo = itemView.findViewById<TextView>(R.id.idInfo)
        val customerInfo = itemView.findViewById<TextView>(R.id.customerInfo)
        val lastSeenInfo = itemView.findViewById<TextView>(R.id.lastSeenInfo)
        val deleteUser =  itemView.findViewById<ImageView>(R.id.deleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_manage_customer,parent,false)
        return myHolder(inflate)
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        var db = data.getRoomHelper(context)
        holder.idInfo.text = "ID: ${myData[position].idAcc}"
        holder.customerInfo.text = "Username: ${myData[position].username}"
        holder.lastSeenInfo.text = "Terakhir login: ${calculateTimeSinceLastLogin(myData[position].terakhirLogin)}"

        holder.deleteUser.setOnClickListener {
            var dialog = AlertDialog.Builder(context)
                .setTitle("Pemberitahuan")
                .setMessage(
                    "Akun customer dengan username ${myData[position].username} akan dihapus. \n" +
                            "Apakah Anda yakin?"
                )
                .setPositiveButton("Ya") { _, _ ->
                    db.daoAccount().deleteById(myData[position].idAcc)
                    Toast.makeText(
                        context,
                        "Akun customer dengan username ${myData[position].username} telah dihapus.",
                        Toast.LENGTH_SHORT
                    ).show()
                    myData.remove(myData[position])
                    update()
                }
                .setNegativeButton("Tidak") { _, _ -> } //Tutup dialog

            dialog.show()
        }
    }

    override fun getItemCount(): Int = myData.size

    fun calculateTimeSinceLastLogin(lastLogin: String) : String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val lastLoginDateTime = LocalDateTime.parse(lastLogin, formatter)
        val currentDateTime = LocalDateTime.now()

        if (lastLoginDateTime == LocalDateTime.of(1971, 1, 1, 0, 0, 0)){
            return "Belum pernah login"
        }

        val duration = Duration.between(lastLoginDateTime, currentDateTime)
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60

        return when {
            days > 0 -> "$days hari yang lalu"
            hours > 0 -> "$hours jam yang lalu"
            else -> "$minutes menit yang lalu"
        }
    }

}

