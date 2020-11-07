package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.asDomainModel
import com.udacity.asteroidradar.data.local.getDatabase
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.DataState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val apiKey: String, application: Application) : AndroidViewModel(application) {

    private var _resultAsteroids = MutableLiveData<List<Asteroid>>()
    val resultAsteroids: LiveData<List<Asteroid>> = _resultAsteroids

    private var _statusLoading = MutableLiveData<Boolean>()
    val statusLoading: LiveData<Boolean> = _statusLoading

    private var _urlPicOfDay = MutableLiveData<String>()
    val urlPicOfDay: LiveData<String> = _urlPicOfDay

    private val asteroidRepository: AsteroidRepository by lazy {
        val database = getDatabase(getApplication())
        AsteroidRepository(database)
    }
    init {
        getAllAsteoids()

        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            asteroidRepository.picOfDay.map {
                it.asDomainModel()
            }.collect {
                if (it != null) {
                    _urlPicOfDay.postValue(it.url)
                } else {
                    fetchPicOfDay()
                }
            }
        }
    }

    fun getAllAsteoids() {
        viewModelScope.launch {
            asteroidRepository.asteroidsLocal.map {
                it.asDomainModel()
            }.collect {
                if (it.isNotEmpty()) {
                    _resultAsteroids.postValue(it)
                } else {
                    fetchAsteroids()
                }
            }
        }
    }

    fun getTodayAsteroids() {
        viewModelScope.launch {
            asteroidRepository.asteroidsOfToday.map {
                it.asDomainModel()
            }.collect {
                _resultAsteroids.value = it
            }
        }
    }

    private fun fetchPicOfDay() {
        viewModelScope.launch {
            asteroidRepository.refreshPicOfDay(apiKey)
        }
    }

    private fun fetchAsteroids() {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids(apiKey).collect { state ->
                when(state) {
                    DataState.Loading -> {
                        _statusLoading.value = true
                    }
                    DataState.Error -> {
                        _statusLoading.value = false
                    }
                    DataState.Success -> {
                        _statusLoading.value = false
                    }
                }
            }
        }
    }

    class Factory(private val apiKey: String, private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(apiKey,application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}