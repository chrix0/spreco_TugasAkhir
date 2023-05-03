
package com.skripsi.spreco.activityUser

import android.content.Intent
import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.google.gson.Gson
import com.skripsi.spreco.*
import com.skripsi.spreco.classes.RecPref
import com.skripsi.spreco.data.criteriaList
import com.skripsi.spreco.data.currentAccId
import com.skripsi.spreco.data.enabledCriteria
import com.skripsi.spreco.data.enabledCriteriaType
import com.skripsi.spreco.data.settingDone
import kotlinx.android.synthetic.main.activity_user_recsettings.*
import kotlinx.android.synthetic.main.activity_user_toggle_kriteria.*

class user_recsettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_recsettings)

        val actionbar = supportActionBar
        actionbar!!.title = "Pengaturan rekomendasi"
        actionbar.setDisplayHomeAsUpEnabled(true)


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

        //Tentukan tipe setiap kriteria
        enabledCriteria.forEach {
            enabledCriteriaType.add(criteriaType[it]!!)
        }

        var pcm : MutableList<MutableList<Double>> = mutableListOf()

        //Berisi posisi matriks yang akan dijadikan tempat input
        var inputPos : MutableList<MutableList<Int>> = mutableListOf()

        //Buat template PCM untuk diisi nantinya dengan ukuran n x n
        for (i in 0 until enabledCriteria.size){
            var baris = mutableListOf<Double>()
            for (j in 0 until enabledCriteria.size){
                if (i == j)
//                    pcm[i][j] = 1.0
                    baris.add(1.0)
                else{
//                    pcm[i][j] = 0.0
                    baris.add(0.0)
                }
            }
            pcm.add(baris)
        }

