package com.example.weatherforecastapplication.model

import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.network.Current
import com.example.weatherforecastapplication.network.WeatherResponse

interface RepositoryInterface {
    fun getWeatherList(latitude:Double,longitude:Double,exclude:String,units:String,apiKey:String): LiveData<WeatherResponse>
    fun getStoredWeather():LiveData<WeatherResponse>
    suspend fun insertWeather(weatherResponse: WeatherResponse)
}