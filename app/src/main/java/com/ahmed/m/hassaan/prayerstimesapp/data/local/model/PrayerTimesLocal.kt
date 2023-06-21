package com.ahmed.m.hassaan.prayerstimesapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.PrayersTimes

@Entity("prayers_times")
data class PrayerTimesLocal(

    val fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String,
    @PrimaryKey(autoGenerate = false)
    val date: String
) {


    fun toPrayerTime(): PrayersTimes {
        return PrayersTimes(
            this.fajr,
            this.Sunrise,
            this.Dhuhr,
            this.Asr,
            "",
            this.Maghrib,
            this.Isha,
            "",
            ""
        )
    }


}