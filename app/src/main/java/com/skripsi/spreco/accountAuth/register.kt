package com.skripsi.spreco.accountAuth

import android.accounts.Account
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.skripsi.spreco.MainActivityUser
import com.skripsi.spreco.R
import com.skripsi.spreco.data
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        lateinit var cuserName : String
        lateinit var cpassword : String
        lateinit var cfullName : String
        lateinit var confirmPass : String

        var db = data.getRoomHelper(applicationContext)

        //Blokir whitespace apapun pada username, password, confirm
        val disableSpace =
            InputFilter { source, start, end, _, _, _ ->
                for (i in start until end) {
                    if (Character.isWhitespace(source[i])) {
                        return@InputFilter ""
                    }
                }
                null
            }
        username.filters = arrayOf(disableSpace)
        password.filters = arrayOf(disableSpace)
        passwordConfirm.filters = arrayOf(disableSpace)

        fun checkdupe(string : String) : Boolean{
            var getAccount = db.daoAccount().getAccUserCheck(string)
            if (getAccount.isNotEmpty()) {
                return true
            }
            return false
        }

       var clsAccount : com.skripsi.spreco.classes.Account =
            com.skripsi.spreco.classes.Account(
                0,
                "","","", ""
            )
        signup.setOnClickListener{
            cuserName = username.text.toString()
            cpassword = password.text.toString()
            cfullName = fullname.text.toString()
            confirmPass = passwordConfirm.text.toString()

            if(cuserName == "" || cpassword == "" || cfullName == "" || confirmPass == ""){
                Toast.makeText(this, "Semua data wajib diisi sebelum melakukan registrasi.", Toast.LENGTH_SHORT).show()
            }
            else{
                if(checkdupe(cuserName)){
                    Toast.makeText(this, "Username tersebut sudah digunakan oleh akun lain. \nCoba gunakan username yang lain.", Toast.LENGTH_SHORT).show()
                }
                else if(cpassword.length < 8){
                    Toast.makeText(this, "Password harus memiliki 8 karakter atau lebih.", Toast.LENGTH_SHORT).show()
                }
                else if(cpassword != confirmPass){
                    Toast.makeText(this, "Password konfirmasi dengan password yang diisi tidak sama. \nSilahkan periksa kembali.", Toast.LENGTH_SHORT).show()
                }
                else{
                    clsAccount.username = cuserName
                    clsAccount.password = cpassword
                    clsAccount.namaAcc = cfullName
                    // Jika menggunakan tanggal dan waktu di bawah ini, berarti user belum pernah login dengan akun ini.
                    // Tanggal ini digunakan karena aplikasi ini belum dibuat pada tanggal tersebut..
                    // Tentunya pada tanggal tersebut, belum ada akun yang terdaftar ataupun yang sudah masuk ke dalam aplikasi ini.
                    clsAccount.terakhirLogin = "01-01-1971 00:00:00"

                    //Tambah data baru
                    db.daoAccount().newAcc(clsAccount)
                    Toast.makeText(this, "Akun baru berhasil dibuat.", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, login::class.java)
                    startActivity(intent)
                }
            }
        }

        toLogin.setOnClickListener {
            var intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }
}