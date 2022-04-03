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
import com.example.weatherforecastapplication.network.Daily
import com.example.weatherforecastapplication.network.Hourly

class DailyAdapter (
    private val context : Context,
    private var dailyList:List<Daily>
        ): RecyclerView.Adapter<DailyAdapter.ViewHolder>(){

    lateinit var settings : SharedPreferences


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_item,parent,false)
        return ViewHolder(view)
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val dayImage :ImageView
        get() = itemView.findViewById(R.id.dayImage)
        val tempDaily:TextView
        get() = itemView.findViewById(R.id.tempDay)
        val day : TextView
        get() = itemView.findViewById(R.id.day)
        val sunDay :TextView
        get() = itemView.findViewById(R.id.dailySun)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load("http://openweathermap.org/img/wn/${dailyList[position].weather[0].icon}@2x.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background))
            .into(holder.dayImage)

        holder.day.text = dailyList[position].dt.toString()
        holder.sunDay.text = dailyList[position].sunrise.toString()



        //sunrise
        var seconds =  (dailyList[position].sunrise/ 1000) % 60 ;
        var minutes =  ((dailyList[position].sunrise / (1000*60)) % 60);
        var hours   =  ((dailyList[position].sunrise / (1000*60*60)) % 24);
        holder.sunDay.text = "${hours}:${minutes}:${seconds}"



        //get temp unit
        settings =  context.getSharedPreferences("prefSettings", Context.MODE_PRIVATE)
        val tempUnit = settings.getString("temp","def")

        if (tempUnit=="Celsius"){

            var  tempString= "${dailyList[position].temp.day}°C"
            holder.tempDaily.text = tempString
        }
        else{
            var tempo = dailyList[position].temp.day*9/5+32
            var  tempString= "${tempo}°F"
            holder.tempDaily.text = tempString
        }
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }

    fun setList(dailyList: List<Daily>){
        this.dailyList= dailyList
    }

}