package com.udacity.asteroidradar.data

import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.local.AsteroidEntity
import com.udacity.asteroidradar.data.local.PicOfDayDatabase

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth =  it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asDatabaseModel(): List<AsteroidEntity> {
    return map {
        AsteroidEntity(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth =  it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun PictureOfDay.asDatabaseModel(): PicOfDayDatabase {
    return PicOfDayDatabase(
            url = this.url,
            mediaType =  this.mediaType,
            title = this.title
    )
}

fun PicOfDayDatabase?.asDomainModel(): PictureOfDay? {
    return if(this != null) {
        PictureOfDay(
                url = this.url,
                mediaType = this.mediaType,
                title = this.title
        )
    } else null
}