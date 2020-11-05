package com.udacity.asteroidradar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_of_day")
data class PicOfDayDatabase(
        @PrimaryKey(autoGenerate = true) val id: Long = 0L,
        val url: String,
        val mediaType: String,
        val title: String
)