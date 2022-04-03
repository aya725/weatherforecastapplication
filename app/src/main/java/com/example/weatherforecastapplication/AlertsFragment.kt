package com.example.weatherforecastapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide


class AlertsFragment : Fragment() {
     lateinit var radioGroup: RadioGroup
     lateinit var maleRadioButton: RadioButton
     lateinit var femaleRadioButton: RadioButton
     lateinit var resultTxt :TextView
     lateinit var resultBtn :TextView
     lateinit var editor:SharedPreferences.Editor
     lateinit var imaa:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioGroup = view.findViewById(R.id.rgGender)
        maleRadioButton = view.findViewById(R.id.male)
        femaleRadioButton=view.findViewById(R.id.female)
        resultTxt = view.findViewById(R.id.result)
        resultBtn = view.findViewById(R.id.btn)
        imaa = view.findViewById(R.id.imaa)

        var preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = preferences.edit()

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.male){
                maleRadioButton.isChecked
                editor.putString("gender","male")
                Toast.makeText(requireContext(),"this data saved",Toast.LENGTH_LONG).show()
            }else if (checkedId == R.id.female){
                femaleRadioButton.isChecked
                editor.putString("gender","female")
                Toast.makeText(requireContext(),"this data saved",Toast.LENGTH_LONG).show()

            }
            //editor.apply()
            editor.commit()

        }
        resultBtn.setOnClickListener {
            val gender = preferences.getString("gender","def")
            resultTxt.text = gender.toString()
        }
        Glide.with(this).load("http://openweathermap.org/img/wn/10d@2x.png").into(imaa)



    }


}