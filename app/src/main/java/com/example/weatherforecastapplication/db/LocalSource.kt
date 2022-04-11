package com.example.weatherforecastapplication.db

import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.network.WeatherResponse
import com.example.weatherforecastapplication.view.fav.FavPojo

interface LocalSource {

    suspend fun insert (weatherResponse: WeatherResponse)
    fun getWeatherLocallyFromTable() : LiveData<WeatherResponse>

    suspend fun insertFav (favPojo: FavPojo)
    fun getFvLocations() :LiveData<List<FavPojo>>

}