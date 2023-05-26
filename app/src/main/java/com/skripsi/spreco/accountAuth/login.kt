package com.skripsi.spreco.accountAuth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.skripsi.spreco.MainActivityUser
import com.skripsi.spreco.R
import com.skripsi.spreco.activityAdmin.admin_passSetting
import com.skripsi.spreco.data
import com.skripsi.spreco.sharedPref.adminPassSharedPref
import com.skripsi.spreco.util.spList
import com.skripsi.spreco.util.spSourceList
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*


class login : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Jika password admin belum diset, halaman admin_passSetting akan selalu muncul
        var savedAdminPass = adminPassSharedPref(this)
        if(savedAdminPass.adminPass == ""){
            var intent = Intent(this, admin_passSetting::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        //Jika terisi, kode setelah baris ini akan dijalankan.
        //Untuk menghindari adanya whitespace pada teks EditText
        val disableSpace =
            InputFilter { source, start, end, _, _, _ ->
                for (i in start until end) {
                    if (Character.isWhitespace(source[i])) {
                        return@InputFilter ""
                    }
                }
                null
            }
        usernameLogin.filters = arrayOf(disableSpace)
        passwordLogin.filters = arrayOf(disableSpace)


        var db = data.getRoomHelper(applicationContext)


        // Kode ini diambil dari passSetting. Hanya digunakan untuk pengujian saja.
        // Untuk sidang nanti, commentkan kode bagian ini.

//        // Pilih dataset Smartphone yang digunakan (original / test)
//        // spList.spListType dapat diganti pada file dengan path "util/spList.kt"
//        if(spList.spListType == "original"){
//            db.daoSPSource().deleteAllSPSource() //Untuk testing saja jangan lupa hapus
//            db.daoSP().deleteAllSP() //Untuk testing saja jangan lupa hapus
//            db.daoSP().addAllSP(spList.list_sp)
//            db.daoSPSource().addAllSource(spSourceList.list_source)
//        }
//        else{
//            db.daoSPSource().deleteAllSPSource() //Hapus semua link untuk menghindari foreign key error
//            db.daoSP().deleteAllSP() //Reset isi tabel Smartphone
//            db.daoSP().addAllSP(spList.list_sp_test) //Ganti dengan list untuk testing
//        }

        loginButton.setOnClickListener {
            var userName = usernameLogin.text.toString()
            var password = passwordLogin.text.toString()

            if (userName.isEmpty()||password.isEmpty()) {
                Toast.makeText(this, "Username dan password harus diisi.", Toast.LENGTH_SHORT).show()
            }
            else {
                var found = false
                var getAccount = db.daoAccount().getAcc(userName, password)
                if (getAccount.isNotEmpty()) {
                    found = true
                    data.currentAccId = getAccount[0].idAcc
                    // Ambil waktu pada saat login dengan format "dd-MM-yyyy HH:mm:ss"
                    getAccount[0].terakhirLogin = getCurrentDateTime()
                    db.daoAccount().updateAcc(getAccount[0]) // Update account
                }
                if (found) {
                    Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    data.curRole = 'C'
                    var intent = Intent(this, MainActivityUser::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Akun tidak ditemukan atau Password salah. \nSilahkan register terlebih dahulu.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        regCustButton.setOnClickListener {
            var intent = Intent(this, register::class.java)
            startActivity(intent)
        }

        loginAdmin.setOnClickListener{
            var layout = layoutInflater.inflate(R.layout.dialog_login_admin,null)
            var dialog = AlertDialog.Builder(this).apply{
                setView(layout)
            }
            var creator = dialog.create()

            var pass = layout.findViewById<EditText>(R.id.PassAdmin)
            var login = layout.findViewById<Button>(R.id.login_admin)
            var batal = layout.findViewById<TextView>(R.id.batal)

            val disableSpace =
                InputFilter { source, start, end, _, _, _ ->
                    for (i in start until end) {
                        if (Character.isWhitespace(source[i])) {
                            return@InputFilter ""
                        }
                    }
                    null
                }
            pass.filters = arrayOf(disableSpace)

            var savedAdminPass = adminPassSharedPref(this)

            login.setOnClickListener {
                if (pass.text.toString() == savedAdminPass.adminPass){
                    Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    data.curRole = 'A' //Admin
                    data.currentAccId = -1
                    var intent = Intent(this, MainActivityUser::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Password tidak sesuai. Coba lagi.", Toast.LENGTH_SHORT).show()
                }
            }

            batal.setOnClickListener{
                creator.cancel()
            }

            creator.show()
        }
    }
    fun getCurrentDateTime():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return sdf.format(Date())
    }
}