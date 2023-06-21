package com.ahmed.m.hassaan.prayerstimesapp.data.local.repo

import com.ahmed.m.hassaan.prayerstimesapp.data.local.shared_ref.AddressCacheSharedPref

object LocationRepository {

    fun saveLocation(latitude: Double, longitude: Double) {
        AddressCacheSharedPref.saveLocation(latitude, longitude)
    }

    fun getSavedLatitude() = AddressCacheSharedPref.getSavedLatitude()

    fun getSavedLongitude() = AddressCacheSharedPref.getSavedLongitude()
    fun saveAddress(address: String) {
        AddressCacheSharedPref.cacheAddress(address)
    }

    fun getAddress() = AddressCacheSharedPref.getCachedAddress()


}