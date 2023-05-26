package com.skripsi.spreco

import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skripsi.spreco.RoomDB.DBHelper
import com.skripsi.spreco.classes.*
import java.text.DecimalFormat
import java.text.NumberFormat

object data {
    // Account auth
    var curRole : Char = 'u'
    var currentAccId : Int = -1

    //pcm
    var pcm : MutableList<MutableList<Double>> =  mutableListOf()

    //Methods
    fun formatHarga(harga : Int) : String{
        var formatter : DecimalFormat = NumberFormat.getInstance() as DecimalFormat
        formatter.applyPattern("#,###")
        return formatter.format(harga)
    }

    var caraHitung = "HITUNGAN FAHP-WASPAS\n "

    //ROOM DB
    fun getRoomHelper(context: Context): DBHelper {
        return Room.databaseBuilder(context, DBHelper::class.java, "SPRecommender.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    // Rekomendasi
    var criteriaList = mutableMapOf( //Secara default, semua kriteria bernilai false
        "RAM" to false,
        "ROM" to false,
        "Kapasitas baterai" to false,
        "Kamera belakang" to false,
        "Kamera depan" to false,
        "Ukuran layar" to false,
        "Harga" to false
    )
    var settingDone = false
    var enabledCriteria = mutableListOf<String>()
    var enabledCriteriaType = mutableListOf<Char>()

    var filterHargaChecked = false //Temporary. Hanya pengaturan toggle kriteria yang disimpan
    var hargaRangeAtas = -1
    var hargaRangeBawah = -1
    var maxDataRec = 0

    var bobotKriteria = mutableMapOf<String, Double>()

    //Tambah SP
    var links = mutableListOf<SPSource>()

    //GSON
    fun serialize(matriks : MutableList<MutableList<Double>>) : String{
        val gson = Gson()
        return gson.toJson(matriks)
    }

    fun deserialize(jsonMatriks : String) : MutableList<MutableList<Double>>{
        val gson = Gson()
        val itemType = object : TypeToken<MutableList<MutableList<Double>>>() {}.type
        return gson.fromJson(jsonMatriks, itemType)
    }

    fun serializeHistory(data : MutableList<SP_rank>) : String{
        val gson = Gson()
        return gson.toJson(data)
    }

    fun deserializeHistory(json : String) : MutableList<SP_rank>{
        val gson = Gson()
        val itemType = object : TypeToken<MutableList<SP_rank>>() {}.type
        return gson.fromJson(json, itemType)
    }

    fun Intent.clearStack() {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
}