package com.example.weatherforecastapplication.view.fav

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.db.ConcreteLocalSource
import com.example.weatherforecastapplication.model.Repository
import com.example.weatherforecastapplication.network.WeatherClient
import com.example.weatherforecastapplication.viewmodel.WeatherViewModel
import com.example.weatherforecastapplication.viewmodel.WeatherViewModelFactory
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class searchActivity : AppCompatActivity() ,OnMapReadyCallback,LocationListener,
GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private var mMap: GoogleMap?=null
    internal  lateinit var mLastLocation : Location
    internal var mCurrentLocationMarker : Marker?=null
    internal var mGoogleApiClient: GoogleApiClient?=null
    internal lateinit var mLocationRequest: com.google.android.gms.location.LocationRequest
    lateinit var   locationSearch :EditText

    //addToDatabase
    lateinit var weatherFactory: WeatherViewModelFactory
    lateinit var weatherViewModel: WeatherViewModel
    lateinit var favPojo: FavPojo


    //shared
    //sharedPreferences
    lateinit var preferencesSearch : SharedPreferences
    lateinit var preferencesGps : SharedPreferences
  //  lateinit var preferencesLocation : SharedPreferences
    // lateinit var preferencesMap : SharedPreferences


    lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //  shared preferences
         preferencesSearch = this.getSharedPreferences("prefSearchLocation", Context.MODE_PRIVATE)
        editor = preferencesSearch.edit()

         preferencesGps = this.getSharedPreferences("prefGps", Context.MODE_PRIVATE)
        // preferencesLocation = this.getSharedPreferences("prefLocation", Context.MODE_PRIVATE);







        weatherFactory = WeatherViewModelFactory(
            Repository.getInstance(
                this, WeatherClient.getInstance()!!
                , ConcreteLocalSource(this)
            )!!
        )
        weatherViewModel = ViewModelProvider(this
            ,weatherFactory).get(WeatherViewModel::class.java)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
              if(ContextCompat.checkSelfPermission(
                      this,
                      android.Manifest.permission.ACCESS_FINE_LOCATION)==
                          PackageManager.PERMISSION_GRANTED
              ){
                  buildGoogleApiClient()
                  mMap!!.isMyLocationEnabled=true
              }
        }else{
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }

    }
    protected fun buildGoogleApiClient(){
        mGoogleApiClient =GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener (this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if(mCurrentLocationMarker != null){
            mCurrentLocationMarker!!.remove()
        }
        val latLng = LatLng(location.latitude,location.longitude)
        val markerOptions= MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrentLocationMarker = mMap!!.addMarker(markerOptions)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))
        if (mGoogleApiClient !=null){
            LocationServices.getFusedLocationProviderClient(this)

        }
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
       if (ContextCompat.checkSelfPermission(
               this ,android.Manifest.permission.ACCESS_FINE_LOCATION)==
                   PackageManager.PERMISSION_GRANTED

       ){
           LocationServices.getFusedLocationProviderClient(this)

       }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    fun searchLocation(view: View) {
         locationSearch  = findViewById(R.id.ediTextSearch)
        var location:String
        location = locationSearch.text.toString().trim()
        var addressList :List <Address>?= null
        if (location == null || location == ""){
            Toast.makeText(this,"provide location", Toast.LENGTH_SHORT).show()
        }
        else{
            val getCoder = Geocoder(this)
            try {
                addressList = getCoder.getFromLocationName(location,1)
            }catch (e :IOException){
                e.printStackTrace()
            }
            val address = addressList!![0]
            val latLng = LatLng(address.latitude,address.longitude)
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            //put data about location in database
            var lat = address.latitude
            var long = address.longitude
            var title = locationSearch.text.toString()

             favPojo = FavPojo(lat,long,title)
            weatherViewModel.insertFavToDatabase(favPojo)



        }
    }

    fun mapLocation(view: View) {
        locationSearch  = findViewById(R.id.ediTextSearch)
        var location:String
        location = locationSearch.text.toString().trim()
        var addressList :List <Address>?= null
        if (location == null || location == ""){
            Toast.makeText(this,"provide location", Toast.LENGTH_SHORT).show()
        }
        else{
            val getCoder = Geocoder(this)
            try {
                addressList = getCoder.getFromLocationName(location,1)
            }catch (e :IOException){
                e.printStackTrace()
            }
            val address = addressList!![0]
            val latLng = LatLng(address.latitude,address.longitude)
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            //put data about location in database
            var lat = address.latitude
            var long = address.longitude
            var title = locationSearch.text.toString()




            val gpsOn = preferencesGps.getInt("on",0)
            Log.e("gpsOf","as ${gpsOn}")


            if (gpsOn==2){
                editor.putInt("latSearch", lat.toInt())
                editor.putInt("longSearch",long.toInt())
                Log.e("lat","as ${lat.toInt()}")
                Log.e("long","as ${long.toInt()}")

                editor.putInt("mapSearchOn",1)
                editor.commit()
            }





        }

    }
}