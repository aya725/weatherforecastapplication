package com.example.weatherforecastapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.view.DailyAdapter
import com.example.weatherforecastapplication.view.HourlyAdapter
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory
import java.util.ArrayList


class DayFragment : Fragment() {
    //viewmodel
    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel

    //sharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var preferences : SharedPreferences

    //recyclerView
    lateinit var recyclerViewDay: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var dailyAdapter: DailyAdapter

    lateinit var tes:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_day, container, false)
       // initRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tes = view.findViewById(R.id.tess)
        tes.text = "ala;"


        //adapter
        recyclerViewDay = view.findViewById(R.id.dayRecyclerView)



        //shared
        preferences = requireActivity().getSharedPreferences("prefLocation", Context.MODE_PRIVATE);

         //lat and long from preferences
        val latitude = preferences.getInt("latitude",0)
        var lat  = latitude.toDouble()

        val longitude = preferences.getInt("longitude",0)
        var long  = longitude.toDouble()

        initRecyclerView()

        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance()!!
                , ConcreteLocalSource(requireContext())
            )!!
        )
        weatherViewModel = ViewModelProvider(requireActivity(),weatherFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getDasWeather(lat,long,"minutely","metric","58cd5baaa8ca729ae6a98af0267a2d16").observe(requireActivity()){

                weatherTow ->
            Log.i("tag", "onCreate: weather: $weatherTow ")

            if (weatherTow !=null)

            Log.e("daily","daily ${weatherTow .daily[0]}")
            //daily weather
            dailyAdapter.setList(weatherTow.daily)
            dailyAdapter.notifyDataSetChanged()

        }/*
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        dailyAdapter= DailyAdapter(requireContext(), ArrayList())
        recyclerViewDay.adapter = dailyAdapter
        recyclerViewDay.layoutManager = layoutManager*/


    }

    private fun initRecyclerView() {

        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        dailyAdapter= DailyAdapter(requireContext(), ArrayList())
        recyclerViewDay.adapter = dailyAdapter
        recyclerViewDay.layoutManager = layoutManager
    }


}