package com.example.weatherforecastapplication.model

import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.network.WeatherResponse
import com.example.weatherforecastapplication.view.fav.FavPojo

interface RepositoryInterface {
    fun getWeatherList(latitude:Double,longitude:Double,exclude:String,units:String,apiKey:String): LiveData<WeatherResponse>
    fun getStoredWeather():LiveData<WeatherResponse>
    suspend fun insertWeather(weatherResponse: WeatherResponse)

    suspend fun insertFav (favPojo: FavPojo)
    fun getFvLocations() :LiveData<List<FavPojo>>
}