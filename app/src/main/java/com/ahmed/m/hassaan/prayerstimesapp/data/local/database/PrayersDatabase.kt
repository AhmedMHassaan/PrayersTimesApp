package com.ahmed.m.hassaan.prayerstimesapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmed.m.hassaan.prayerstimesapp.data.local.dao.PrayersTimesDao
import com.ahmed.m.hassaan.prayerstimesapp.data.local.model.PrayerTimesLocal

@Database(entities = [PrayerTimesLocal::class], version = 2, exportSchema = false)
abstract class PrayersDatabase : RoomDatabase() {

    abstract fun prayersDao(): PrayersTimesDao


    companion object {
        @Volatile
        var instance: PrayersDatabase? = null
        private const val DATABASE_NAME = "prayers_times.db"

        fun getInstance(context: Context): PrayersDatabase? {
            if (instance == null) {
                synchronized(PrayersDatabase::class.java)
                {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context, PrayersDatabase::class.java,
                            DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return instance
        }

    }


}