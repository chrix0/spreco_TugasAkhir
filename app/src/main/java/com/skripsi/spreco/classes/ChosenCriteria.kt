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
    //Composite key
    primaryKeys = ["idAcc", "criteria"]
)
data class ChosenCriteria(
    var idAcc : Int,
    var criteria : String,
    var type: Char
)