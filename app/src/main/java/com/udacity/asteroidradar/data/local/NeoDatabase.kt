package com.udacity.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidDatabase::class,PicOfDayDatabase::class], version = 1)
abstract class NeoDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    abstract val picOFDayDao: PicOFDayDao
}
@Volatile
private lateinit var INSTANCE: NeoDatabase

fun getDatabase(context: Context): NeoDatabase {
    synchronized(NeoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    NeoDatabase::class.java,
                    "asteroids").build()
        }
    }
    return INSTANCE
}