package com.skripsi.spreco.RoomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skripsi.spreco.RoomDAO.*
import com.skripsi.spreco.classes.*

@Database(
    entities =
    [
        Account::class,
        Smartphone::class,
        Wishlist::class,
        SPSource::class,
        ChosenCriteria::class,
//        RecPref::class,
        recHistory::class
    ],
    version = 12)
abstract class DBHelper : RoomDatabase(){
    abstract fun daoAccount() : AccountDAO
    abstract fun daoSP() : SpDAO
    abstract fun daoWishlist() : wishlistDAO
    abstract fun daoSPSource() : SpSrcDAO
    abstract fun daoToggle() : CToggleDAO
//    abstract fun daoRecPref() : recPrefDAO
    abstract fun daoRecHistory() : recHistoryDAO
}