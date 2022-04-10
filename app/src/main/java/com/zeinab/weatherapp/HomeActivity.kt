package com.zeinab.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.provider.Settings



import androidx.core.app.ActivityCompat

import android.location.LocationManager
import android.os.Looper
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.OneTimeWorkRequest
import com.google.android.gms.location.*
import java.util.*


class HomeActivity : AppCompatActivity() ,ICommunicator{
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    companion object{
       var goToAntherFragmement:Int=0
        lateinit var requests: ArrayList<OneTimeWorkRequest>
        var destinationID=0

    }
    //test
    lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        requests = ArrayList<OneTimeWorkRequest>()





        var homeFragment=HomeFragment()
        var settingFragment=SettingFragment()
        var favouriteFragment=FavouriteFragment()
        var alertFragment=AlertFragment()

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)
        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()





        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener {

            if(it.itemId==R.id.home_item){
                val transaction=this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.viewLayout,homeFragment).addToBackStack(null).commit()
           drawerLayout.closeDrawers()
            }
            else if(it.itemId==R.id.setting_item){
                val transaction=this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.viewLayout,settingFragment).addToBackStack(null)
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            else if(it.itemId==R.id.fav_item){
                val transaction=this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.viewLayout,favouriteFragment).addToBackStack(null)
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            else if(it.itemId==R.id.alert_item){
                val transaction=this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.viewLayout,alertFragment).addToBackStack(null)
                transaction.commit()
                drawerLayout.closeDrawers()
            }
            true
        }

        if(goToAntherFragmement==0){
            val transaction=this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.viewLayout,homeFragment).addToBackStack(null).commit()
        }

       else if(goToAntherFragmement==1){
            val bundle: Bundle? = intent.extras
            val lat: Double = intent.getDoubleExtra("Latitude",0.0)
            val lon: Double = intent.getDoubleExtra("Longtitude",0.0)
            var mBundle=Bundle()
            mBundle.putDouble("lat",lat)
            mBundle.putDouble("lon",lon)
            homeFragment.arguments = mBundle
            val transaction=this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.viewLayout,homeFragment).addToBackStack(null)
            transaction.commit()
        }
       else if(goToAntherFragmement==2){

            val transaction=this.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.viewLayout,favouriteFragment).addToBackStack(null).commit()
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.viewLayout) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val navGraph = navHostFragment!!.navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(R.id.alert_fragment)
        navController.graph = navGraph
    }

    override fun goToAntherFragment(lat: Double, lon: Double) {
        val bundle = Bundle()
        bundle.putDouble("lat",lat)
        bundle.putDouble("lon",lon)
        val transaction=this.supportFragmentManager.beginTransaction()
        val showFavInfoFragment=ShowFavInfoFragment()
        showFavInfoFragment.arguments=bundle
        transaction.replace(R.id.viewLayout,showFavInfoFragment).addToBackStack(null)
        transaction.commit()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}