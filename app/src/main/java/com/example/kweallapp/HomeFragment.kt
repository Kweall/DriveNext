package com.example.kweallapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var searchBar: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфлейт макета fragment_home.xml
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Инициализация RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Подготовка данных
        val carList = listOf(
            Car("S 500 Sedan", "Mercedes-Benz", "2500₽", "А/Т", "Бензин", R.drawable.ic_car),
            Car("X7", "BMW", "3000₽", "А/Т", "Бензин", R.drawable.ic_car),
            Car("Q8", "Audi", "2800₽", "А/Т", "Бензин", R.drawable.ic_car)
        )

        // Настройка адаптера
        carAdapter = CarAdapter(carList)
        recyclerView.adapter = carAdapter

        // Инициализация поисковой строки
        searchBar = view.findViewById(R.id.search_bar)

        // Обработка нажатия на кнопку "Поиск" (или клавиши Enter)
        searchBar.setOnEditorActionListener { _, _, _ ->
            // Переход на SearchingFragment
            findNavController().navigate(R.id.action_homeFragment_to_searchingFragment)
            true
        }

        return view
    }
}