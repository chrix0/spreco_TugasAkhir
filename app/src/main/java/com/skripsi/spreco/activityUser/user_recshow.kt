@file:Suppress("DEPRECATION")

package com.skripsi.spreco.activityUser

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skripsi.spreco.*
import com.skripsi.spreco.classes.SP_rank
import com.skripsi.spreco.classes.SP_rec
import com.skripsi.spreco.classes.Smartphone
import com.skripsi.spreco.classes.recHistory
import com.skripsi.spreco.fahp_waspas.FAHP
import com.skripsi.spreco.fahp_waspas.WASPAS
import com.skripsi.spreco.fahp_waspas.makeRanking
import com.skripsi.spreco.recyclerAdapters.recycler_recshow_adapter
import com.skripsi.spreco.util.spList
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_user_recshow.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


var convert : MutableList<SP_rec> = mutableListOf() //PCM yang telah diubah ke TFN
var hitung : MutableList<Double> = mutableListOf() //Berisi hasil perhitungan FAHP-WASPAS (Skor setiap data smartphone)
var hasil : MutableList<SP_rank> = mutableListOf() //Berisi daftar ranking smartphone
var pcm : MutableList<MutableList<Double>> = mutableListOf() //Berisi PCM
lateinit var obj_history : recHistory
lateinit var adapter : recycler_recshow_adapter

