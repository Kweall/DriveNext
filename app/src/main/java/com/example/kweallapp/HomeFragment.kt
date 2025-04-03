package com.example.kweallapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var searchBar: TextInputEditText
    private lateinit var database: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        database = AppDatabase.getDatabase(requireContext())

        // Получение данных из базы данных
        lifecycleScope.launch {
            val carList = database.carDao().getAllCars()
            carAdapter = CarAdapter(carList)
            recyclerView.adapter = carAdapter
        }

        searchBar = view.findViewById(R.id.search_bar)

        searchBar.setOnEditorActionListener { _, _, _ ->
            val query = searchBar.text.toString().trim()
            if (query.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putString("query", query)

                findNavController().navigate(R.id.action_homeFragment_to_searchingFragment, bundle)
            }
            true
        }
    }
}