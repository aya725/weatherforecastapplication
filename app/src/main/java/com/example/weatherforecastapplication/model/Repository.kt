package com.example.weatherforecastapplication.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.network.Current
import com.example.weatherforecastapplication.network.RemoteSource
import com.example.weatherforecastapplication.network.WeatherResponse

class Repository (
    val context: Context,
    val remoteSource: RemoteSource
)
    :RepositoryInterface{

    companion object {

        private var repository: Repository? = null

        @Synchronized
        fun getInstance(
            context: Context,
            remoteSource: RemoteSource,
        ): Repository? {

            if (repository == null) {
                repository = Repository(context, remoteSource)
            }
            return repository
        }
    }

    override fun getWeatherList(
        latitude: Double,
        longitude: Double,
        exclude: String,
        units: String,
        apiKey: String
    ): LiveData<WeatherResponse> {
        return remoteSource.getWeather(latitude,longitude,exclude,units, apiKey)

    }


}
