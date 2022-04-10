package com.zeinab.weatherapp.weather.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zeinab.weatherapp.HomeFragment
import com.zeinab.weatherapp.R
import com.zeinab.weatherapp.model.Daily
import java.text.SimpleDateFormat
import java.util.*


import kotlin.collections.ArrayList

class DaysRecyclerAdapter :
    RecyclerView.Adapter<DaysRecyclerAdapter.ViewHolder>() {
    var days: List<Daily> = ArrayList<Daily>()
    lateinit var context: Context
    fun setDailyData(_context: Context, days: List<Daily>){
        context= _context
        this.days = days
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.days_layout,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var sdf = SimpleDateFormat("EEEE")
        var dateFormate= Date(days[position].dt!!*1000)
        var dayOfWeakName = sdf.format(dateFormate)
        //holder.dayCloudImg.setImageResource(days[0].weather.get(0).icon!!.toInt())
        holder.txtDayDesc.text="${days[position].weather.get(0).description.toString()}"
        if(HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="Celsius"
            || HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="" ) {

            var cMax=(days[position].temp?.max!!-273.15).toInt()
            var cMin=(days[position].temp?.min!!-273.15).toInt()
            holder.txtDayTemp.text="${cMax.toString()}/${cMin.toString()} ℃"
        }
        else if(HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="Fahrenheit"){


            var cMax=(((days[position].temp?.max!!-273.15) * (9 / 5)) + 32).toInt()
            var cMin=(((days[position].temp?.min!!-273.15) * (9 / 5)) + 32).toInt()
            holder.txtDayTemp.text="${cMax.toString()}/${cMin.toString()} ℉"
        }
        else if(HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="Kelvin"){
            var cMax=days[position].temp?.max
            var cMin=days[position].temp?.min
            holder.txtDayTemp.text="${cMax.toString()}/${cMin.toString()} K"
        }

        //kelvin to culsuim

        holder.txtdayName.text=dayOfWeakName

        var myIcon = "https://openweathermap.org/img/w/${days[position].weather[0].icon}.png"
        Glide.with(context).load(myIcon)
            .placeholder(R.drawable.cloud)
            .error(R.drawable.cloud)
            .into(holder.dayCloudImg)
    }

    override fun getItemCount(): Int  = days?.size!!
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         var dayCloudImg: ImageView = itemView.findViewById(R.id.dayCloudImg)
        var txtdayName: TextView = itemView.findViewById(R.id.txtdayName)
        var txtDayDesc: TextView = itemView.findViewById(R.id.txtDayDesc)
        var txtDayTemp: TextView = itemView.findViewById(R.id.txtDayTemp)


        // var layout: CardView = itemView.findViewById(R.id.dayCardView)
    }

}