package com.example.weatherforecastapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecastapplication.model.Converter
import com.example.weatherforecastapplication.network.WeatherResponse

@Database(entities = [WeatherResponse::class],version = 1)
@TypeConverters(Converter::class)

abstract class AppDatabase : RoomDatabase (){

    abstract fun weatherDao(): WeatherDoa

    companion object {
        private var instance : AppDatabase ? = null

        @Synchronized
        fun getInstance( context : Context) : AppDatabase?{
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,"weather"
                ).build()
            }
            return instance
        }
    }
}