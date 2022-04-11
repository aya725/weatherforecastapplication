package com.example.weatherforecastapplication.view.fav

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory

class FavDeatailsActivity : AppCompatActivity() {

    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var location : TextView

    lateinit var  sunrise : TextView
    lateinit var  wind: TextView
    lateinit var  pressure : TextView
    lateinit var  humidity: TextView
    lateinit var  sunset : TextView
    lateinit var desc: TextView
    lateinit var fellsLike : TextView
    lateinit var temp : TextView
    lateinit var image : ImageView
    lateinit var cloud : TextView


    //shared
    //sharedPreferences
    lateinit var preferences : SharedPreferences
    lateinit var settings : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_deatails)


        location= findViewById(R.id.addressFav)
        sunrise = findViewById(R.id.sunriseFav)
        wind = findViewById(R.id.windFav)
        pressure = findViewById(R.id.pressureFav)
        humidity = findViewById(R.id.humidityFav)
        sunset = findViewById(R.id.sunsetFav)
        desc = findViewById(R.id.descFav)
        fellsLike = findViewById(R.id.fells_like_Fav)
        temp = findViewById(R.id.tempcFav)
        image = findViewById(R.id.imageFav)
        cloud = findViewById(R.id.cloudFav)

        preferences = this.getSharedPreferences("prefFav", Context.MODE_PRIVATE)
        settings =  this.getSharedPreferences("prefSettings", Context.MODE_PRIVATE)

        val latitude = preferences.getInt("lat",0)
        var lat  = latitude.toDouble()

        val longitude = preferences.getInt("long",0)
        var long  = longitude.toDouble()

        Log.e("lat"," lat${lat}")
        Log.e("long"," long${long}")

        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                this, WeatherClient.getInstance()!!
                , ConcreteLocalSource(this)
            )!!
        )
        weatherViewModel = ViewModelProvider(this,weatherFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getWeather(lat,long,"minutely","metric","58cd5baaa8ca729ae6a98af0267a2d16") .observe(this){

                weather ->

            if (weather!=null)

                updatedLocation(weather.timezone)
            updatedStatues(weather.current.weather[0].description)
            updatedTemperatures(weather.current.temp)
            updateSunRise(weather.current.sunrise)
            updatedSunset(weather.current.sunset)
            updateWindSpeed(weather.current.wind_speed)
            updatedPressure(weather.current.pressure)
            updatedHumidity(weather.current.humidity.toString())
            updatedClouds(weather.current.clouds.toString())
            // updatedIcon("http://openweathermap.org/img/wn/${weather.current.weather[0].icon}@2x.png")

            //weatherViewModel.addWeatherToDataBase(weather)

        }
    }

    private fun updatedLocation (locationPar :String){
        location.text = locationPar
    }
    private fun updateDateToday(){}

    private fun updatedTemperatures(temperature:Double){
        // °C °F
        val tempUnit = settings.getString("temp","def")

        if (tempUnit=="Celsius"){

            var  tempString= "${temperature}°C"
            temp.text = tempString
        }
        else{
            var tempo = temperature*9/5+32
            var  tempString= "${tempo}°F"
            temp.text = tempString
        }
    }
    private  fun updatedStatues(statues: String){
        desc.text = statues
    }
    private fun updateWindSpeed (WindSpeed:Double){
        val windUnit = settings.getString("windSpeed","def")
        if (windUnit == "meter"){

            var windString = "${WindSpeed}m"
            wind.text = windString

        }else{
            var mil= WindSpeed/1000
            var windString = "${mil}mm"
            wind.text = windString
        }

    }
    private fun updateSunRise(sunrisee:Int){
        var seconds =  (sunrisee/ 1000) % 60 ;
        var minutes =  ((sunrisee / (1000*60)) % 60);
        var hours   =  ((sunrisee / (1000*60*60)) % 24);
        sunrise.text = "${hours}:${minutes}:${seconds}"
    }
    private fun updatedSunset(sunsett:Int){
        var seconds =  (sunsett/ 1000) % 60 ;
        var minutes =  ((sunsett / (1000*60)) % 60);
        var hours   =  ((sunsett / (1000*60*60)) % 24);
        sunset.text = "${hours}:${minutes}:${seconds}"
    }
    private fun updatedPressure(pressurePar:Int){
        pressure.text="${pressurePar} ft"

    }
    private fun updatedClouds(clouds:String){
        cloud.text = "${clouds}%"
    }
    private fun updatedIcon (url:String){
        Glide.with(this).load(url).into(image)

    }
    private fun updatedHumidity(humidityPar:String){
        humidity.text = humidityPar
    }

}