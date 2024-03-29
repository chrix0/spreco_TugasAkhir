package com.skripsi.spreco.RoomDAO

import androidx.room.*
import com.skripsi.spreco.classes.Smartphone

@Dao
interface SpDAO {
    //SELECT ALL (SETELAH DIOPTIMASI)
    @Transaction
    @Query("Select * from Smartphone")
    fun getAllSP() : List<Smartphone>

    @Transaction
    @Query("Select * from Smartphone where idSP = :id")
    fun getSPDetail(id: Int) : List<Smartphone>

    @Query("Select * from Smartphone where namaSP like :name")
    fun getSPLike(name: String) : List<Smartphone> //Isi sendiri bagian name, misalnya "%yang dicari%"

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSP(item : Smartphone) : Long

    @Update
    fun updateSP(item : Smartphone)

    @Delete
    fun deleteSP(item : Smartphone)

    @Query("DELETE FROM Smartphone")
    fun deleteAllSP()

    @Transaction
    fun addAllSP(list : List<Smartphone>){
        for(i in list){
            addSP(i)
        }
    }
    @Transaction
    fun updateAllSP(list : List<Smartphone>){
        for(i in list){
            updateSP(i)
        }
    }
}