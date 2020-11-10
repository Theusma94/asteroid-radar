package com.udacity.asteroidradar.utils

import android.annotation.SuppressLint
import com.udacity.asteroidradar.Constants
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("WeekBasedYear")
object DateHelper {

    fun getStartDateFormatted(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getEndDateFormatted(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getEndOfWeekFormatted(): String {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val leftDays = Calendar.SATURDAY - currentDay
        calendar.add(Calendar.DAY_OF_WEEK, leftDays)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)

    }
}