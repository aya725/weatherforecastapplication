package com.example.weatherforecastapplication.db

import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.network.WeatherResponse

interface LocalSource {

    suspend fun insert (weatherResponse: WeatherResponse)
    fun getWeatherLocallyFromTable() : LiveData<WeatherResponse>
}