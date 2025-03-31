package com.example.kweallapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kweallapp.Car
import com.example.kweallapp.CarAdapter
import com.example.kweallapp.R

class ResultsFragment : Fragment(R.layout.fragment_results) {

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Подготовка данных
        val carList = listOf(
            Car(1,"S 500 Sedan", "Mercedes-Benz", 2500, "А/Т", "Бензин", R.drawable.ic_car),
            Car(2, "X7", "BMW", 3000, "А/Т", "Бензин", R.drawable.ic_car),
            Car(3, "Q8", "Audi", 2800, "А/Т", "Бензин", R.drawable.ic_car)
        )

        // Настройка адаптера
        val adapter = CarAdapter(carList)
        recyclerView.adapter = adapter

        // Найдите imageView и добавьте обработчик нажатия
        val imageView = view.findViewById<View>(R.id.imageView)
        imageView.setOnClickListener {
            // Переход обратно на HomeFragment
            findNavController().navigate(R.id.action_resultsFragment_to_homeFragment)
        }
    }
}