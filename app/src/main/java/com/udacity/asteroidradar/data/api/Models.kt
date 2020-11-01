package com.udacity.asteroidradar.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.main.MainViewModel

@JsonClass(generateAdapter = true)
data class NetworkContainer(@Json(name = "near_earth_objects") val asteroids: NetworkAsteroidContainer)

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(
        @Json(name = MainViewModel.START_DATE) val startDate: List<NetworkAsteroid>?,
        @Json(name = MainViewModel.END_DATE) val endDate: List<NetworkAsteroid>?
)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
        val id: String,
        @Json(name = "absolute_magnitude_h") val absoluteMagnitude: Double,
        @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean,
        @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameter,
        @Json(name = "close_approach_data") val containerApproachData: List<ApproachDataContainer>
)

@JsonClass(generateAdapter = true)
data class EstimatedDiameter(
        val kilometers: Kilometers
)

@JsonClass(generateAdapter = true)
data class Kilometers(
        @Json(name = "estimated_diameter_max") val estimatedDiameterMax: Double
)

@JsonClass(generateAdapter = true)
data class ApproachDataContainer(
        @Json(name = "relative_velocity") val relativeVelocity: RelativeVelocity,
        @Json(name = "miss_distance") val missDistance: MissDistance
)

@JsonClass(generateAdapter = true)
data class RelativeVelocity(@Json(name = "kilometers_per_second") val kmPerSecond: String)

@JsonClass(generateAdapter = true)
data class MissDistance(val astronomical: String)
