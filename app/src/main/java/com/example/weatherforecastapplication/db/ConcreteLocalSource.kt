package com.example.weatherforecastapplication.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.network.WeatherResponse

class ConcreteLocalSource(
    val context: Context
):LocalSource {

    private lateinit var dao :WeatherDoa
    init {
        val db :AppDatabase? = AppDatabase.getInstance(context)
        dao= db!!.weatherDao()
    }



    override suspend fun insert(weatherResponse: WeatherResponse) {
        dao.insertWeatherLocally(weatherResponse)
    }

    override fun getWeatherLocallyFromTable(): LiveData<WeatherResponse> {
      return dao.getWeatherLocally()
    }
}