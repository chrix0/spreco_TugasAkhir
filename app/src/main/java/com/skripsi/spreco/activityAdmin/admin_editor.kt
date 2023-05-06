package com.skripsi.spreco.activityAdmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skripsi.spreco.R

class admin_editor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_editor)

        val actionbar = supportActionBar
        actionbar!!.title = "Edit data smartphone"
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}