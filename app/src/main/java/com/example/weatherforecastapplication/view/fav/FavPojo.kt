package com.example.weatherforecastapplication.view.fav

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "favourite")
data class FavPojo (
    @ColumnInfo(name = "latitude")
    var latitudeFv  : Double,

    @ColumnInfo(name = "longitude")
    var longitudeFv : Double,

    @ColumnInfo(name = "location")
    var locationName :String

        )
{
    @PrimaryKey (autoGenerate = true)
    var id : Int = 0
}