class user_recshow : AppCompatActivity() {
    var temp : MutableList<SP_rank> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_recshow)

        val actionbar = supportActionBar
        actionbar!!.title = "Rekomendasi"
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Kalau tampilan ini dibuka melalui halaman riwayat rekomendasi..
        if (intent.hasExtra(SHOW_RECOMMENDED)){
            obj_history = intent.getParcelableExtra<Parcelable>(SHOW_RECOMMENDED) as recHistory
            val typeTokenHistory = object : TypeToken<MutableList<SP_rank>>() {}.type
            hasil = Gson().fromJson(obj_history.result, typeTokenHistory)
            temp.addAll(hasil)
            if(hasil.isEmpty()){
                nothing_text.text = "Tidak ada produk yang dapat direkomendasikan."
                nothing_text.visibility = View.VISIBLE
            }
            shownCount.text = "Jumlah data: ${hasil.size}"
            adapter = recycler_recshow_adapter(hasil){
                val info = Intent(this, user_spdetail::class.java)
                info.putExtra(SHOW_PRODUCT_INFO, it.obj_sp as Parcelable) //Tampilkan detail untuk smartphone yang dipilih
                startActivity(info)
            }
            rankingList.layoutManager = LinearLayoutManager(this@user_recshow)
            rankingList.adapter = adapter
        }
        else{ //Jika dari menekan tombol Tampilkan Rekomendasi dari Menu Rekomendasi
            Async().execute()
        }

        last_settings.setOnClickListener {
            val info = Intent(this, user_recsettings_history::class.java)
            info.putExtra(SHOW_PAST_REC_SETTINGS, obj_history as Parcelable)
            startActivity(info)
        }

        search.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE){
                temp.clear()
                temp.addAll(filter(search.text.toString(), hasil))
                if(hasil.isEmpty()){
                    nothing_text.text = "Data smartphone tidak ditemukan."
                    nothing_text.visibility = View.VISIBLE
                }
                adapter = recycler_recshow_adapter(temp){
                    val info = Intent(this, user_spdetail::class.java)
                    info.putExtra(SHOW_PRODUCT_INFO, it.obj_sp as Parcelable) //Tampilkan detail untuk smartphone yang dipilih
                    startActivity(info)
                }
                rankingList.adapter = adapter
            }
            return@setOnEditorActionListener true
        }

        // Tombol sort
        var popup = PopupMenu(this, sortButton)
        popup.menuInflater.inflate(R.menu.menu_sort_rec, popup.menu)
        sortButton.setOnClickListener{
            popup.show()
        }

        fun showModifiedRec(temp : List<SP_rank>){
            adapter = recycler_recshow_adapter(temp){
                val info = Intent(this, user_spdetail::class.java)
                info.putExtra(SHOW_PRODUCT_INFO, it.obj_sp as Parcelable) //Tampilkan detail untuk smartphone yang dipilih
                startActivity(info)
            }
            rankingList.adapter = adapter

            if(temp.isEmpty()){
                nothing_text.visibility = View.VISIBLE
            }
        }

        popup.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when(it.itemId){
                R.id.harga_des->{
                    temp.sortBy { it.obj_sp.harga }
                    temp.reverse()
                    showModifiedRec(temp)
                    true
                }
                R.id.harga_asc ->{
                    temp.sortBy { it.obj_sp.harga }
                    showModifiedRec(temp)
                    true
                }
                R.id.nama_des->{
                    temp.sortBy { it.obj_sp.namaSP }
                    temp.reverse()
                    showModifiedRec(temp)
                    true
                }
                R.id.nama_asc ->{
                    temp.sortBy { it.obj_sp.namaSP }
                    showModifiedRec(temp)
                    true
                }
                R.id.rank_des -> {
                    temp.sortBy { it.rank }
                    temp.reverse()
                    showModifiedRec(temp)
                    true
                }
                R.id.rank_asc -> {
                    temp.sortBy { it.rank }
                    showModifiedRec(temp)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    fun getCurrentDateTime():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return sdf.format(Date())
    }

    fun filter(searchText: String, list: MutableList<SP_rank>): MutableList<SP_rank> {
        var res = list.filter{it.obj_sp.namaSP.contains(searchText, ignoreCase = true)}.toMutableList()
        return res
    }

    inner class Async : AsyncTask<Void, Void, Unit>(){
        //Beri tahu user bahwa rekomendasi sedang diproses..
        private var startTime: Long = 0
        var dialog = ProgressDialog(this@user_recshow).apply {
            setMessage("Rekomendasi Anda sedang diproses.\nMohon tunggu.")
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setCancelable(false)
        }
        override fun doInBackground(vararg p0: Void?): Unit? {
            var db = data.getRoomHelper(applicationContext)
            var listDiproses = mutableListOf<Smartphone>()
            var listSp = db.daoSP().getAllSP()
            listDiproses.addAll(listSp)
            //Kalau perlu filter harga..
            if(data.filterHargaChecked){
                listDiproses = listDiproses.filter {
                    it.harga in data.hargaRangeBawah..data.hargaRangeAtas
                }.toMutableList()
            }

            convert = fahp_waspas.convert_ke_nilai_kriteria(listDiproses)
            hitung = WASPAS(convert, FAHP(data.pcm)) // Perhitungan FAHP-WASPAS di sini

            hasil = if(listDiproses.size >= data.maxDataRec){
                makeRanking(listDiproses, hitung).take(data.maxDataRec).toMutableList()
            } else{
                makeRanking(listDiproses, hitung).toMutableList()
            }

//            var sorted_wsm_wpm_waspas = data.wsm_wpm_waspas.sortedByDescending { it[2] }
//            var take = sorted_wsm_wpm_waspas.take(data.maxDataRec).toMutableList()
//            Log.i("waspas", take.toString())

            return null
        }
        override fun onPreExecute() {
            super.onPreExecute()
            startTime = System.currentTimeMillis()
            pcm = data.pcm
            dialog.show()
        }
        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            val executionTime = (System.currentTimeMillis() - startTime) / 1000.0
            val decimalFormat = DecimalFormat("#.##")
            val roundedExecutionTime = decimalFormat.format(executionTime)
            if (dialog.isShowing) {
                dialog.dismiss()
            }

            temp.addAll(hasil) //Akan dipakai dalam pencarian nantinya.

            if(hasil.isEmpty()){
                nothing_text.text = "Tidak ada produk yang dapat direkomendasikan."
                nothing_text.visibility = View.VISIBLE
            }

            var db = data.getRoomHelper(applicationContext)
            adapter = recycler_recshow_adapter(hasil){
                val info = Intent(this@user_recshow, user_spdetail::class.java)
                info.putExtra(SHOW_PRODUCT_INFO, it.obj_sp as Parcelable) //Tampilkan detail untuk smartphone yang dipilih
                startActivity(info)
            }

            shownCount.text = "Jumlah data: ${hasil.size} || Waktu eksekusi: $roundedExecutionTime detik"

            rankingList.layoutManager = LinearLayoutManager(this@user_recshow)
            rankingList.adapter = adapter

            var hasilToJSON= Gson().toJson(hasil)
            var bobotKriteriaToJSON = Gson().toJson(data.bobotKriteria)
            var pcmJSON = Gson().toJson(data.pcm)
            obj_history = recHistory(0, data.currentAccId, hasilToJSON, getCurrentDateTime(), data.hargaRangeBawah, data.hargaRangeAtas, data.maxDataRec, bobotKriteriaToJSON, pcmJSON)
            db.daoRecHistory().addHistory(obj_history) //Simpan ke RecHistory

            //Reset..
            data.enabledCriteria = mutableListOf<String>()
            data.enabledCriteriaType = mutableListOf<Char>()
            data.filterHargaChecked = false //Temporary. Hanya pengaturan toggle kriteria yang disimpan
            data.hargaRangeAtas = -1
            data.hargaRangeBawah = -1
            data.maxDataRec = 0
            data.bobotKriteria = mutableMapOf<String, Double>()
            data.settingDone = false
        }
    }
}

