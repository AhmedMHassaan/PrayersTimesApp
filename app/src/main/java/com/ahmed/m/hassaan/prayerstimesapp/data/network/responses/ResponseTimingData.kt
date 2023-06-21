package com.ahmed.m.hassaan.prayerstimesapp.data.network.responses

data class ResponseTimingData(
    val date: ResponseDate,
    val responseMeta: ResponseMeta,
    val timings: PrayersTimes
)
