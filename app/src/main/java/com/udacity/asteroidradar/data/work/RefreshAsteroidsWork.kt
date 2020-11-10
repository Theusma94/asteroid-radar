/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data.local.getDatabase
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.DataState
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class RefreshAsteroidsWork(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.fetchAsteroids(applicationContext.getString(R.string.API_KEY))
            repository.fetchPictureOfDay(applicationContext.getString(R.string.API_KEY))
            repository.deleteAsteroidsBeforeToday(Dispatchers.Default)
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}
