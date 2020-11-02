package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.api.Network
import com.udacity.asteroidradar.data.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(apiKey: String) : ViewModel() {

    private var _resultAsteroids = MutableLiveData<List<Asteroid>>()
    val resultAsteroids: LiveData<List<Asteroid>> = _resultAsteroids

    init {
        viewModelScope.launch {
            val result = Network.asteroids.getAsteroids("neo/rest/v1/feed?start_date=$START_DATE&end_date=$END_DATE&api_key=$apiKey").await()
            val jsonObject = JSONObject(result)
            val resultParsed = parseAsteroidsJsonResult(jsonObject)
            _resultAsteroids.value = resultParsed
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