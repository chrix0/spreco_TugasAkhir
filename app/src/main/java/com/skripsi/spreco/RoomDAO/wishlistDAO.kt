package com.skripsi.spreco.RoomDAO

import androidx.room.*
import com.skripsi.spreco.classes.Wishlist

@Dao
interface wishlistDAO {
    @Query("Select * from Wishlist where idAcc = :id")
    fun getAllWish(id : Int) : List<Wishlist>

    @Query("Select * from Wishlist where idAcc = :acc and idSP = :sp")
    fun wishExist(acc : Int, sp : Int) : List<Wishlist>

    @Delete
    fun deleteByObj(wish : Wishlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWishlist(wish : Wishlist)
}