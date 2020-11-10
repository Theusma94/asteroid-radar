package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.data.domain.Asteroid
import com.udacity.asteroidradar.data.asDomainModel
import com.udacity.asteroidradar.data.domain.PictureOfDay
import com.udacity.asteroidradar.data.local.getDatabase
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val apiKey: String, application: Application) : AndroidViewModel(application) {

    private var _resultAsteroids = MutableLiveData<List<Asteroid>>()
    val resultAsteroids: LiveData<List<Asteroid>> = _resultAsteroids

    private var _statusLoading = MutableLiveData<Boolean>()
    val statusLoading: LiveData<Boolean> = _statusLoading

    private var _urlPicOfDay = MutableLiveData<PictureOfDay>()
    val urlPicOfDay: LiveData<PictureOfDay> = _urlPicOfDay

    private val asteroidRepository: AsteroidRepository by lazy {
        val database = getDatabase(getApplication())
        AsteroidRepository(database)
    }
    init {
        getAllAsteroids()
        getPictureOfDay()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            asteroidRepository.picOfDay.map {
                it.asDomainModel()
            }.collect {
                if (it != null) {
                    _urlPicOfDay.postValue(it)
                } else {
                    startFetchPicOfDay()
                }
            }
        }
    }

    fun getWeekAsteroids() {
        viewModelScope.launch {
            asteroidRepository.asteroidsOfWeek.map {
                it.asDomainModel()
            }.collect {
                _resultAsteroids.value = it
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getAllAsteroids() {
        viewModelScope.launch {
            asteroidRepository.allAsteroids.map {
                it.asDomainModel()
            }.collect {
                if (it.isNotEmpty()) {
                    _resultAsteroids.postValue(it)
                } else {
                    startFetchAsteroids()
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

    private fun startFetchPicOfDay() {
        viewModelScope.launch {
            asteroidRepository.fetchPictureOfDay(apiKey)
        }
    }

    @ExperimentalCoroutinesApi
    private fun startFetchAsteroids() {
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
                return MainViewModel(apiKey, application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}