package com.example.jobfinder.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(sharedPreference:SharedPreferences) {
    lateinit var sharedPreference:SharedPreferences
    var editor: SharedPreferences.Editor = sharedPreference.edit()

    fun putBoolean(key:String,value:Boolean){
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun getBoolean(key: String):Boolean{
        return sharedPreference.getBoolean(key,false)
    }

    fun putString(key:String,value:String){
        editor.putString(key,value)
        editor.apply()
    }

    fun getString(key:String): String? {
        return sharedPreference.getString(key,null)
    }

    fun clear(){
        editor.clear()
        editor.apply()
    }

}