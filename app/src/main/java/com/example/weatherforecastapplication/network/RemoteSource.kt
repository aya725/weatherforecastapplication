package com.example.weatherforecastapplication.network

import androidx.lifecycle.LiveData

interface RemoteSource {
    fun getWeather(latitude:Double,longitude:Double,exclude:String,units:String,apiKey:String): LiveData<WeatherResponse>

}