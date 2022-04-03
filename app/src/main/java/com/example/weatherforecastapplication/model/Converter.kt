package com.example.weatherforecastapplication.model

import androidx.room.TypeConverter
import com.example.weatherforecastapplication.network.Current
import com.example.weatherforecastapplication.network.Daily
import com.example.weatherforecastapplication.network.Hourly
import com.example.weatherforecastapplication.network.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    var gson = Gson()

    @TypeConverter
    fun hourToString(hourly: List<Hourly>):String{
        return gson.toJson(hourly)
    }
    @TypeConverter
    fun stringToHour(data:String):List<Hourly>{
        val hourListType = object  : TypeToken<List<Hourly>>(){
        }.type
        return gson.fromJson(data,hourListType)
    }

    @TypeConverter
    fun dailyToString(daily: List<Daily>):String{
        return gson.toJson(daily)
    }
    @TypeConverter
    fun stringToDay(data:String):List<Daily>{
        val dayListType = object  : TypeToken<List<Daily>>(){
        }.type
        return gson.fromJson(data,dayListType)
    }

    @TypeConverter
    fun currentToString(current: Current):String{
        return gson.toJson(current)
    }
    @TypeConverter
    fun stringToCurrent(data:String):Current{
        val currentType = object  : TypeToken<Current>(){
        }.type
        return gson.fromJson(data,currentType)
    }

    @TypeConverter
    fun weatherToString(weather: List<Weather>):String{
        return gson.toJson(weather)
    }
    @TypeConverter
    fun stringToWeatherList(data:String):List<Weather>{
        val weatherListType = object  : TypeToken<List<Weather>>(){
        }.type
        return gson.fromJson(data,weatherListType)
    }





}