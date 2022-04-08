package com.zeinab.weatherapp

import android.app.ActionBar
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import java.util.*


class SettingFragment : Fragment() {
 lateinit var rgNotification:RadioGroup
 lateinit var rgTemp:RadioGroup
 lateinit var rgWind:RadioGroup
 lateinit var rgLanguage:RadioGroup
 lateinit var rgLocation:RadioGroup

 lateinit var radioGPSLocation:RadioButton
 lateinit var radioMapLocation:RadioButton
 lateinit var radioEnglishLanguage:RadioButton
 lateinit var radioArabicLanguage:RadioButton
 lateinit var radioCelsius:RadioButton
 lateinit var radioKelvin:RadioButton
 lateinit var radioFahrenheit:RadioButton
 lateinit var radioMeter:RadioButton
 lateinit var radioMill:RadioButton
 lateinit var radioEnable:RadioButton
 lateinit var radioDisable:RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocal()
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       var v:View=inflater.inflate(R.layout.fragment_setting, container, false)
        rgNotification=v.findViewById(R.id.rgNotification)
        rgTemp=v.findViewById(R.id.rgTemp)
        rgWind=v.findViewById(R.id.rgWind)
        rgLanguage=v.findViewById(R.id.rgLanguage)
        rgLocation=v.findViewById(R.id.rgLocation)

        radioGPSLocation=v.findViewById(R.id.radioGPSLocation)
        radioMapLocation=v.findViewById(R.id.radioMapLocation)
        radioEnglishLanguage=v.findViewById(R.id.radioEnglishLanguage)
        radioArabicLanguage=v.findViewById(R.id.radioArabicLanguage)
        radioCelsius=v.findViewById(R.id.radioCelsius)
        radioKelvin=v.findViewById(R.id.radioKelvin)
        radioFahrenheit=v.findViewById(R.id.radioFahrenheit)
        radioMeter=v.findViewById(R.id.radioMeter)
        radioMill=v.findViewById(R.id.radioMill)
        radioEnable=v.findViewById(R.id.radioEnable)
        radioDisable=v.findViewById(R.id.radioDisable)

        radioEnglishLanguage.setOnClickListener {
            setLocal("en")

        }
        radioArabicLanguage.setOnClickListener {
            setLocal("ar")

        }

        return v
    }

    private fun setLocal(language: String) {
        var locale:Locale= Locale(language)
        Locale.setDefault(locale)
        var configuration:Configuration=Configuration()
        configuration.locale=locale
        resources.updateConfiguration(configuration,resources.displayMetrics)
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("Languages",Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("language",language)
        editor.apply()

    }
    public fun loadLocal(){
       var sharedPreferences:SharedPreferences= requireActivity().getSharedPreferences("Languages",Context.MODE_PRIVATE)
        var languages: String? =sharedPreferences.getString("language","")
        setLocal(languages!!)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}