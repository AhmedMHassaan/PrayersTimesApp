package com.ahmed.m.hassaan.prayerstimesapp.data.network.api

import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.PrayersTimesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayerTimesEndPoints {


    //    @GET("v1/calendar/{year}/{month}")
//    @GET("v1/calendar")
    @GET("v1/timings/{date}") // date as DD-MM-YYYY
     fun getPrayersTimes(
        @Path("date") date:String,
        @Query("latitude") latitude:Double,
        @Query("longitude") longitude: Double
//        @Query("method") method: Int = 5,
    ): Call<PrayersTimesResponse>
}