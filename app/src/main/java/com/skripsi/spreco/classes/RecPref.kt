package com.skripsi.spreco.classes

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    //Foreign key
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("idAcc"),
            childColumns = arrayOf("idAcc"),
            onDelete = ForeignKey.CASCADE),
    ],
    primaryKeys = ["idAcc"]
)
data class RecPref(
    var idAcc : Int,
    var prefPCM : String
)