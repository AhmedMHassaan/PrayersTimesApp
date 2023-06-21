package com.ahmed.m.hassaan.prayerstimesapp.data.network


import com.ahmed.m.hassaan.prayerstimesapp.data.network.api.PrayerTimesEndPoints
import com.ahmed.m.hassaan.prayerstimesapp.data.network.api.QiblaDirectionEndPoints
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {

    private const val baseUrl = "https://api.aladhan.com"

    private val retrofit: Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()


    fun createPrayerTimesEndPointService(): PrayerTimesEndPoints =
        retrofit.create(PrayerTimesEndPoints::class.java)

    fun createQiblaDirectionEdnPointService(): QiblaDirectionEndPoints =
        retrofit.create(QiblaDirectionEndPoints::class.java)

}