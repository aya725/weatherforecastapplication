package com.example.weatherforecastapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.Hourly
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.view.fav.adapters.HourlyAdapter
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory
import java.util.*


class HomeFragment : Fragment() {

    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel:WeatherViewModel
     lateinit var location : TextView

     lateinit var  sunrise : TextView
     lateinit var  wind: TextView
     lateinit var  pressure :TextView
     lateinit var  humidity:TextView
     lateinit var  sunset :TextView
     lateinit var desc:TextView
     lateinit var fellsLike :TextView
     lateinit var temp : TextView
     lateinit var image :ImageView
     lateinit var cloud :TextView

     //lateinit var tempString:String


     //shared
     //sharedPreferences
     lateinit var editor: SharedPreferences.Editor
     lateinit var preferences : SharedPreferences
     lateinit var settings :SharedPreferences

     //recyclerView
     lateinit var recyclerView: RecyclerView
     lateinit var layoutManager: LinearLayoutManager
     lateinit var hourlyAdapter: HourlyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location= view.findViewById(R.id.address)
        sunrise = view.findViewById(R.id.sunrise)
        wind = view.findViewById(R.id.wind)
        pressure = view.findViewById(R.id.pressure)
        humidity = view.findViewById(R.id.humidity)
        sunset = view.findViewById(R.id.sunset)
        desc = view.findViewById(R.id.desc)
        fellsLike = view.findViewById(R.id.fells_like)
        temp =  view.findViewById(R.id.tempc)
        image = view.findViewById(R.id.image)
        cloud = view.findViewById(R.id.cloud)
        //adapter
        recyclerView = view.findViewById(R.id.recyclerView)

        //shared
        preferences = requireActivity().getSharedPreferences("prefLocation", Context.MODE_PRIVATE);
        settings =  requireActivity().getSharedPreferences("prefSettings", Context.MODE_PRIVATE)

        initRecyclerView()

        val latitude = preferences.getInt("latitude",0)
        var lat  = latitude.toDouble()

        val longitude = preferences.getInt("longitude",0)
        var long  = longitude.toDouble()





        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance()!!
               , ConcreteLocalSource(requireContext())
            )!!
        )
        weatherViewModel = ViewModelProvider(requireActivity(),weatherFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getWeather(lat,long,"minutely","metric","58cd5baaa8ca729ae6a98af0267a2d16") .observe(requireActivity()){

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
                   updatedRecyclerView(weather.hourly)
                    updatedIcon("http://openweathermap.org/img/wn/${weather.current.weather[0].icon}@2x.png")

             weatherViewModel.addWeatherToDataBase(weather)

        }

    }

    private fun initRecyclerView() {

        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        hourlyAdapter= HourlyAdapter(requireContext(), ArrayList())
        recyclerView.adapter = hourlyAdapter
        recyclerView.layoutManager = layoutManager
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
    private fun updatedRecyclerView(hourlyList:List<Hourly>){
        hourlyAdapter.setList(hourlyList)
        hourlyAdapter.notifyDataSetChanged()
    }

}