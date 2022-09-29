package com.skripsi.spreco.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SP(
    var id : Int,
    var nama : String,
    var harga : Int,
    var network : String,
    var SIM : String,
    var tipeDisplay : String,
    var ukuranDisplay : Double,
    var OS : String,
    var chipset : String,
    var CPU : String,
    var GPU : String,
    var RAM : Int,
    var ROM : Int,
    var mainCam : Double,
    var selfieCam : Double,
    var battery : Int,
    var warna : String,
    var score : Int,
    var picURL : String
) : Parcelable