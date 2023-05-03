package com.skripsi.spreco.classes

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.android.parcel.Parcelize

@Entity(
    //Foreign key
    foreignKeys = [
        ForeignKey(entity = Smartphone::class,
            parentColumns = arrayOf("idSP"),
            childColumns = arrayOf("idSP"),
            onDelete = ForeignKey.CASCADE)
    ],
    //Composite key
    primaryKeys = ["idSP", "source"]
)
@Parcelize
data class SPSource(
    var idSP : Int,
    var source : String,
    var sourceLink : String,
    var dateAdded : String
) : Parcelable
