package com.skripsi.spreco.sharedPref

import android.content.Context
import android.content.SharedPreferences

class adminPassSharedPref(context: Context) {
    val adminConstant = "ADMIN"

    private var myPreferences : SharedPreferences = context.getSharedPreferences("sharedprefAdminPass", Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operator :(SharedPreferences.Editor) -> Unit){
        val editMe = edit() //method edit berasal dari SharedPreferences
        operator(editMe) //operator ini seperti putString, putInteger, putBoolean
        editMe.apply()
    }

    var adminPass : String?
        get() = myPreferences.getString(adminConstant, "") //Nilai defaultnya adalah string kosong
        // String kosong berarti password admin belum diset
        set(value){
            myPreferences.editMe{
                it.putString(adminConstant, value!!)
            }
        }

    fun resetKept(){
        myPreferences.editMe{
            it.clear()
        }
    }
}