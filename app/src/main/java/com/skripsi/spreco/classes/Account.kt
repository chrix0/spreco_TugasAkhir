package com.skripsi.spreco.classes

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Account (
    @PrimaryKey(autoGenerate = true)
    var idAcc : Int,

    @ColumnInfo
    var namaAcc : String,

    @ColumnInfo
    var username : String,

    @ColumnInfo
    var password : String,

    @ColumnInfo
    var terakhirLogin : String,
): Parcelable