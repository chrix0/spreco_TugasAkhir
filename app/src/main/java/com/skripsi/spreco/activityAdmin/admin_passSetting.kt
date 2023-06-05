package com.skripsi.spreco.activityAdmin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.util.newStringBuilder
import com.skripsi.spreco.R
import com.skripsi.spreco.accountAuth.login
import com.skripsi.spreco.data
import com.skripsi.spreco.sharedPref.adminPassSharedPref
import com.skripsi.spreco.util.spList
import com.skripsi.spreco.util.spSourceList
import kotlinx.android.synthetic.main.activity_admin_pass_setting.*
import kotlinx.android.synthetic.main.activity_login.*

class admin_passSetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pass_setting)


        //Masukkan data ke dalam database
        var db = data.getRoomHelper(applicationContext)
        // Pilih dataset Smartphone yang digunakan (original / test)
        // spList.spListType dapat diganti pada file dengan path "util/spList.kt"
//        if(spList.spListType == "original"){
////            db.daoSPSource().deleteAllSPSource() //Untuk testing saja jangan lupa hapus
////            db.daoSP().deleteAllSP() //Untuk testing saja jangan lupa hapus
//            db.daoSP().addAllSP(spList.list_sp)
//            db.daoSPSource().addAllSource(spSourceList.list_source)
//        }
//        else{
//            db.daoSPSource().deleteAllSPSource() //Hapus semua link untuk menghindari foreign key error
//            db.daoSP().deleteAllSP() //Reset isi tabel Smartphone
//            db.daoSP().addAllSP(spList.list_sp_test) //Ganti dengan list untuk testing
//        }

        var sharedpref = adminPassSharedPref(this)

        val disableSpace =
            InputFilter { source, start, end, _, _, _ ->
                for (i in start until end) {
                    if (Character.isWhitespace(source[i])) {
                        return@InputFilter ""
                    }
                }
                null
            }
        newPass.filters = arrayOf(disableSpace)
        newPassConfirm.filters = arrayOf(disableSpace)

        setAdminPassButton.setOnClickListener {

            if(newPass.length() < 8){
                Toast.makeText(this, "Password harus memiliki 8 karakter atau lebih.", Toast.LENGTH_SHORT).show()
            }
            else if (newPass.text.toString() != newPassConfirm.text.toString()){
                Toast.makeText(this, "Password konfirmasi dengan password yang diisi tidak sama. \nSilahkan periksa kembali.", Toast.LENGTH_SHORT).show()
            }
            else{
                var dialog = AlertDialog.Builder(this)
                    .setTitle("Pemberitahuan")
                    .setMessage("Password admin tidak dapat diganti setelah diatur. \n" +
                            "Mohon ingat password admin Anda untuk dapat mengelola daftar smartphone dan daftar akun customer Anda. \n\n" +
                            "Berikut password admin yang akan Anda gunakan:\n ${newPass.text.toString()}\n" +
                            "Apakah password tersebut sudah sesuai?")
                    .setPositiveButton("Ya") { _, _ ->
                        sharedpref.adminPass = newPass.text.toString()
                        var intent = Intent(this, login::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        Toast.makeText(this, "Password admin berhasil diatur.", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(intent)
                    }
                    .setNegativeButton("Tidak"){ _, _ -> } //Tutup dialog

                dialog.show()
            }

        }


    }
}