package com.udacity.asteroidradar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PicOFDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfDay(picOfDay: PicOfDayDatabase)

    @Query("SELECT * FROM picture_of_day")
    fun getPictureOfDay(): Flow<PicOfDayDatabase>

}