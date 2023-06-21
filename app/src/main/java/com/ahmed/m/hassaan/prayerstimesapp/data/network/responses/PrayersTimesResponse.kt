package com.ahmed.m.hassaan.prayerstimesapp.data.network.responses

data class PrayersTimesResponse(
    val code: Int,
    val `data`: ResponseTimingData,
    val status: String
)