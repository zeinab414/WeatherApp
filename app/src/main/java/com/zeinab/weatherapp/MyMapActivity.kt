package com.zeinab.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.zeinab.weatherapp.database.ConcreteLocalSourceClass
import com.zeinab.weatherapp.databinding.ActivityMyMapBinding
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModel
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModelFactory
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.Repository
import com.zeinab.weatherapp.network.WeatherClient
import java.util.*

class MyMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMyMapBinding
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation : Location?= null
    var currentMarker: Marker? = null
    companion object {
        var lat: Double=0.0
        var lon: Double=0.0
    }
    private lateinit var favViewModel: FavViewModel
    private lateinit var favViewModelFactory: FavViewModelFactory
    private lateinit var favWeather: FavWeather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng = LatLng(currentLocation?.latitude!!,currentLocation?.longitude!!)
        mMap?.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng) {
                if(currentMarker != null){
                    currentMarker?.remove()
                }
                val newLatLng = LatLng(p0.latitude,p0.longitude)
                drawMarker(newLatLng)
            }
        })


    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat!!, lon!!,3)
        var countryName="${Adress.get(0).locality} \n ${Adress.get(0).adminArea}"
        Toast.makeText(this,"${lat}, $lon", Toast.LENGTH_SHORT).show()
        if(HomeActivity.goToAntherFragmement==1){
            val i = Intent(this, HomeActivity::class.java)
            i.putExtra("Latitude",lat)
            i.putExtra("Longtitude",lon)
            startActivity(i)
        }
        if(HomeActivity.goToAntherFragmement==2){
            favViewModelFactory= FavViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),
                ConcreteLocalSourceClass(this),this),
                lat!!, lon!!,"0406f3883d8b6a4d0cdf992646df99a0")
            favViewModel=ViewModelProvider(this,favViewModelFactory).get(FavViewModel::class.java)
            favWeather= FavWeather(lat,lon,countryName!!)
            favViewModel.insertFavWeather(favWeather)
            Toast.makeText(this,"success",Toast.LENGTH_LONG).show()
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }
        if(HomeActivity.goToAntherFragmement==3){
            finish()
        }
        if(HomeActivity.goToAntherFragmement==4){
            finish()
        }
    }

    private fun getAddress(lat:Double, lon: Double): String?{
        val geocoder= Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat,lon,1)
        return address[0].getAddressLine(0).toString()
    }
    override fun onStop() {
        super.onStop()
        if(lat!=0.0 && lon!=0.0) {
            HomeFragment.sharedPreferences.edit().putString("latitude", lat.toString()).commit()
            HomeFragment.sharedPreferences.edit().putString("longtitude", lon.toString()).commit()

        }



    }

}