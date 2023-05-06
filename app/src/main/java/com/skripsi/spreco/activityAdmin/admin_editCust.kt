package com.skripsi.spreco.activityAdmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.skripsi.spreco.*
import com.skripsi.spreco.classes.Account
import com.skripsi.spreco.classes.Smartphone
import kotlinx.android.synthetic.main.activity_admin_edit_cust.*
import kotlinx.android.synthetic.main.activity_admin_edit_cust.fullname
import kotlinx.android.synthetic.main.activity_admin_edit_cust.idCustomer
import kotlinx.android.synthetic.main.activity_admin_edit_cust.update
import kotlinx.android.synthetic.main.activity_admin_edit_cust.username
import kotlinx.android.synthetic.main.activity_user_editprofile.*

@Suppress("DEPRECATION")
class admin_editCust : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_cust)

        val actionbar = supportActionBar
        actionbar!!.title = "Edit data Customer"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val getData = intent.getParcelableExtra<Parcelable>(SHOW_PROFILE_TO_EDIT) as Account

        idCustomer.text = "ID Customer: ${getData.idAcc}"
        fullname.setText(getData.namaAcc)
        username.setText(getData.username)

        fun checkdupe(string : String) : Boolean{
            var db = data.getRoomHelper(this)
            var getAccount = db.daoAccount().getAccUserCheck(string)
            if (getAccount.isNotEmpty()) {
                return true
            }
            return false
        }

        update.setOnClickListener {
            var db = data.getRoomHelper(this)
            if (fullname.text.toString() == "" || username.text.toString() == ""){
                Toast.makeText(this, "Nama lengkap atau Username belum terisi.", Toast.LENGTH_SHORT).show()
            }
            else{
                var duplicate = checkdupe(username.text.toString())

                //Jika username lama sama dengan yang sekarang..
                if (username.text.toString() == getData.username){
                    var updated = getData
                    updated.namaAcc = fullname.text.toString()
                    updated.username = username.text.toString()
                    db.daoAccount().updateAcc(updated)
                    Toast.makeText(this, "Profil berhasil diubah.", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, MainActivityUser::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra(RETURN_LAST_TAB, "PRO") //Munculkan fragment Tentang ketika MainActivity dibuka
                    startActivity(intent)
                }
                else if (duplicate){
                    Toast.makeText(this, "Username sudah digunakan akun lain.\nSilahkan gunakan username lain.", Toast.LENGTH_SHORT).show()
                }
                else{
                    var updated = getData
                    updated.namaAcc = fullname.text.toString()
                    updated.username = username.text.toString()
                    db.daoAccount().updateAcc(updated)
                    Toast.makeText(this, "Profil berhasil diubah.", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, MainActivityUser::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra(RETURN_LAST_TAB, "CUS") //Munculkan fragment Tentang ketika MainActivity dibuka
                    startActivity(intent)
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}