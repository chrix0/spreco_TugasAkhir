package com.skripsi.spreco.activityUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textview.MaterialTextView
import com.skripsi.spreco.MainActivityUser
import com.skripsi.spreco.R
import com.skripsi.spreco.RETURN_LAST_TAB
import com.skripsi.spreco.classes.Account
import com.skripsi.spreco.data
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_editprofile.*
import kotlinx.android.synthetic.main.activity_user_editprofile.fullname
import kotlinx.android.synthetic.main.activity_user_editprofile.username
import java.time.temporal.ValueRange

class user_editprofile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_editprofile)

        val actionbar = supportActionBar
        actionbar!!.title = "Edit Data Akun"
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Blokir whitespace pada field username
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

        if(data.currentAccId < 0){ //Jika admin, sembunyikan tombol ubah password
            ubah_pass.visibility = View.INVISIBLE
            //Tambahkan lagi kode untuk si admin.. tolong jangan lupa
            //..
        }
        else{ //Jika customer
            var db = data.getRoomHelper(this)
            var getData = db.daoAccount().getAccById(data.currentAccId)[0]

            idCustomer.text = "ID Customer: ${data.currentAccId}"
            fullname.setText(getData.namaAcc)
            username.setText(getData.username)

            fun checkdupe(string : String) : Boolean{
                var getAccount = db.daoAccount().getAccUserCheck(string)
                if (getAccount.isNotEmpty()) {
                    return true
                }
                return false
            }

            update.setOnClickListener{
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
                        intent.putExtra(RETURN_LAST_TAB, "PRO") //Munculkan fragment Tentang ketika MainActivity dibuka
                        startActivity(intent)
                    }
                }
            }

            ubah_pass.setOnClickListener {
                var layout = layoutInflater.inflate(R.layout.dialog_ubahpass,null)
                var dialog = AlertDialog.Builder(this).apply{
                    setView(layout)
                }
                var creator = dialog.create()

                creator.show()

                var tbPassOld = layout.findViewById<EditText>(R.id.tbPassOld)
                var tbPassNew = layout.findViewById<EditText>(R.id.tbPassNew)
                var tbPassNewConfirm = layout.findViewById<EditText>(R.id.tbPassNewConfirm)
                var batal = layout.findViewById<TextView>(R.id.batal)
                var ganti_pass = layout.findViewById<Button>(R.id.ganti_pass)

                val disableSpace =
                    InputFilter { source, start, end, _, _, _ ->
                        for (i in start until end) {
                            if (Character.isWhitespace(source[i])) {
                                return@InputFilter ""
                            }
                        }
                        null
                    }
                tbPassOld.filters = arrayOf(disableSpace)
                tbPassNew.filters = arrayOf(disableSpace)
                tbPassNewConfirm.filters = arrayOf(disableSpace)

                ganti_pass.setOnClickListener {
                    if (tbPassOld.text.toString() == "" || tbPassNew.text.toString() == "" ||  tbPassNewConfirm.text.toString() == ""){
                        Toast.makeText(this, "Semua data harus terisi untuk mengubah password.", Toast.LENGTH_SHORT).show()
                    }
                    else if(tbPassOld.text.toString() != getData.password){
                        Toast.makeText(this, "Password lama tidak sesuai. Silahkan coba kembali.", Toast.LENGTH_SHORT).show()
                    }
                    else if(tbPassNew.text.toString().length < 8){
                        Toast.makeText(this, "Password baru harus memiliki 8 karakter atau lebih.", Toast.LENGTH_SHORT).show()
                    }
                    else if(tbPassNew.text.toString() != tbPassNewConfirm.text.toString()){
                        Toast.makeText(this, "Konfirmasi password baru tidak cocok dengan password baru yang dimasukkan sebelumnya.\n Silahkan diperiksa kembali.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        try{
                            var updated = getData
                            updated.password = tbPassNew.text.toString()
                            db.daoAccount().updateAcc(updated)
                            Toast.makeText(this, "Password sukses diganti.", Toast.LENGTH_SHORT).show()
                            creator.cancel()
                        }
                        catch(e : Exception){
                            Toast.makeText(this, "Terdapat kesalahan pada sistem.\nSilahkan coba kembali.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                batal.setOnClickListener {
                    creator.cancel()
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