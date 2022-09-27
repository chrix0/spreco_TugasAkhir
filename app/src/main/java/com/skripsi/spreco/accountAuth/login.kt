package com.skripsi.spreco.accountAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skripsi.spreco.MainActivityUser
import com.skripsi.spreco.R
import com.skripsi.spreco.data
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            //Temporary
            if(usernameLogin.text.toString() == "user"){
                data.curRole = "user"
            }
            else{
                data.curRole = "admin"
            }
            var intent = Intent(this, MainActivityUser::class.java)
            startActivity(intent)
        }

        toSignUp.setOnClickListener {
            var intent = Intent(this, register::class.java)
            startActivity(intent)
        }
    }
}