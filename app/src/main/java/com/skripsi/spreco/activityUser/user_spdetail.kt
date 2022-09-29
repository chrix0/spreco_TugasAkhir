package com.skripsi.spreco.activityUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PRODUCT_INFO
import com.skripsi.spreco.classes.SP
import com.skripsi.spreco.data
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_spdetail.*

class user_spdetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_spdetail)

        val actionbar = supportActionBar
        actionbar!!.title = "Info Produk"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val produk : SP = intent.getParcelableExtra<Parcelable>(SHOW_PRODUCT_INFO) as SP

        Picasso.get().load(produk.picURL).into(productImg)
        name.text = produk.nama
        price.text = "Rp." + data.formatHarga(produk.harga)
        specText.text = produk.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}