package com.skripsi.spreco.activityUser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PRODUCT_INFO
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.classes.Wishlist
import com.skripsi.spreco.data
import com.skripsi.spreco.data.currentAccId
import com.skripsi.spreco.recyclerAdapters.recycler_link_pembelian
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_spdetail.*
import kotlinx.android.synthetic.main.dialog_detail_links.*

@Suppress("DEPRECATION")
class user_spdetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_spdetail)

        val actionbar = supportActionBar
        actionbar!!.title = "Info Produk"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val produk : Smartphone = intent.getParcelableExtra<Parcelable>(SHOW_PRODUCT_INFO) as Smartphone

        Picasso.get().load(produk.picURL).into(productImg)
        name.text = produk.namaSP
        price.text = "Rp." + data.formatHarga(produk.harga)
        var specs =
            "Nama: ${produk.namaSP}\n" +
            "Merek: ${produk.merek}\n" +
            "Network: ${produk.network}\n" +
            "SIM: ${produk.sim}\n" +
            "Tipe display: ${produk.tDisplay}\n" +
            "Ukuran display: ${produk.uDisplay} inci\n" +
            "OS: ${produk.OS}\n" +
            "Chipset: ${produk.chipset}\n" +
            "CPU: ${produk.cpu}\n" +
            "GPU: ${produk.gpu}\n" +
            "RAM: ${produk.ram} GB\n" +
            "ROM: ${produk.rom} GB\n" +
            "Kamera Belakang: ${produk.mainCam} MP\n" +
            "Kamera Depan: ${produk.selfieCam} MP\n" +
            "Baterai: ${produk.battery} mAh\n" +
            "Warna yang tersedia: ${produk.warna}\n"
        specText.text = specs

        var db = data.getRoomHelper(applicationContext)
        var wish = Wishlist(currentAccId, produk.idSP)

        var exist = db.daoWishlist().wishExist(currentAccId, produk.idSP)
        if (exist.isNotEmpty()){
            addToWish.isChecked = true
        }

        addToWish.setOnCheckedChangeListener { _, checked ->
            if(checked){
                db.daoWishlist().addWishlist(wish)
            }
            else{
                db.daoWishlist().deleteByObj(wish)
            }
        }

        showSource.setOnClickListener {
            var layout = layoutInflater.inflate(R.layout.dialog_detail_links,null)
            var dialog = AlertDialog.Builder(this).apply{
                setView(layout)
            }
            var creator = dialog.create()

            var links = layout.findViewById<RecyclerView>(R.id.links)
            var tutup = layout.findViewById<TextView>(R.id.tutup)
            var nothing_text = layout.findViewById<TextView>(R.id.nothing_text)

            var db = data.getRoomHelper(this)
            var sources = db.daoSPSource().getAllSource(produk.idSP)

            // Menambah data ke dalam recycler view
            var adapter = recycler_link_pembelian(sources){
                // https://stackoverflow.com/questions/3004515/sending-an-intent-to-browser-to-open-specific-url
                var url = it.sourceLink
                //Kalau diawali dengan www., URL harus diawali dengan http://
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://$url"
                }
                val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(openUrlIntent)
            }

            links.layoutManager = LinearLayoutManager(this)
            links.adapter = adapter

            if(sources.isEmpty()){
                nothing_text.visibility = View.VISIBLE
            }

            tutup.setOnClickListener {
                creator.cancel()
            }

            creator.show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
//        onBackPressedDispatcher.onBackPressed()
        finish()
        return true
    }
}