package com.ahmed.m.hassaan.prayerstimesapp.data.repository

import com.ahmed.m.hassaan.prayerstimesapp.data.model.response.ApiResponse
import com.ahmed.m.hassaan.prayerstimesapp.data.network.RetrofitClient
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.PrayersTimesResponse
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.QiblaDirectionResponse
import retrofit2.awaitResponse

object QiblaDirectionsRepository {


    suspend fun getKaabaDirections(
        latitude: Double,
        longitude: Double
    ): ApiResponse<QiblaDirectionResponse> {

        try {
            val response = RetrofitClient
                .createQiblaDirectionEdnPointService()
                .getQiblaDirection(latitude, longitude)
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