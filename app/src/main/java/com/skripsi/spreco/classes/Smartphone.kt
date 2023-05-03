package com.skripsi.spreco.classes

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity @Parcelize
data class Smartphone(
    @PrimaryKey(autoGenerate = true)
    var idSP : Int,

    @ColumnInfo
    var namaSP : String,

    @ColumnInfo
    var merek : String,

    @ColumnInfo
    var harga : Int,

    @ColumnInfo
    var network : String,

    @ColumnInfo
    var sim : String,

    @ColumnInfo
    var tDisplay : String,

    @ColumnInfo
    var uDisplay : Double,

    @ColumnInfo
    var OS : String,

    @ColumnInfo
    var chipset : String,

    @ColumnInfo
    var cpu : String,

    @ColumnInfo
    var gpu : String,

    @ColumnInfo
    var ram : Int,

    @ColumnInfo
    var rom : Int,

    @ColumnInfo
    var mainCam : Double,

    @ColumnInfo
    var selfieCam : Double,

    @ColumnInfo
    var battery : Int,

    @ColumnInfo
    var warna : String,

    @ColumnInfo
    var tanggalRilis : String,

    @ColumnInfo
    var picURL : String
) : Parcelable