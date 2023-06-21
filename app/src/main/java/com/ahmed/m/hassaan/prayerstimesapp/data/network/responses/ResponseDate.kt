package com.ahmed.m.hassaan.prayerstimesapp.data.network.responses

data class ResponseDate(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)