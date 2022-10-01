package com.skripsi.spreco.activityUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.skripsi.spreco.R
import com.skripsi.spreco.data
import com.skripsi.spreco.fahp_waspas
import com.skripsi.spreco.fahp_waspas.FAHP
import com.skripsi.spreco.fahp_waspas.FAHP_WASPAS

class user_recshow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_recshow)

        val actionbar = supportActionBar
        actionbar!!.title = "Rekomendasi"
        actionbar.setDisplayHomeAsUpEnabled(true)

        var pcm : MutableList<MutableList<Double>> =
            mutableListOf(
                mutableListOf(1.0, 2.0, 1.0/2.0, 1.0/4.0, 4.0, 2.0),
                mutableListOf(0.0, 1.0, 3.0, 1.0/5.0, 2.0, 3.0),
                mutableListOf(0.0, 0.0, 1.0, 2.0, 1.0/3.0, 3.0),
                mutableListOf(0.0, 0.0, 0.0, 1.0, 4.0, 4.0),
                mutableListOf(0.0, 0.0, 0.0, 0.0, 1.0, 5.0),
                mutableListOf(0.0, 0.0, 0.0, 0.0, 0.0, 1.0)
            )

        var criteriaLabel : MutableList<String> = mutableListOf("C1", "C2", "C3", "C4", "C5", "C6")
        var pcmInput : MutableList<MutableList<Double>> = mutableListOf()
        var allFilled = true
        //Input Check
        for (i in 0 until pcm.size){
            for(j in 0..i){
                if(i != j){
                    if(!pcm[j][i].equals(0.0)){
                        print("Sudah diinput : ${criteriaLabel[j]} dengan ${criteriaLabel[i]} = ${pcm[j][i]}\n")
                    }
                    else{
                        print("Belum diinput : ${criteriaLabel[j]} dengan ${criteriaLabel[i]}\n")
                        allFilled = false
                    }
                }
            }
        }


        if(allFilled){
            print("Semua sudah diinput!\n")
            print("Bobot setiap kriteria setelah perhitungan FAHP: " + FAHP(pcm).toString() + "\n")
            print("Jumlah bobot: " + FAHP(pcm).sumOf{it}.toString() + "\n")
            print("Hasil ranking: ")
            var list_converted = data.convert_ke_nilai_kriteria(data.list_sp)
            var resFAHPWASPAS = FAHP_WASPAS(list_converted, pcm)
            var res = fahp_waspas.makeRanking(data.list_sp, resFAHPWASPAS)
            Toast.makeText(this, res.toString(), Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this,"Terdapat elemen yang masih bernilai 0", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
