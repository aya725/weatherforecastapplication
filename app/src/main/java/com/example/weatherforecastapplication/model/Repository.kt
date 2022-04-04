package com.example.weatherforecastapplication.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.db.LocalSource
import com.example.weatherforecastapplication.network.Current
import com.example.weatherforecastapplication.network.RemoteSource
import com.example.weatherforecastapplication.network.WeatherResponse
import com.example.weatherforecastapplication.view.fav.adapters.FavPojo

class Repository (
    val context: Context,
    val remoteSource: RemoteSource,
    val localSource: LocalSource

)
    :RepositoryInterface{

    companion object {

        private var repository: Repository? = null

        @Synchronized
        fun getInstance(
            context: Context,
            remoteSource: RemoteSource,
            localSource: LocalSource
        ): Repository? {

            if (repository == null) {
                repository = Repository(context, remoteSource,localSource)
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

    override fun getStoredWeather(): LiveData<WeatherResponse> {
        return localSource.getWeatherLocallyFromTable()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
       localSource.insert(weatherResponse)
    }

    override suspend fun insertFav(favPojo: FavPojo) {
        localSource.insertFav(favPojo)
    }

    override fun getFvLocations(): LiveData<List<FavPojo>> {
        return localSource.getFvLocations()
    }


}
