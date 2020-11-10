package com.udacity.asteroidradar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.data.domain.Asteroid

@Entity(tableName = "asteroid")
data class AsteroidDatabase(
        @PrimaryKey val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
)