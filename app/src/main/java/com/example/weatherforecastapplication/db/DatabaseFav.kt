package com.example.weatherforecastapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecastapplication.view.fav.FavPojo


@Database(entities = [FavPojo::class],version = 1)
 abstract class DatabaseFav : RoomDatabase() {

    abstract fun favDao ():FavDao

    companion object {
        private var instance : DatabaseFav ? = null

        @Synchronized
        fun getInstance( context : Context) : DatabaseFav?{
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseFav::class.java,"favourite"
                ).build()
            }
            return instance
        }
    }

}