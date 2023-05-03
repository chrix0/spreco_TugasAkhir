package com.skripsi.spreco.classes

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.android.parcel.Parcelize

@Entity(
    //Foreign key
    foreignKeys = [
        ForeignKey(
        entity = Account::class,
        parentColumns = arrayOf("idAcc"),
        childColumns = arrayOf("idAcc"),
        onDelete = ForeignKey.CASCADE),

        ForeignKey(entity = Smartphone::class,
        parentColumns = arrayOf("idSP"),
        childColumns = arrayOf("idSP"),
        onDelete = ForeignKey.CASCADE)
    ],
    //Composite key
    primaryKeys = ["idAcc", "idSP"]
)
@Parcelize
data class Wishlist (
    @ColumnInfo(name = "idAcc")
    var idAcc : Int,
    @ColumnInfo(name = "idSP")
    var idSP : Int
): Parcelable