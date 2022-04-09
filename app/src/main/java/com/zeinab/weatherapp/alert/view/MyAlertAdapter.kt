package com.zeinab.weatherapp.alert.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zeinab.weatherapp.R

import com.zeinab.weatherapp.model.AlertWeather



class MyAlertAdapter(private val listner: OnClickDeleteListnerAlert):
    RecyclerView.Adapter<MyAlertAdapter.ViewHolder>()  {
    var alertWeather: List<AlertWeather> = ArrayList<AlertWeather>()
    lateinit var context: Context
    fun setFavWeather(_context: Context, alertWeather: List<AlertWeather>){
        context= _context
        this.alertWeather = alertWeather
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alert_row,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtStartTime.text=alertWeather[position].starTime
        holder.txtStartDate.text=alertWeather[position].startDate
        holder.txtEndDate.text=alertWeather[position].endDate
        holder.txtEntTime.text=alertWeather[position].id.toString()
        holder.deleteAlertImg.setOnClickListener {
            listner.onClick(alertWeather[position])
        }



    }

    override fun getItemCount(): Int  = alertWeather?.size!!
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var deleteAlertImg: ImageView = itemView.findViewById(R.id.deleteAlertImg)
        var txtStartTime: TextView = itemView.findViewById(R.id.txtStartTime)
        var txtStartDate: TextView = itemView.findViewById(R.id.txtStartDate)
        var txtEndDate: TextView = itemView.findViewById(R.id.txtEndDate)
        var txtEntTime: TextView = itemView.findViewById(R.id.txtEntTime)


    }
    }

