package com.example.weatherforecastapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.model.RepositoryInterface
import java.lang.IllegalArgumentException

class WeatherViewModelFactory(private val repo:RepositoryInterface):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            WeatherViewModel(repo)as T
        }else
        {
            throw  IllegalArgumentException("ViewModel Class not found")
        }
    }


}