package com.example.kweallapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Найдите кнопку и добавьте обработчик нажатия
        val button = view.findViewById<View>(R.id.custom_button1)
        button.setOnClickListener {
            // Переход к ProfileFragment
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }

        return view
    }
}