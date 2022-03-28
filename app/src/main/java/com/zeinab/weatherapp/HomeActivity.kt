package com.zeinab.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar

    //test
    lateinit var toggle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val transaction=this.supportFragmentManager.beginTransaction()
        var homeFragment=HomeFragment()
        var settingFragment=SettingFragment()
        var favouriteFragment=FavouriteFragment()
       transaction.replace(R.id.viewLayout,homeFragment).addToBackStack(null).commit()

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)
        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setNavigationItemSelectedListener {

            if(it.itemId==R.id.home_item){
                val transaction=this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.viewLayout,homeFragment).addToBackStack(null)
                transaction.commit()
            }
            if(it.itemId==R.id.setting_item){
                val transaction=this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.viewLayout,settingFragment).addToBackStack(null)
                transaction.commit()
            }
            if(it.itemId==R.id.fav_item){
                val transaction=this.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.viewLayout,favouriteFragment).addToBackStack(null)
                transaction.commit()
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
}