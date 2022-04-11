package com.example.weatherforecastapplication.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.view.fav.searchActivity


class SettingsFragment : Fragment() {
    //location
    private lateinit var radioGroup: RadioGroup
    private lateinit var gpsButton :RadioButton
    private lateinit var mapButton :RadioButton

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
    lateinit var editorGps: SharedPreferences.Editor

    lateinit var preferencesLocation : SharedPreferences
    lateinit var preferencesMap : SharedPreferences
    lateinit var editorMap: SharedPreferences.Editor







    lateinit var text:TextView
   lateinit var imageButton: ImageButton


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
        imageButton = view.findViewById(R.id.ButtonFav)



        //location
        radioGroup = view.findViewById(R.id.radioLocation)
        gpsButton  = view.findViewById(R.id.gps)
        mapButton  = view.findViewById(R.id.map)

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

        var preferencesGps = requireActivity().getSharedPreferences("prefGps", Context.MODE_PRIVATE)
        editorGps = preferencesGps.edit()




        //location
        radioGroup.setOnCheckedChangeListener{
            group,checkedId->
            if (checkedId == R.id.gps){
                Toast.makeText(requireContext(),"gps",Toast.LENGTH_SHORT).show()


                editorGps.putInt("on",1)

            }
            else if (checkedId ==R.id.map){
                editorGps.putInt("on",2)
                val intent = Intent(requireContext(), searchActivity::class.java)
                startActivity(intent)
            }
            editorGps.commit()
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

