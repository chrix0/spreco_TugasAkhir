package com.skripsi.spreco.activityUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skripsi.spreco.R

class user_spdetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_spdetail)

        val actionbar = supportActionBar
        actionbar!!.title = "Detail produk"
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}