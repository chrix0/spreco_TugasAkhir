package com.skripsi.spreco.RoomDAO

import androidx.room.*
import com.skripsi.spreco.classes.SPSource

@Dao
interface SpSrcDAO {
    @Query("Select * from SPSource where idSP = :id")
    fun getAllSource(id : Int) : List<SPSource>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSource(source : SPSource)

    @Update
    fun updateSource(source : SPSource)

    @Delete
    fun deleteSource(source : SPSource)

    @Delete
    fun deleteAllSource(listSrc : List<SPSource>)

    @Query("DELETE FROM SPSource")
    fun deleteAllSPSource()

    @Transaction
    fun addAllSource(list : List<SPSource>){
        for(i in list){
            addSource(i)
        }
    }
}