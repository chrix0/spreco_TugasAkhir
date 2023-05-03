package com.skripsi.spreco.RoomDAO

import androidx.room.*
import com.skripsi.spreco.classes.Account

@Dao
interface AccountDAO {
    //SELECT (SETELAH OPTIMASI)
    @Transaction
    @Query("Select * from Account")
    fun getAllAcc() : List<Account>

    @Query("Select * from Account where username = :username and password = :password")
    fun getAcc(username : String, password : String) : List<Account>

    @Query("Select * from Account where username = :username")
    fun getAccUserCheck(username : String) : List<Account>

    @Query("Select * from Account where idAcc = :id")
    fun getAccById(id: Int) : List<Account>

    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun newAcc(acc : Account)

    //UPDATE
    @Update
    fun updateAcc(acc : Account)

    //DELETE
    @Query("DELETE FROM Account WHERE idAcc = :id")
    fun deleteById(id: Int)

    @Transaction
    fun addAllAcc(list : List<Account>){
        for(i in list){
            newAcc(i)
        }
    }
}