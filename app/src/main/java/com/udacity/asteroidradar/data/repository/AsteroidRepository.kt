package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.data.api.Network
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.asDatabaseModel
import com.udacity.asteroidradar.data.local.AsteroidDatabase
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.utils.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val asteroidDatabase: AsteroidDatabase) {

    val asteroidsLocal = asteroidDatabase.asteroidDao.getAllAsteroids()

    suspend fun refreshAsteroids(apiKey: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                try {
                    val result = Network.asteroids.getAsteroids(
                            "neo/rest/v1/feed?start_date=${MainViewModel.START_DATE}&end_date=${MainViewModel.END_DATE}&api_key=$apiKey").await()
                    val jsonObject = JSONObject(result)
                    val resultParsed = parseAsteroidsJsonResult(jsonObject)
                    asteroidDatabase.asteroidDao.insertAll(resultParsed.asDatabaseModel())
                    emit(DataState.Success)
                } catch (throwable: Throwable) {
                    emit(DataState.Error)
                }
            }
        }.flowOn(dispatcher)
    }
}