package com.zeinab.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zeinab.weatherapp.databinding.ActivityGoogleMapsBinding


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    var currentLocation: Location?=null
    var fusedLocationProviderClient: FusedLocationProviderClient?=null
    val REQUEST_CODE=100
   // private lateinit var mMap: GoogleMap
   // private lateinit var binding: ActivityGoogleMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding = ActivityGoogleMapsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_google_maps)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        getMyLocation()

    }

    private fun getMyLocation() {
     if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
     !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
     !=PackageManager.PERMISSION_GRANTED){
         ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
            return
     }
        val task=fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if(location!=null){
                currentLocation=location
                val supportMapFragment=(supportFragmentManager.findFragmentById(R.id.map)as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this)

            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val latLng=LatLng(currentLocation!!.latitude,currentLocation!!.longitude)
        val markerOptions=MarkerOptions().position(latLng).title("my loc")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        googleMap.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE->{
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getMyLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}