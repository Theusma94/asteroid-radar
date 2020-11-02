package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.api.Network
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import timber.log.Timber

class MainViewModel(apiKey: String) : ViewModel() {

    init {
        viewModelScope.launch {
            val result = Network.asteroids.getAsteroids("neo/rest/v1/feed?start_date=$START_DATE&end_date=$END_DATE&api_key=$apiKey").await()
            val jsonObject = JSONObject(result)
            val resultParsed = parseAsteroidsJsonResult(jsonObject)
            resultParsed.forEach {
                Timber.d(it.toString())
            }
        }
    }

    class Factory(private val apiKey: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(apiKey) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    companion object {
        const val START_DATE = "2020-11-01"
        const val END_DATE = "2020-11-03"
    }
}