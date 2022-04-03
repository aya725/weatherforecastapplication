package com.example.weatherforecastapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecastapplication.model.RepositoryInterface
import com.example.weatherforecastapplication.network.Current
import com.example.weatherforecastapplication.network.WeatherResponse

class WeatherViewModel(private val repo:RepositoryInterface):ViewModel() {

    fun getWeather(latitude: Double,
                   longitude: Double,
                   exclude: String,
                   units:String,
                   apiKey: String):LiveData<WeatherResponse>{
        return repo.getWeatherList(latitude, longitude, exclude, units,apiKey)
    }

    fun getDasWeather(latitude: Double,
                      longitude: Double,
                      exclude: String,
                      units:String,
                      apiKey: String):LiveData<WeatherResponse>{
        return repo.getWeatherList(latitude, longitude, exclude, units,apiKey)
    }



}