package com.ahmed.m.hassaan.prayerstimesapp.data.repository

import com.ahmed.m.hassaan.prayerstimesapp.data.model.response.ApiResponse
import com.ahmed.m.hassaan.prayerstimesapp.data.network.RetrofitClient
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.PrayersTimesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.util.Date

object PrayersTimesRepository {

    suspend fun getPrayersTimes(
        date:String,
        latitude: Double,
        longitude: Double
    ): ApiResponse<PrayersTimesResponse> {

        try {
            val response = RetrofitClient
                .createPrayerTimesEndPointService()
                .getPrayersTimes(date,latitude, longitude)
                .awaitResponse()
            if (response.isSuccessful) {
                return response.body()?.let {
                    ApiResponse.DataApiResponse(response.body()!!)
                } ?: return ApiResponse.ErrorApiResponse("Null Prayers times in repo")

            } else {
                return ApiResponse.ErrorApiResponse(response.errorBody()?.string().toString())
            }
        } catch (e: Throwable) {
            return ApiResponse.ErrorApiResponse(e.message.toString())
        }


    }

}