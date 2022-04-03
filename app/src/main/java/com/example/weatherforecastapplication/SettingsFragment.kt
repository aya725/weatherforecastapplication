package com.example.weatherforecastapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.example.weatherforecastapplication.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class SettingsFragment : Fragment() {
    //location
    private lateinit var radioGroup: RadioGroup
    private lateinit var gpsButton :RadioButton

    //temperature
    private lateinit var tempGroup : RadioGroup
    private lateinit var celsiusButton : RadioButton
    private lateinit var kelvinButton :RadioButton

    //windSpeed
    private lateinit var windSpeedGroup: RadioGroup
    private lateinit var meterButton: RadioButton
    private lateinit var mileButton: RadioButton

    //shared
    lateinit var editor: SharedPreferences.Editor

   lateinit var text:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_settings2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view.findViewById(R.id.text_fet)
        //location
        radioGroup = view.findViewById(R.id.radioLocation)
        gpsButton = view.findViewById(R.id.gps)

        //temperature
        tempGroup = view.findViewById(R.id.tempGroup)
        celsiusButton = view.findViewById(R.id.celsius)
        kelvinButton = view .findViewById(R.id.kelvin)

        //windSpeed

        windSpeedGroup = view.findViewById(R.id.windGroup)
        meterButton = view.findViewById(R.id.meter)
        mileButton = view.findViewById(R.id.mile)


          //  shared preferences
        var preferences = requireActivity().getSharedPreferences("prefSettings", Context.MODE_PRIVATE)
        editor = preferences.edit()




        //location
        gpsButton.setOnClickListener{
            val intent = Intent(requireContext(),MapsActivity::class.java)
            startActivity(intent)
            /*val gender = preferences.getString("temp","def")
            text.text = gender.toString()*/


        }

        //temperature preferences

        tempGroup.setOnCheckedChangeListener{group,checkedId->
            if (checkedId == R.id.celsius){
                editor.putString("temp","Celsius")
            }else if (checkedId == R.id.kelvin){
                editor.putString("temp","kelvin")

            }
            editor.commit()
        }

        //windSpeed preferences

        windSpeedGroup.setOnCheckedChangeListener{ group,checkedId->
            if (checkedId == R.id.meter){
                 editor.putString("windSpeed","meter")
                Toast.makeText(requireContext(),"this data saved",Toast.LENGTH_LONG).show()

            } else if (checkedId == R.id.mile){
                editor.putString("windSpeed","mile")
                Toast.makeText(requireContext(),"this data saved",Toast.LENGTH_LONG).show()

            }

            editor.commit()

        }


    }

}

