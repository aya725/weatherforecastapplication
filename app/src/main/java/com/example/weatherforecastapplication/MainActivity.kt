package com.example.weatherforecastapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.weatherforecastapplication.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.reflect.Array.newInstance

class MainActivity : AppCompatActivity() {

    //nav drawer
    private lateinit var binding: ActivityMainBinding
    lateinit var  toggle : ActionBarDrawerToggle
    private lateinit var navController: NavController

    //location
    private  lateinit var fusedLocationClient: FusedLocationProviderClient

    //sharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var preferences :SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //init fragment

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,HomeFragment())
        fragmentTransaction.commit()


        // nav drawer init

        toggle =ActionBarDrawerToggle(this,binding.drawer,R.string.open,R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment(),it.title.toString())

                R.id.fav -> replaceFragment(FavoriteFragment(),it.title.toString())

                R.id.alert -> replaceFragment(AlertsFragment(),it.title.toString())

                R.id.settings -> replaceFragment(SettingsFragment(),it.title.toString())

                R.id.day -> replaceFragment(DayFragment(),it.title.toString())

                R.id.week -> replaceFragment(SevenDaysFragment(),it.title.toString())

                R.id.Month -> replaceFragment(MonthFragment(),it.title.toString())

            }
            true
        }


        //shared preferences init
        preferences = this.getSharedPreferences("prefLocation", Context.MODE_PRIVATE);
        editor = preferences.edit()




        //location init
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()



    }
    // nav drawer
    private fun replaceFragment(fragment:Fragment,title : String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        binding.drawer.closeDrawers()
        setTitle(title)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // location
    private fun getCurrentLocation() {

        if (checkPermissions()){

            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location: Location? = task.result
                    if (location==null){
                        Toast.makeText(this,"Null",Toast.LENGTH_LONG).show()

                    }
                    else
                    {

                        Toast.makeText(this,"Get Success " + location.latitude +""+location.longitude,Toast.LENGTH_LONG).show()

                        //shared
                        editor.putInt("latitude", location.latitude.toInt())
                        editor.putInt("longitude",location.longitude.toInt())
                        editor.commit()

                    }

                }

            }
            else{

                //settings open here
                Toast.makeText(this,"Turn on your location",Toast.LENGTH_LONG).show()
                val intent = Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else
        {
            //request permission

            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object{
        private const val  PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)

    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )== PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)==
            PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Granted",Toast.LENGTH_LONG).show()
                getCurrentLocation()

            }
            else {
                Toast.makeText(applicationContext,"denied",Toast.LENGTH_LONG).show()
            }
        }
    }
}