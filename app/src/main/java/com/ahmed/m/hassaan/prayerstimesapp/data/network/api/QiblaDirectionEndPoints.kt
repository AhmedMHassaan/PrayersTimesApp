package com.ahmed.m.hassaan.prayerstimesapp.data.network.api

import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.QiblaDirectionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface QiblaDirectionEndPoints {


    @GET("v1/qibla/{latitude}/{longitude}")
    fun getQiblaDirection(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): Call<QiblaDirectionResponse>

}