package com.example.kweallapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class SearchingFragment : Fragment(R.layout.fragment_searching) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Имитация поиска (задержка 2 секунды)
        Handler(Looper.getMainLooper()).postDelayed({
            // Переход на экран результатов поиска
            findNavController().navigate(R.id.action_searchingFragment_to_resultsFragment)
        }, 2000)
    }
}