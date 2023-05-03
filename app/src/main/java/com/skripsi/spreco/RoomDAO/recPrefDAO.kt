package com.skripsi.spreco.RoomDAO

import androidx.room.*
import com.skripsi.spreco.classes.RecPref

@Dao
interface recPrefDAO {
    @Transaction
    @Query("Select * from RecPref where idAcc = :id")
    fun getPCMValue(id: Int) : List<RecPref>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPCM(item : RecPref)

    @Update
    fun updatePCM(item : RecPref)

    @Delete
    fun deletePCM(item : RecPref)

    @Transaction
    fun resetPCM(list : List<RecPref>){
        for(i in list){
            deletePCM(i)
        }
    }

    @Transaction
    fun addAllPCM(list : List<RecPref>){
        for(i in list){
            addPCM(i)
        }
    }
}