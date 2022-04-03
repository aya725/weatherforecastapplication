package com.example.weatherforecastapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapplication.model.RepositoryInterface
import com.example.weatherforecastapplication.network.Current
import com.example.weatherforecastapplication.network.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun addWeatherToDataBase(weatherResponse: WeatherResponse){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWeather(weatherResponse)
        }
    }
    fun getWeatherLocally ():LiveData<WeatherResponse>{
         return repo.getStoredWeather()
    }


}