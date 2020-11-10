package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.data.api.Network
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.asDatabaseModel
import com.udacity.asteroidradar.data.local.NeoDatabase
import com.udacity.asteroidradar.utils.DataState
import com.udacity.asteroidradar.utils.DateHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val asteroidDatabase: NeoDatabase) {

    val allAsteroids = asteroidDatabase.asteroidDao.getAllAsteroids()
    val asteroidsOfWeek = asteroidDatabase.asteroidDao.getWeekAsteroids(DateHelper.getEndOfWeekFormatted())
    val asteroidsOfToday = asteroidDatabase.asteroidDao.getTodayAsteroids(DateHelper.getStartDateFormatted())
    val picOfDay = asteroidDatabase.picOFDayDao.getPictureOfDay()

    suspend fun refreshAsteroids(apiKey: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                emit(fetchAsteroids(apiKey))
            }
        }.flowOn(dispatcher)
    }

    suspend fun fetchAsteroids(apiKey: String): DataState {
        return try {
            val result = Network.asteroids.getAsteroids(
                    "neo/rest/v1/feed?start_date=" +
                            "${DateHelper.getStartDateFormatted()}&end_date=" +
                            "${DateHelper.getEndDateFormatted()}&api_key=$apiKey").await()
            val jsonObject = JSONObject(result)
            val resultParsed = parseAsteroidsJsonResult(jsonObject)
            asteroidDatabase.asteroidDao.insertAll(resultParsed.asDatabaseModel())
            DataState.Success
        } catch (throwable: Throwable) {
            DataState.Error
        }
    }

    suspend fun fetchPictureOfDay(apiKey: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(dispatcher) {
            try {
                val result = Network.asteroids.getImageOfDay("planetary/apod?api_key=$apiKey").await()
                if(result.mediaType == "image") {
                    asteroidDatabase.picOFDayDao.insertPictureOfDay(result.asDatabaseModel())
                }
            } catch (throwable: Throwable) {}
        }
    }

    suspend fun deleteAsteroidsBeforeToday(coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(coroutineDispatcher) {
            asteroidDatabase.asteroidDao.deleteBeforeToday(DateHelper.getStartDateFormatted())
        }
    }
}