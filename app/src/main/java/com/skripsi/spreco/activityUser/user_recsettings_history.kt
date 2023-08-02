package com.skripsi.spreco.activityUser

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skripsi.spreco.R
import com.skripsi.spreco.SHOW_PAST_REC_SETTINGS
import com.skripsi.spreco.classes.recHistory
import kotlinx.android.synthetic.main.activity_user_recsettings_history.*


class user_recsettings_history : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_recsettings_history)
        // Pakai https://weeklycoding.com/mpandroidchart/rec

        val actionbar = supportActionBar
        actionbar!!.title = "Rekomendasi"
        actionbar.setDisplayHomeAsUpEnabled(true)

        //Ambil data kriteria dan bobot, yang masih dalam bentuk JSON
        val getHistory : recHistory = intent.getParcelableExtra<Parcelable>(SHOW_PAST_REC_SETTINGS) as recHistory
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = ""

        // Siapkan typeToken untuk konversi string JSON menjadi MutableMap
        val typeToken = object : TypeToken<MutableMap<String, Double>>() {}.type
        // Ubah JSON menjadi bentuk MutableMap<String, Double> (String sebagai key, Double sebagai value)
        val bobotKriteriaList = Gson().fromJson<MutableMap<String, Double>>(getHistory.criteriaList, typeToken)

        // Masukkan nilai value setiap key, serta nama key (kriterianya) ke dalam pieEntries
        for (kriteria in bobotKriteriaList.keys) {
            pieEntries.add(PieEntry(bobotKriteriaList[kriteria]!!.toFloat(), kriteria))
        }

        // Hubungkan semua pieEntries dengan label kosong (Labelnya sudah tersedia di pieEntries)
        val pieDataSet = PieDataSet(pieEntries, label)

        // Ukuran teks label
        pieDataSet.valueTextSize = 12f

        // Membuat warna-warna yang digunakan dalam pieChart
        fun generateColors(): List<Int> {
            // Warna ditentukan dengan Hue Saturation Color (HSV)
            val colors = mutableListOf<Int>()
            val colorCount = 8 // Buat 8 warna

            for (i in 0 until colorCount) {
                val hsv = FloatArray(3)
                hsv[0] = (i * 360f / colorCount) % 360 // Hue: Diambil berdasarkan posisi color wheel dengan rumus ini
                hsv[1] = 0.7f // Saturation: 70%
                hsv[2] = 0.9f // Brightness: 90%
                colors.add(Color.HSVToColor(hsv)) //Ubah HSV ke warna yang dapat diproses aplikasi
            }
            return colors
        }

        pieDataSet.colors = generateColors() //Berisi 8 warna

        //Masukkan dataset ke dalam PieData
        val pieData = PieData(pieDataSet)

        //Munculkan nilai bobot dalam bentuk persen dalam piechart
        pieData.setDrawValues(true)
        pieData.setValueFormatter(PercentFormatter(pieChartBobot)) //Tambahkan tanda persen di belakangnya

        //pieChartBobot: Elemen piechart di view
        //Tambahkan legenda yang disusun secara vertikal
        val legend: Legend = pieChartBobot.legend
        legend.setDrawInside(false)
        legend.yOffset = 25f;
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
        legend.isWordWrapEnabled = true
        legend.textSize = 13f
        legend.orientation = Legend.LegendOrientation.VERTICAL

        // Masukkan data ke dalam piechart
        pieChartBobot.data = pieData
        pieChartBobot.setUsePercentValues(true);
        pieChartBobot.setEntryLabelColor(Color.BLACK);
        pieChartBobot.description.isEnabled = false
        pieChartBobot.invalidate() //Refresh untuk memunculkan data kriteria-bobot pada piechart

        if ((getHistory.rangeHargaAwal != -1) and (getHistory.rangeHargaAkhir != -1)){
            filterHargaHistory.isChecked = true
            filterStart.setText("${getHistory.rangeHargaAwal}")
            filterEnd.setText("${getHistory.rangeHargaAkhir}")
        }

        dataCount.setText("${getHistory.resultLimit}")
    }

    override fun onSupportNavigateUp(): Boolean {
        //onBackPressed() deprecated, jadi perlu menggunakan onBackPressedDispatcher.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}