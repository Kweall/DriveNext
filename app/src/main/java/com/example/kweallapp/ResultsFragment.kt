package com.example.kweallapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kweallapp.Car
import com.example.kweallapp.CarAdapter
import com.example.kweallapp.R
import kotlinx.coroutines.launch

class ResultsFragment : Fragment(R.layout.fragment_results) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var database: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        database = AppDatabase.getDatabase(requireContext())

        val query = arguments?.getString("query") ?: ""

        lifecycleScope.launch {
            val formattedQuery = "%${query.lowercase()}%"
            val filteredCars = database.carDao().searchCars(formattedQuery)

            val adapter = CarAdapter(filteredCars)
            recyclerView.adapter = adapter
        }

        val imageView = view.findViewById<View>(R.id.imageView)
        imageView.setOnClickListener {
            findNavController().navigate(R.id.action_resultsFragment_to_homeFragment)
        }
    }
}