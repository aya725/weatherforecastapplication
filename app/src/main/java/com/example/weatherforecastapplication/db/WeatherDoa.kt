package com.example.weatherforecastapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecastapplication.network.LAST_WEATHER_ID
import com.example.weatherforecastapplication.network.WeatherResponse

@Dao
interface WeatherDoa {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
   fun insertWeatherLocally (weatherResponse: WeatherResponse)

    @Query(value = "select * from weather where id = $LAST_WEATHER_ID")
   fun getWeatherLocally():LiveData<WeatherResponse>
}