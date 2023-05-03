package com.skripsi.spreco.RoomDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.skripsi.spreco.classes.recHistory

@Dao
interface recHistoryDAO {
    @Query("Select * from recHistory where idAcc = :idAcc")
    fun getHistory(idAcc: Int) : List<recHistory>

    @Insert
    fun addHistory(history: recHistory)
}