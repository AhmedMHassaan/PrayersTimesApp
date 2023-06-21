package com.ahmed.m.hassaan.prayerstimesapp.data.local.shared_ref

import com.ahmed.m.hassaan.prayerstimesapp.base.shared_pref.BaseSharedPref

object AddressCacheSharedPref : BaseSharedPref("address_cach") {

    private const val addressKey = "Address"
    private const val latitudeKey = "latitude"
    private const val longitudeKey = "longitude"


    fun cacheAddress(address: String) {
        addItem(addressKey, address)

    }

    fun getCachedAddress(): String {
        return getItem(addressKey, "")
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        addItem(latitudeKey, latitude)
        addItem(longitudeKey, longitude)
    }

    fun getSavedLatitude() = getItem(latitudeKey, 0f)

    fun getSavedLongitude() = getItem(longitudeKey, 0f)

}