package com.example.weatherforecastapplication.view.fav.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.network.Hourly
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(
    private val context : Context,
    private var hourlyList:List<Hourly>
) :RecyclerView.Adapter<HourlyAdapter.ViewHolder>(){

    lateinit var settings : SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item_hourly,parent,false)

         return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val hourTemp :TextView
        get() = itemView.findViewById(R.id.tempTemp)
        val tempHour :TextView
        get () = itemView.findViewById(R.id.tempHour)
        val image :ImageView
        get() = itemView.findViewById(R.id.hourImage)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
             Glide.with(context)
                 .load("http://openweathermap.org/img/wn/${hourlyList[position].weather[0].icon}@2x.png")
                 .apply(
                     RequestOptions()
                     .placeholder(R.drawable.ic_launcher_background)
                     .error(R.drawable.ic_launcher_background))
                 .into(holder.image)


         //get temp unit
        settings =  context.getSharedPreferences("prefSettings", Context.MODE_PRIVATE)
        val tempUnit = settings.getString("temp","def")

        if (tempUnit=="Celsius"){

            var  tempString= "${hourlyList[position].temp}°C"
            holder.hourTemp.text = tempString
        }
        else{
            var tempo = hourlyList[position].temp*9/5+32
            var  tempString= "${tempo}°F"
            holder.hourTemp.text = tempString
        }

        //Unix seconds
        val unix_seconds: Long = hourlyList[position].dt.toLong()
        //convert seconds to milliseconds
        val date = Date(unix_seconds * 1000L)
        // format of the date
        //val jdf = SimpleDateFormat("EEE yyyy-MM-dd HH:mm")
        val jdf = SimpleDateFormat("HH:mm")
        jdf.timeZone = TimeZone.getTimeZone("GMT+2")
        val java_date = jdf.format(date).trimIndent()
        holder.tempHour.text = java_date



    }
    fun setList(hourlyList: List<Hourly>){
        this.hourlyList= hourlyList
    }

    override fun getItemCount(): Int {
      return hourlyList.size
    }

     fun getTempUnit(temp :String){


     }

}