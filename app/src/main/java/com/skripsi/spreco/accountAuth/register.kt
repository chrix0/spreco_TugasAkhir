package com.skripsi.spreco.accountAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skripsi.spreco.MainActivityUser
import com.skripsi.spreco.R
import kotlinx.android.synthetic.main.activity_register.*

class register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        toLogin.setOnClickListener {
            var intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }
}