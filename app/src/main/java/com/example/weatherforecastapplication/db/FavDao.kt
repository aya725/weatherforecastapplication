package com.example.weatherforecastapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.example.weatherforecastapplication.view.fav.FavPojo

@Dao
interface FavDao {

    @Insert
    fun insertFav (favPojo: FavPojo)

    @Query( "select * from favourite")
    fun getFavouritesLocally(): LiveData<List<FavPojo>>
}