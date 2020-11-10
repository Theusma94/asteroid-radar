package com.udacity.asteroidradar.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.data.domain.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface AsteroidService {
    @GET
    fun getAsteroids(@Url url: String): Deferred<String>

    @GET
    fun getImageOfDay(@Url url: String): Deferred<PictureOfDay>
}

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private fun client(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
}

object Network {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val asteroids = retrofit.create(AsteroidService::class.java)
}