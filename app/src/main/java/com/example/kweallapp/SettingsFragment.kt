package com.example.kweallapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kweallapp.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Используем View Binding для удобства
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Инициализация базы данных
        database = AppDatabase.getDatabase(requireContext())

        binding.customButton1.setOnClickListener {
            // Переход к ProfileFragment
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }
        // Загрузка данных пользователя
        loadUserData()

        return binding.root
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            // Получаем email текущего пользователя из SharedPreferences
            val sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE)
            val currentUserEmail = sharedPreferences.getString("current_user_email", null)

            if (currentUserEmail != null) {
                val user = database.userDao().getUserByEmail(currentUserEmail)

                // Обновляем UI с данными пользователя
                user?.let {
                    binding.fullName.text = "${it.firstName} ${it.lastName}"
                    binding.email.text = "${it.email}"
                }
            } else {
                Log.e("SettingsFragment", "Email пользователя не найден")
            }
        }
    }
}