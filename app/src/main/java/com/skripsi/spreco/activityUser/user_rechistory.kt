package com.skripsi.spreco.activityUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.skripsi.spreco.*
import com.skripsi.spreco.recyclerAdapters.recycler_recHistory_adapter
import com.skripsi.spreco.recyclerAdapters.recycler_sp_adapter
import kotlinx.android.synthetic.main.activity_user_rechistory.*

class user_rechistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_rechistory)

        val actionbar = supportActionBar
        actionbar!!.title = "Riwayat Rekomendasi"
        actionbar.setDisplayHomeAsUpEnabled(true)

        var db = data.getRoomHelper(this)
        // Berisi kumpulan smartphone yang tersedia di dalam sistem.
        // Diubah ke tipe MutableList agar isinya bisa diubah
        var history = db.daoRecHistory().getHistory(data.currentAccId)

        var adapter = recycler_recHistory_adapter(history){
            val info = Intent(this, user_recshow::class.java)
            info.putExtra(SHOW_RECOMMENDED, it as Parcelable)
            startActivity(info)
        }

        histories.layoutManager = LinearLayoutManager(this)
        histories.adapter = adapter

        if(history.isEmpty()){
            nothing_text.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}