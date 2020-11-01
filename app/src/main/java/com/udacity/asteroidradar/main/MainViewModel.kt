package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.api.Network
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class MainViewModel(apiKey: String) : ViewModel() {

    init {
        viewModelScope.launch {
            val result = Network.asteroids.getAsteroids("feed?start_date=$START_DATE&end_date=$END_DATE&api_key=$apiKey").await()
            result.asteroids.startDate?.forEach {
                Timber.d(it.id)
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
        const val START_DATE = "2015-09-08"
        const val END_DATE = "2015-09-09"
    }
}