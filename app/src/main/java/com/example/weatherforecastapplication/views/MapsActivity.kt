package com.example.weatherforecastapplication.views

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.ActivityMaps2Binding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback ,LocationListener,GoogleMap.OnCameraIdleListener,GoogleMap.OnCameraMoveStartedListener{
    var currentLocation:Location? = null
    var fusedLocationProviderClient:FusedLocationProviderClient?=null
    val REQUEST_CODE=101

    private lateinit var binding: ActivityMaps2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fetchLocation()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this ,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(this, arrayOf(
              android.Manifest.permission.ACCESS_FINE_LOCATION,
              android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),REQUEST_CODE)
            return
        }
        val task= fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map)as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this@MapsActivity)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
          val lating =LatLng(currentLocation!!.latitude,currentLocation!!.longitude)
       val markerOption = MarkerOptions().position(lating).title("I am Here !")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(lating))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lating,15f))
        googleMap.addMarker(markerOption)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            REQUEST_CODE ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   fetchLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onLocationChanged(location: Location) {
          val geocoder = Geocoder(this, Locale.getDefault())
        var address: List<Address>? = null
        try {
            address = geocoder.getFromLocation(location!!.latitude,location.longitude,1)
        }
        catch(e:IOException){
            e.printStackTrace()
        }
         // setAddress(address!![0])
    }

    override fun onCameraIdle() {

    }

    override fun onCameraMoveStarted(p0: Int) {
        TODO("Not yet implemented")
    }


}