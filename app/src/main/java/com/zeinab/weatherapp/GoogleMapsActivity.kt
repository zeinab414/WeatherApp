package com.zeinab.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import java.util.*


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    var mMap: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation : Location?= null
    var currentMarker: Marker? = null
    var lat :Double? = null
    var lon : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener {location ->
            if(location != null){
                this.currentLocation = location
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng = LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        drawMarker(latLng)
        mMap?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {}
            override fun onMarkerDragEnd(p0: Marker) {
                if(currentMarker != null){
                    currentMarker?.remove()
                }
                val newLatLng = LatLng(p0?.position!!.latitude,p0?.position.longitude)
                drawMarker(newLatLng)            }
            override fun onMarkerDragStart(p0: Marker) {}

        })

    }

    private fun drawMarker(latLng: LatLng){
        val markerOptions=  MarkerOptions().position(latLng).title("Your Location")
            .snippet(getAddress(latLng.latitude, latLng.longitude)).draggable(true)
        mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
        currentMarker=  mMap?.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
        lat = latLng.latitude
        lon = latLng.longitude
        Toast.makeText(this,"${lat}, $lon",Toast.LENGTH_SHORT).show()
    }

    private fun getAddress(lat:Double, lon: Double): String?{
        val geocoder= Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat,lon,1)
        return address[0].getAddressLine(0).toString()
    }
}