package com.ahmed.m.hassaan.prayerstimesapp.data.network.responses

data class ResponseMeta(
    val latitude: Double,
    val latitudeAdjustmentMethod: String,
    val longitude: Double,
    val method: PrayerTimeCalcMethod,
    val midnightMode: String,
    val offset: Offset,
    val school: String,
    val timezone: String
)