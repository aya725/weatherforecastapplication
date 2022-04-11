package com.example.weatherforecastapplication.view.fav

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.view.fav.adapters.FavAdapter
import com.example.weatherforecastapplication.view.fav.adapters.FavOnClickListner
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory
import java.util.ArrayList


class FavoriteFragment : Fragment() ,FavOnClickListner{
    lateinit var button: Button

    //viewmodel
    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel


    //recyclerView
    lateinit var recyclerViewFav: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var favAdapter: FavAdapter

    //shared
    lateinit var editor: SharedPreferences.Editor

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
        button = view.findViewById(R.id.buttonFav)


        //  shared preferences
        var preferences = requireActivity().getSharedPreferences("prefFav", Context.MODE_PRIVATE)
        editor = preferences.edit()



        //adapter
        recyclerViewFav = view.findViewById(R.id.favRecyclerView)



        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance()!!
                , ConcreteLocalSource(requireContext())
            )!!
        )

        weatherViewModel = ViewModelProvider(requireActivity(),weatherFactory).get(WeatherViewModel::class.java)

        button.setOnClickListener {
            val intent = Intent(requireContext(), searchActivity::class.java)
            startActivity(intent)
        }
                    weatherViewModel.getAllFavLocation().observe(requireActivity()){
                        fav->
                        favAdapter.setList(fav)
                        favAdapter.notifyDataSetChanged()
                        Log.e("favlist"," fav list${fav}")
                    }

        initRecyclerView()
    }
    private fun initRecyclerView() {

        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        favAdapter= FavAdapter(requireContext(), ArrayList(),this)
        recyclerViewFav.adapter = favAdapter
        recyclerViewFav.layoutManager = layoutManager
    }

    override fun onClick(favPojo: FavPojo) {

        editor.putInt("lat", favPojo.latitudeFv.toInt())
        editor.putInt("long", favPojo.longitudeFv.toInt())
        editor.commit()

        Log.e("lat"," lat${favPojo.latitudeFv}")
        Log.e("long"," long${favPojo.longitudeFv}")
        val intent = Intent(requireActivity(), FavDeatailsActivity::class.java)
        startActivity(intent)

    }

}