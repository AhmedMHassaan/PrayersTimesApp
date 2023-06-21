package com.ahmed.m.hassaan.prayerstimesapp.data.network.responses

import android.util.Log
import com.ahmed.m.hassaan.prayerstimesapp.base.App
import com.ahmed.m.hassaan.prayerstimesapp.data.local.model.PrayerTimesLocal
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class PrayersTimes(
    var Fajr: String,
    var Sunrise: String,
    var Dhuhr: String,
    var Asr: String,
    var Sunset: String,
    var Maghrib: String,
    var Isha: String,
    var Midnight: String,
    var Imsak: String
) {


    fun convertTo12(): PrayersTimes {
            return  this.also {
                val format = SimpleDateFormat("hh:mm aaa", Locale.forLanguageTag("ar"))
                this.Fajr = format.format(toJodaDateTime(this.Fajr).toDate())
                this.Sunrise = format.format(toJodaDateTime(this.Sunrise).toDate())
                this.Dhuhr = format.format(toJodaDateTime(this.Dhuhr).toDate())
                this.Asr = format.format(toJodaDateTime(this.Asr).toDate())
//                this.Sunset = format.format(toJodaDateTime(this.Sunset).toDate())
                this.Maghrib = format.format(toJodaDateTime(this.Maghrib).toDate())
                this.Isha = format.format(toJodaDateTime(this.Isha).toDate())
            }
    }

    /*fun convertTo24(): PrayersTimes {
        return removeEEST()
            .also {
                val format = SimpleDateFormat("HH:mm", Locale.forLanguageTag("ar"))
                this.Fajr = format.format(toJodaDateTime(this.Fajr).toDate())
                this.Sunrise = format.format(toJodaDateTime(this.Sunrise).toDate())
                this.Dhuhr = format.format(toJodaDateTime(this.Dhuhr).toDate())
                this.Asr = format.format(toJodaDateTime(this.Asr).toDate())
                this.Sunset = format.format(toJodaDateTime(this.Sunset).toDate())
                this.Maghrib = format.format(toJodaDateTime(this.Maghrib).toDate())
                this.Isha = format.format(toJodaDateTime(this.Isha).toDate())
            }

    }*/

    fun toJodaDateTime(prayer: String): DateTime {
//        Log.d(App.APP_TAG, "PrayersTimes - toJodaDateTime:  prayer befor convert is $prayer")
        val prayerTime = SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(prayer.trim())
        return DateTime.now().toMutableDateTime().also {
            it.setTime(DateTime(prayerTime).hourOfDay, DateTime(prayerTime).minuteOfHour, 0, 0)
        }.toDateTime()

    }


}