//        for (i in 0 until enabledCriteria.size - 1){
//            for (j in 0 until enabledCriteria.size - 1){
//                if (i == j)
//                    pcm[i][j] = 1.0
//                else{
//                    pcm[i][j] = 0.0
//                }
//            }
//        }

        // Menentukan posisi input
        for (i in 0 until enabledCriteria.size){
            for (j in i until enabledCriteria.size){
                if (i != j){
                    inputPos.add(mutableListOf(i, j))
                }
            }
        }

        // Tambah Nilai Intensitas Kepentingan
        fun addNIK(pcm : MutableList<MutableList<Double>>, score : Double, i : Int, j : Int) : MutableList<MutableList<Double>>{
            // Capai indeks limit -> arahkan ke halaman selanjutnya
            if (i == j){
                pcm[i][j] = 1.0
            }
            else{
                pcm[i][j] = score
            }
            return pcm
        }

        fun next(i : Int, j : Int, total : Int, criteriaList : MutableList<String>, posisi : Int){
            askCount.text = "Pertanyaan $posisi dari $total."
            qText.text = "Menurut Anda, seberapa penting ${criteriaList[i]} smartphone daripada ${criteriaList[j]} smartphone?"
        }

        var index_target = 0
        // Munculkan pertanyaan pertama
        next(inputPos[index_target][0], inputPos[index_target][1], inputPos.size, enabledCriteria, index_target + 1)

        p1.setOnClickListener {
            pcm = addNIK(pcm, 1.0/9.0, inputPos[index_target][0], inputPos[index_target][1])
            if (index_target + 1 < inputPos.size){
                index_target += 1
                next(inputPos[index_target][0], inputPos[index_target][1], inputPos.size, enabledCriteria, index_target + 1)
            }
            else{
                var db = data.getRoomHelper(applicationContext)

                data.pcm = pcm
//                val gson = Gson()
//                var pcmJSON = gson.toJson(pcm)
//                var res = RecPref(currentAccId, pcmJSON)
//                db.daoRecPref().addPCM(res)

                data.settingDone = true
                Toast.makeText(this, "Pengaturan rekomendasi Anda telah disimpan.\nSilahkan lanjut ke step berikutnya.", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivityUser::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(RETURN_LAST_TAB, "REC") //Munculkan fragment Menu Rekomendasi ketika MainActivity dibuka
                startActivity(intent)
            }

        }
        p2.setOnClickListener {
            pcm = addNIK(pcm, 1.0/5.0, inputPos[index_target][0], inputPos[index_target][1])
            if (index_target + 1 < inputPos.size){
                index_target += 1
                next(inputPos[index_target][0], inputPos[index_target][1], inputPos.size, enabledCriteria, index_target + 1)
            }
            else{
                var db = data.getRoomHelper(applicationContext)

                data.pcm = pcm
//                val gson = Gson()
//                var pcmJSON = gson.toJson(pcm)
//                var res = RecPref(currentAccId, pcmJSON)
//                db.daoRecPref().addPCM(res)

                data.settingDone = true
                Toast.makeText(this, "Pengaturan rekomendasi Anda telah disimpan.\nSilahkan lanjut ke step berikutnya.", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivityUser::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(RETURN_LAST_TAB, "REC") //Munculkan fragment Menu Rekomendasi ketika MainActivity dibuka
                startActivity(intent)
            }
        }
        p3.setOnClickListener {
            pcm = addNIK(pcm, 1.0, inputPos[index_target][0], inputPos[index_target][1])
            if (index_target + 1 < inputPos.size){
                index_target += 1
                next(inputPos[index_target][0], inputPos[index_target][1], inputPos.size, enabledCriteria, index_target + 1)
            }
            else{
                var db = data.getRoomHelper(applicationContext)

                data.pcm = pcm
//                val gson = Gson()
//                var pcmJSON = gson.toJson(pcm)
//                var res = RecPref(currentAccId, pcmJSON)
//                db.daoRecPref().addPCM(res)

                data.settingDone = true
                Toast.makeText(this, "Pengaturan rekomendasi Anda telah disimpan.\nSilahkan lanjut ke step berikutnya.", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivityUser::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(RETURN_LAST_TAB, "REC") //Munculkan fragment Menu Rekomendasi ketika MainActivity dibuka
                startActivity(intent)
            }
        }
        p4.setOnClickListener {
            pcm = addNIK(pcm, 5.0, inputPos[index_target][0], inputPos[index_target][1])
            if (index_target + 1 < inputPos.size){
                index_target += 1
                next(inputPos[index_target][0], inputPos[index_target][1], inputPos.size, enabledCriteria, index_target + 1)
            }
            else{
                var db = data.getRoomHelper(applicationContext)

                data.pcm = pcm
//                val gson = Gson()
//                var pcmJSON = gson.toJson(pcm)
//                var res = RecPref(currentAccId, pcmJSON)
//                db.daoRecPref().addPCM(res)

                data.settingDone = true
                Toast.makeText(this, "Pengaturan rekomendasi Anda telah disimpan.\nSilahkan lanjut ke step berikutnya.", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivityUser::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(RETURN_LAST_TAB, "REC") //Munculkan fragment Menu Rekomendasi ketika MainActivity dibuka
                startActivity(intent)
            }
        }
        p5.setOnClickListener {
            pcm = addNIK(pcm, 9.0, inputPos[index_target][0], inputPos[index_target][1])
            if (index_target + 1 < inputPos.size){
                index_target += 1
                next(inputPos[index_target][0], inputPos[index_target][1], inputPos.size, enabledCriteria, index_target + 1)
            }
            else{
                var db = data.getRoomHelper(applicationContext)

                data.pcm = pcm
//                val gson = Gson()
//                var pcmJSON = gson.toJson(pcm)
//                var res = RecPref(currentAccId, pcmJSON)
//                db.daoRecPref().addPCM(res)

                data.settingDone = true
                Toast.makeText(this, "Pengaturan rekomendasi Anda telah disimpan.\nSilahkan lanjut ke step berikutnya.", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivityUser::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(RETURN_LAST_TAB, "REC") //Munculkan fragment Menu Rekomendasi ketika MainActivity dibuka
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