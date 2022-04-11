package com.example.weatherforecastapplication.network

import androidx.room.*
import com.example.weatherforecastapplication.model.Converter


const val LAST_WEATHER_ID = 0

    @Entity(tableName = "weather")
data class WeatherResponse(
    @TypeConverters(Converter::class)
    @ColumnInfo (name = "current")
    val current: Current,

    @TypeConverters(Converter::class)
    @ColumnInfo (name = "daily")
    val daily: List<Daily>,

    @TypeConverters(Converter::class)
    @ColumnInfo (name = "hourly")
    val hourly: List<Hourly>,

    @ColumnInfo (name = "latitude")
    val lat: Double,

    @ColumnInfo (name = "longitude")
    val lon: Double,

    @ColumnInfo (name = "timezone")
    val timezone: String,

    @ColumnInfo (name = "timezone_offset")
    val timezone_offset: Int,

    @ColumnInfo (name = "alerts")

    val alerts :List<Alerts>? = null,

   // @ColumnInfo (name = "endDate")
    //val endDate :Long

){
    @PrimaryKey(autoGenerate = false)
    var id :Int = LAST_WEATHER_ID
}