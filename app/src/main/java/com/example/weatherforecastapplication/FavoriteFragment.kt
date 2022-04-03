package com.example.weatherforecastapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory


class FavoriteFragment : Fragment() {
    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel:WeatherViewModel
    lateinit var response:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        response= view.findViewById(R.id.response)
        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance()!!
                , ConcreteLocalSource(requireContext())
            )!!
        )
        weatherViewModel = ViewModelProvider(requireActivity(),weatherFactory).get(WeatherViewModel::class.java)
        weatherViewModel.getWeather(31.2507303,29.9819893,"hourly,daily","metric","58cd5baaa8ca729ae6a98af0267a2d16").observe(requireActivity()){

                weather ->
            Log.i("tag", "onCreate: weather: $weather")

            if (weather!=null)
              response.text= weather.current.toString()
        }
    }

}