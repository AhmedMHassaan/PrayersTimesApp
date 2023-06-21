package com.ahmed.m.hassaan.prayerstimesapp.base.shared_pref

import android.content.Context
import android.content.SharedPreferences
import com.ahmed.m.hassaan.prayerstimesapp.base.App

abstract class BaseSharedPref(prefName: String) {

    private var shared: SharedPreferences = App.mACTIVITY.getSharedPreferences(
        prefName,
        Context.MODE_PRIVATE
    )


    fun clearPreference() {
        shared.edit().clear().apply()
    }

    fun addItem(key: String, value: String) {
        shared.edit().putString(key, value).apply()
    }

    fun addItem(key: String, value: Double) {
        shared.edit().putFloat(key, value.toFloat()).apply()
    }

    fun addItem(key: String, value: Int) {
        shared.edit().putInt(key, value).apply()
    }


    fun getItem(key: String, defVal: String = ""): String =
        shared.getString(key, defVal).toString()

    fun getItem(key: String, defVal: Int = 0): Int =
        shared.getInt(key, defVal)

    fun getItem(key: String, defVal: Float = 0f): Double =
        shared.getFloat(key, defVal).toDouble()


}