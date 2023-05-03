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

        val getHistory : recHistory = intent.getParcelableExtra<Parcelable>(SHOW_PAST_REC_SETTINGS) as recHistory

        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = ""

        //initializing data

        val typeToken = object : TypeToken<MutableMap<String, Double>>() {}.type
        val bobotKriteriaList = Gson().fromJson<MutableMap<String, Double>>(getHistory.criteriaList, typeToken)

        //input data and fit data into pie chart entry

        //input data and fit data into pie chart entry
        for (kriteria in bobotKriteriaList.keys) {
            pieEntries.add(PieEntry(bobotKriteriaList[kriteria]!!.toFloat(), kriteria))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        fun generateColors(): List<Int> {
            val colors = mutableListOf<Int>()
            val colorCount = 8 // the number of colors you want to generate

            for (i in 0 until colorCount) {
                val hsv = FloatArray(3)
                hsv[0] = (i * 360f / colorCount) % 360 // generate hues evenly spaced around the color wheel
                hsv[1] = 0.7f // set the saturation to 70%
                hsv[2] = 0.9f // set the brightness to 90%
                colors.add(Color.HSVToColor(hsv))
            }
            return colors
        }
        pieDataSet.colors = generateColors()
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        pieData.setValueFormatter(PercentFormatter(pieChartBobot))

        val legend: Legend = pieChartBobot.legend
//        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.yOffset = 25f;//here value changes
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
        legend.isWordWrapEnabled = true
        legend.textSize = 13f
        legend.orientation = Legend.LegendOrientation.VERTICAL



        pieChartBobot.data = pieData
        pieChartBobot.setUsePercentValues(true);
        pieChartBobot.setEntryLabelColor(Color.BLACK);
        pieChartBobot.description.isEnabled = false
        pieChartBobot.invalidate()

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