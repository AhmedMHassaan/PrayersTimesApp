package com.ahmed.m.hassaan.prayerstimesapp.data.local.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.ahmed.m.hassaan.prayerstimesapp.base.App
import com.ahmed.m.hassaan.prayerstimesapp.data.local.database.PrayersDatabase
import com.ahmed.m.hassaan.prayerstimesapp.data.local.model.PrayerTimesLocal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LocalPrayersRepository {

    private var prayersDatabase: PrayersDatabase? = null

    init {
        prayersDatabase = PrayersDatabase.getInstance(App.mACTIVITY)
    }

    suspend fun insertTimes(prayersTimes: PrayerTimesLocal) {
        prayersDatabase?.prayersDao()?.insert(prayersTimes)?: kotlin.run {
            Log.d(App.APP_TAG, "LocalPrayersRepository - insertTimes:  database is null")
        }
    }


    suspend fun loadPrayersTimes(): PrayerTimesLocal? {
        return prayersDatabase?.prayersDao()?.getPrayerTimes()
    }

}