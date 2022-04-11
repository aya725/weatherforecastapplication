package com.example.weatherforecastapplication.views

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
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.Hourly
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.view.fav.adapters.HourlyAdapter
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory
import java.text.SimpleDateFormat
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
     lateinit var dayHome :TextView
     lateinit var temp : TextView
     lateinit var image :ImageView
     lateinit var cloud :TextView

     //lateinit var tempString:String

    var delayInSeconds =0L
    var message = ""


     //shared
     //sharedPreferences
     lateinit var preferences : SharedPreferences
    lateinit var preferencesAlert : SharedPreferences
     lateinit var settings :SharedPreferences
     lateinit var map: SharedPreferences
     lateinit var gpsSettings :SharedPreferences
    lateinit var  homePref :SharedPreferences
    lateinit var editor: SharedPreferences.Editor



    //recyclerView
     lateinit var recyclerView: RecyclerView
     lateinit var layoutManager: LinearLayoutManager
     lateinit var hourlyAdapter: HourlyAdapter


     var latMap = 0.0
    var longMap =0.0


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
        dayHome = view.findViewById(R.id.dayHome)
        temp =  view.findViewById(R.id.tempc)
        image = view.findViewById(R.id.image)
        cloud = view.findViewById(R.id.cloud)
        //adapter
        recyclerView = view.findViewById(R.id.recyclerView)

        //shared
        preferences = requireActivity().getSharedPreferences("prefLocation", Context.MODE_PRIVATE);
        settings =  requireActivity().getSharedPreferences("prefSettings", Context.MODE_PRIVATE)
        map = requireActivity().getSharedPreferences("prefSearchLocation", Context.MODE_PRIVATE)
        gpsSettings = requireActivity().getSharedPreferences("prefGps", Context.MODE_PRIVATE)
        preferencesAlert = requireActivity().getSharedPreferences("prefAlerts", Context.MODE_PRIVATE)
        homePref =  requireActivity().getSharedPreferences("homePrefs", Context.MODE_PRIVATE)
        editor = homePref.edit()



        initRecyclerView()
        //map
         val latSearch = map.getInt("latSearch",0)
           latMap = latSearch.toDouble()

        val longSearch = map.getInt("longSearch",0)
           longMap = longSearch.toDouble()

        val mapOn = map.getInt("mapSearchOn",0)


            //gps
        val latitude = preferences.getInt("latitude",0)
        val lat  = latitude.toDouble()

        val longitude = preferences.getInt("longitude",0)
        val long  = longitude.toDouble()

        //gps settings
        val gpsOn = gpsSettings.getInt("on",0)






        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance()!!
               , ConcreteLocalSource(requireContext())
            )!!
        )


        weatherViewModel = ViewModelProvider(requireActivity(),weatherFactory).get(WeatherViewModel::class.java)

         if (gpsOn ==1) {


            weatherViewModel.getWeather(
                lat,
                long,
                "minutely",
                "metric",
                "58cd5baaa8ca729ae6a98af0267a2d16"
            ).observe(requireActivity()) {

                    weather ->

                if (weather != null)

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
                    updateDateToday(weather.current.dt)

                weatherViewModel.addWeatherToDataBase(weather)

            }
        }
       else if(gpsOn==2){
             mapFun()

        }else{
             weatherViewModel.getWeather(
                 lat,
                 long,
                 "minutely",
                 "metric",
                 "58cd5baaa8ca729ae6a98af0267a2d16"
             ).observe(requireActivity()) {

                     weather ->

                 if (weather != null)
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
                     updateDateToday(weather.current.dt)


                 weatherViewModel.addWeatherToDataBase(weather)

             }
        }

             //get weather from database
        /* weatherViewModel.getWeatherLocally().observe(requireActivity()){
               weather ->
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
               updateDateToday(weather.current.dt)

           //  updatedIcon("http://openweathermap.org/img/wn/${weather.current.weather[0].icon}@2x.png")


           }*/

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
    private fun updateDateToday(dt:Int){
        //Unix seconds
        val unix_seconds: Long = dt.toLong()
        //convert seconds to milliseconds
        val date = Date(unix_seconds * 1000L)
        // format of the date
        val jdf = SimpleDateFormat("EEE yyyy-MM-dd HH:mm")
        // val jdf = SimpleDateFormat("HH:mm")
        jdf.timeZone = TimeZone.getTimeZone("GMT+2")
        val java_date = jdf.format(date).trimIndent()
        dayHome.text = java_date

    }

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
    Glide.with(this).load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background))
        .into(image)


    }
    private fun updatedHumidity(humidityPar:String){
        humidity.text = humidityPar
    }
    private fun updatedRecyclerView(hourlyList:List<Hourly>){
        hourlyAdapter.setList(hourlyList)
        hourlyAdapter.notifyDataSetChanged()
    }






        private fun mapFun( ):Int {
            var alert = 0

            weatherFactory = WeatherViewModelFactory(
                Repository.getInstance(
                     requireContext(), WeatherClient.getInstance()!!
                    , ConcreteLocalSource(requireContext())
                )!!
            )

            weatherViewModel.getWeather(latMap,longMap,"minutely","metric","58cd5baaa8ca729ae6a98af0267a2d16") .observe(requireActivity()){

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
                updateDateToday(weather.current.dt)

                if (weather.alerts?.get(0)==null) {
                    //createWorkRequest("there is no alert in this time", delayInSeconds)
                    //Toast.makeText(requireContext(), "Reminder set", Toast.LENGTH_SHORT).show()
                    editor.putInt("alert",1)
                    alert =1
                }
                else {
                    //createWorkRequest("opo", delayInSeconds)
                    editor.putInt("alert",2)
                    alert = 2
                }
                editor.commit()
                weatherViewModel.addWeatherToDataBase(weather)




            }
            return alert
        }



}