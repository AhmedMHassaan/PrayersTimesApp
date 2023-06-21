package com.ahmed.m.hassaan.prayerstimesapp.data.network.responses

data class Hijri(
    val date: String,
    val day: String,
    val designation: Designation,
    val format: String,
    val holidays: List<String>,
    val month: HijriMonth,
    val weekday: HijriWeekday,
    val year: String
)