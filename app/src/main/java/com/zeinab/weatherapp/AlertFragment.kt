package com.zeinab.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AlertFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var v:View=inflater.inflate(R.layout.fragment_alert, container, false)

        var addAlertBtn: FloatingActionButton =v.findViewById(R.id.addAlertBtn)

        addAlertBtn.setOnClickListener {


            var navController:NavController=Navigation.findNavController(addAlertBtn)
            var navDirections:NavDirections=AlertFragmentDirections.alertMakealertDest()
            navController.navigate(navDirections)

        }
        return v
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlertFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}