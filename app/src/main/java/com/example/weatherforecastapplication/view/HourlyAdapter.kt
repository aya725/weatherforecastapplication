package com.example.weatherforecastapplication.view

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
        val hour :TextView
        get() = itemView.findViewById(R.id.hour)
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
            holder.tempHour.text = tempString
        }
        else{
            var tempo = hourlyList[position].temp*9/5+32
            var  tempString= "${tempo}°F"
            holder.tempHour.text = tempString
        }







        var seconds =  (hourlyList[position].dt/ 1000) % 60 ;
        var minutes =  ((hourlyList[position].dt / (1000*60)) % 60);
        var hours   =  ((hourlyList[position].dt / (1000*60*60)) % 24);
        holder.hour.text = "${hours}:${minutes}:${seconds}"

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