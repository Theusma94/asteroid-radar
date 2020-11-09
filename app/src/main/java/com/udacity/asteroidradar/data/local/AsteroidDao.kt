package com.udacity.asteroidradar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.data.domain.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<AsteroidEntity>)

    @Query("SELECT * FROM asteroid ORDER BY date(closeApproachDate) DESC")
    fun getAllAsteroids(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate <= :lastDayOfWeek ORDER BY date(closeApproachDate) DESC")
    fun getWeekAsteroids(lastDayOfWeek: String): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroid WHERE closeApproachDate == :today ORDER BY date(closeApproachDate) DESC")
    fun getTodayAsteroids(today: String): Flow<List<AsteroidEntity>>

    @Query("DELETE FROM asteroid WHERE date(closeApproachDate) < date(:today)")
    fun deleteBeforeToday(today: String)
}