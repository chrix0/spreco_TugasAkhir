package com.skripsi.spreco.activityUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.skripsi.spreco.R
import com.skripsi.spreco.classes.ChosenCriteria
import com.skripsi.spreco.data
import kotlinx.android.synthetic.main.activity_user_recsettings_history.*
import kotlinx.android.synthetic.main.activity_user_toggle_kriteria.*
import kotlinx.android.synthetic.main.activity_user_toggle_kriteria.filterEnd
import kotlinx.android.synthetic.main.activity_user_toggle_kriteria.filterStart
import org.jetbrains.anko.doBeforeSdk

class user_toggleKriteria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_toggle_kriteria)

        val actionbar = supportActionBar
        actionbar!!.title = "Pengaturan rekomendasi"
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Urutan dalam list kriteria perlu diperhatikan, untuk memudahkan visualisasi kita pakai Map
        var criteriaStatus = mutableMapOf(
            "RAM" to false,
            "ROM" to false,
            "Kapasitas baterai" to false,
            "Kamera belakang" to false,
            "Kamera depan" to false,
            "Ukuran layar" to false,
            "Harga" to false
        )

        //B -> Benefit, C -> Cost
        var criteriaType = mutableMapOf(
            "RAM" to 'B',
            "ROM" to 'B',
            "Kapasitas baterai" to 'B',
            "Kamera belakang" to 'B',
            "Kamera depan" to 'B',
            "Ukuran layar" to 'B',
            "Harga" to 'C'
        )

        // Hidupkan switch sesuai dengan data pada tabel yang tersedia pada tabel ChosenCriteria untuk id Account tertentu
        var db = data.getRoomHelper(this)
        criteriaStatus.forEach { (key, value) ->
            if (db.daoToggle().criteriaExists(data.currentAccId, key).isNotEmpty()){
                criteriaStatus[key] = true
                when(key){
                    "RAM" -> switch_ram.isChecked = true
                    "ROM" -> switch_rom.isChecked = true
                    "Kapasitas baterai" -> switch_baterai.isChecked = true
                    "Kamera belakang" -> switch_backcam.isChecked = true
                    "Kamera depan" -> switch_frontcam.isChecked = true
                    "Ukuran layar" -> switch_ukuranLayar.isChecked = true
                    "Harga" -> switch_harga.isChecked = true
                }
            }
        }

        // Switch Event
        switch_ram.setOnCheckedChangeListener { _, tambah ->
            criteriaStatus["RAM"] = tambah
        }
        switch_rom.setOnCheckedChangeListener { _, tambah ->
            criteriaStatus["ROM"] = tambah
        }
        switch_baterai.setOnCheckedChangeListener { _, tambah ->
            criteriaStatus["Kapasitas baterai"] = tambah
        }
        switch_backcam.setOnCheckedChangeListener { _, tambah ->
            criteriaStatus["Kamera belakang"] = tambah
        }
        switch_frontcam.setOnCheckedChangeListener { _, tambah ->
            criteriaStatus["Kamera depan"] = tambah
        }
        switch_ukuranLayar.setOnCheckedChangeListener { _, tambah ->
            criteriaStatus["Ukuran layar"] = tambah
        }
        switch_harga.setOnCheckedChangeListener { _, tambah ->
            criteriaStatus["Harga"] = tambah
        }

        filterHarga.setOnCheckedChangeListener { _, kondisi ->
            filterStart.isEnabled = kondisi
            filterEnd.isEnabled = kondisi
        }

        var size = db.daoSP().getAllSP().size
        showMax.text = "Minimal 1, Maksimal $size"

        saveToggle.setOnClickListener {
            //Cek apakah jumlah kriteria yang hidup lebih dari 1
            var count = 0
            criteriaStatus.forEach { (key, value) ->
                if (value)
                    count += 1
            }

            if (filterHarga.isChecked and
                filterStart.text.toString().isEmpty() and
                filterEnd.text.toString().isEmpty()){
                Toast.makeText(this, "Mohon hilangkan centang Filter Harga terlebih dahulu jika tidak digunakan.", Toast.LENGTH_SHORT).show()
            }
            else if (count < 2){
                Toast.makeText(this, "Jumlah kriteria rekomendasi yang digunakan harus lebih dari satu.", Toast.LENGTH_SHORT).show()
            }
            else if (dataMax.text.toString().isEmpty()){
                Toast.makeText(this, "Jumlah data maksimum yang ditampilkan harus diisi.", Toast.LENGTH_SHORT).show()
            }
            else if ((dataMax.text.toString().toInt() < 1) or (dataMax.text.toString().toInt() > size)){
                Toast.makeText(this, "Jumlah data maksimal harus sesuai dengan ketentuan nilai minimal dan maksimal.", Toast.LENGTH_SHORT).show()
            }
            else{
                criteriaStatus.forEach { (key, value) ->
                    print("$key : $value")
                    // Jika terdapat data untuk id Account tersebut dan customer ingin menghapusnya
                    // db.daoToggle().criteriaExists(data.currentAccId, key).isNotEmpty() and value == false
                    if (!(db.daoToggle().criteriaExists(data.currentAccId, key).isNotEmpty() and value)){
                        var obj = ChosenCriteria(data.currentAccId, key, criteriaType[key]!!)
                        db.daoToggle().offToggle(obj)
                    }
                    // Jika tidak ada data dan customer ingin menambahnya
                    if (db.daoToggle().criteriaExists(data.currentAccId, key).isEmpty() and value){
                        var obj = ChosenCriteria(data.currentAccId, key, criteriaType[key]!!)
                        db.daoToggle().onToggle(obj)
                    }
                    // Selain kedua syarat tersebut, tidak ada proses yang dilakukan
                }

                if(filterHarga.isChecked){
                    data.filterHargaChecked = true
                    data.hargaRangeAtas = filterEnd.text.toString().toInt()
                    data.hargaRangeBawah = filterStart.text.toString().toInt()
                }
                else{
                    data.filterHargaChecked = false
                    data.hargaRangeAtas = -1
                    data.hargaRangeBawah = -1
                }

                data.criteriaList = criteriaStatus
                data.maxDataRec = dataMax.text.toString().toInt()
                Toast.makeText(this, "Pengaturan Anda telah berhasil disimpan.", Toast.LENGTH_SHORT).show()

                //Ambil semua key yang bernilai true di data.criteriaList. Masukkan ke dalam data.enabledCriteria
                data.enabledCriteria = mutableListOf()
                data.criteriaList.forEach { (key, value) ->
                    if (value){ //jika true
                        data.enabledCriteria.add(key)
                    }
                } //Sampai di sini, enabledCriteria sudah memiliki kriteria yang berurutan

                var intent = Intent(this, user_recsettings::class.java)
                startActivity(intent)
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}