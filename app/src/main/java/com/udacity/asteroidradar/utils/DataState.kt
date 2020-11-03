package com.udacity.asteroidradar.utils

sealed class DataState {
    object Loading : DataState()
    object Error : DataState()
    object Success : DataState()
}