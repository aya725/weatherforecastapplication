package com.example.weatherforecastapplication.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weatherforecastapplication.network.WeatherResponse
import com.example.weatherforecastapplication.view.fav.FavPojo

class ConcreteLocalSource(
    val context: Context
):LocalSource {

    private lateinit var dao :WeatherDoa
    private lateinit var daoFav :FavDao
    init {
        val db :AppDatabase? = AppDatabase.getInstance(context)
        dao= db!!.weatherDao()
    }
    init {
        val dv :DatabaseFav? = DatabaseFav.getInstance(context)
        daoFav = dv!!.favDao()
    }



    override suspend fun insert(weatherResponse: WeatherResponse) {
        dao.insertWeatherLocally(weatherResponse)
    }

    override fun getWeatherLocallyFromTable(): LiveData<WeatherResponse> {
      return dao.getWeatherLocally()
    }

    override suspend fun insertFav(favPojo: FavPojo) {
        daoFav.insertFav(favPojo)
    }

    override fun getFvLocations(): LiveData<List<FavPojo>> {
        return  daoFav.getFavouritesLocally()
    }
}