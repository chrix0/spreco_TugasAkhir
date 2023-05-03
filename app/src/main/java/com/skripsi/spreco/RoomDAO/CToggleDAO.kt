package com.skripsi.spreco.RoomDAO

import androidx.room.*
import com.skripsi.spreco.classes.ChosenCriteria

@Dao
interface CToggleDAO {
    @Transaction
    @Query("Select * from ChosenCriteria where idAcc = :id")
    fun getToggleStatus(id : Int) : List<ChosenCriteria>

    @Query("Select * from ChosenCriteria where idAcc = :id and criteria = :criteria")
    fun criteriaExists(id : Int, criteria: String) : List<ChosenCriteria>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun onToggle(item : ChosenCriteria)

    @Delete
    fun offToggle(item : ChosenCriteria)

    @Transaction
    fun onToggleThese(list : List<ChosenCriteria>){
        for(i in list){
            onToggle(i)
        }
    }
}