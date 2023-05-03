package com.skripsi.spreco.classes

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    //Foreign key
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("idAcc"),
            childColumns = arrayOf("idAcc"),
            onDelete = ForeignKey.CASCADE),
    ],
//    //Composite key, tapi idRec perlu autoGenerate jadi perlu dibuat seperti ini..
//    indices = [Index(value = ["idAcc"], unique = true)]
) @Parcelize
data class recHistory(
    @PrimaryKey(autoGenerate = true)
    var idRec : Int,
    var idAcc : Int,
    var result : String,
    var datetime : String,
    var rangeHargaAwal : Int,
    var rangeHargaAkhir : Int,
    var resultLimit : Int,
    var criteriaList : String,
    var prefPCM : String
) : Parcelable