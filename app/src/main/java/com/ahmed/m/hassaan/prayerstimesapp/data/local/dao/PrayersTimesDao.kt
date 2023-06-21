package com.ahmed.m.hassaan.prayerstimesapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmed.m.hassaan.prayerstimesapp.data.local.model.PrayerTimesLocal

@Dao
interface PrayersTimesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(times: PrayerTimesLocal)


    @Query("SELECT * FROM prayers_times  limit 1 ")
    suspend fun getPrayerTimes(): PrayerTimesLocal


}