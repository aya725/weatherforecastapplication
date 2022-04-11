package com.example.weatherforecastapplication.workManger

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory
import java.util.*
import java.util.concurrent.TimeUnit


class AlertsFragment : Fragment() {

    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel



    //sharedPreferences
    lateinit var preferencesAlert : SharedPreferences

    lateinit var map: SharedPreferences
    lateinit var  homePref :SharedPreferences




    lateinit var editor: SharedPreferences.Editor


    var DefaultDay = 0
    var DefaultMonth = 0
    var DefaultYear = 0
    var DefulHour = 0
    var DefultMinute =0


    var delayInSeconds =0L

    var checkedInDay = 0
    var checkedInMonth = 0
    var checkedInYear = 0
    var checkInHour = 0 ;
    var checkInMinute =0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0 ;
    var savedMinute =0


    var savedEndDay = 0
    var savedEndMonth = 0
    var savedEndYear = 0
    var savedEndHour = 0 ;
    var savedEndMinute =0



    var checkedOutDay = 0
    var checkedOutMonth = 0
    var checkedOutYear = 0
    var checkOutHour = 0
    var checkOutMinute =0




    lateinit var dateButton :Button
    lateinit var dateText:TextView
    lateinit var datTowButton :Button
    lateinit var dateTextTow:TextView
    lateinit var button: Button



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
       // dateButton = view.findViewById(R.id.dateAndTimePicker)
        dateText = view.findViewById(R.id.tv_textTime)
        datTowButton = view.findViewById(R.id.dateAndTimePickerTow)
       // dateTextTow = view.findViewById(R.id.tv_textTimeRow)
        //button = view.findViewById(R.id.set)


        //  shared preferences
        preferencesAlert = requireActivity().getSharedPreferences("prefAlerts", Context.MODE_PRIVATE)
        editor = preferencesAlert.edit()


        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                requireContext(), WeatherClient.getInstance()!!
                , ConcreteLocalSource(requireContext())
            )!!
        )
           weatherViewModel = ViewModelProvider(requireActivity(),weatherFactory).get(WeatherViewModel::class.java)

           pickDate()


    }

    private fun createWorkRequest(message: String, timeDelayInSeconds: Long ) {


                val myWorkRequest = OneTimeWorkRequestBuilder<MyWorkManger>()
                    .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
                    .setInputData(
                        workDataOf(
                            "title" to "null",
                            "message" to message,
                        )
                    )
                    .build()

                WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)

    }
    private fun pickDate() {
       /*   dateButton.setOnClickListener {
              getDateTimeCalendar()
              DatePickerDialog(requireContext(),checkInDatePicker,checkedInYear,checkedInMonth,checkedInDay).show()

          }*/
        datTowButton.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(requireContext(),checkOutDatePicker,checkedOutYear,checkedOutMonth,checkedOutDay).show()

        }
    }

    private fun getDateTimeCalendar(){
        val cal : Calendar = Calendar.getInstance()
        DefaultDay = cal.get(Calendar.DAY_OF_MONTH)
        DefaultMonth = cal.get(Calendar.MONTH)
        DefaultYear = cal.get(Calendar.YEAR)
        DefulHour = cal.get(Calendar.HOUR)
        DefultMinute = cal.get(Calendar.MINUTE)
    }




    val checkInDatePicker = object: DatePickerDialog.OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            checkedInDay = dayOfMonth
            checkedInMonth = month
            checkedInYear = year

            savedEndYear = year
            savedEndMonth = month
            savedEndDay = dayOfMonth
           TimePickerDialog(requireContext(),checkInTimePicker,checkedInDay,checkInMinute,false).show()

        }
    }
    val checkOutDatePicker = object: DatePickerDialog.OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            checkedOutDay = dayOfMonth
            checkedOutMonth = month
            checkedOutYear = year
            savedYear = year
            savedMonth = month
            savedDay = dayOfMonth
          TimePickerDialog(requireContext(),checkOutTimePicker,checkedOutDay,checkOutMinute,false).show()


        }
    }
    val checkInTimePicker = object: TimePickerDialog.OnTimeSetListener{
        override fun onTimeSet(p0: TimePicker?, hour: Int, min: Int) {
            checkInHour = hour
            checkInMinute =min
            savedEndHour = hour
            savedEndMinute = min
            dateTextTow.text = "$savedEndYear -$savedEndMonth-$savedEndDay : $savedEndHour : $savedEndMinute"

        }

    }
    val checkOutTimePicker = object: TimePickerDialog.OnTimeSetListener{
        override fun onTimeSet(p0: TimePicker?, hour: Int, min: Int) {
            checkOutHour = hour
            checkOutMinute =min
            savedHour = hour
            savedMinute = min
            dateText.text = "$savedYear -$savedMonth-$savedDay : $savedHour : $savedMinute"
            var userSelectedTime = Calendar.getInstance()

            map = requireActivity().getSharedPreferences("prefSearchLocation", Context.MODE_PRIVATE)
            homePref =  requireActivity().getSharedPreferences("homePrefs", Context.MODE_PRIVATE)


            //map
            val latSearch = map.getInt("latSearch",0)
            val latMap = latSearch.toDouble()

            val longSearch = map.getInt("longSearch",0)
            val longMap = longSearch.toDouble()


            userSelectedTime.set(savedYear,savedMonth,savedDay,savedHour,savedMinute)
            val todayDateTime = Calendar.getInstance()



            delayInSeconds = (userSelectedTime.timeInMillis/1000L) - (todayDateTime.timeInMillis/1000L)
           // editor.putLong("AlertInMill",delayInSeconds)
            //   editor.putString("null","null")


            //editor.commit()
            //createWorkRequest("aya", delayInSeconds)
                var alert = homePref.getInt("alert",0)
                if (alert ==1) {
                    createWorkRequest("there is no alert in this time", delayInSeconds)
                    Toast.makeText(requireContext(), "Reminder set", Toast.LENGTH_SHORT).show()

                       }
                else if (alert==2) {
                    createWorkRequest("opo", delayInSeconds)

          }
    }

    }



}