package com.example.kweallapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.kweallapp.databinding.ProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: ProfileBinding
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Используем View Binding
        binding = ProfileBinding.inflate(inflater, container, false)

        // Инициализация базы данных
        database = AppDatabase.getDatabase(requireContext())

        // Загрузка данных пользователя
        loadUserData()

        return binding.root
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            // Получаем текущего пользователя (например, по email)
            val sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE)
            val currentUserEmail = sharedPreferences.getString("current_user_email", null)

            if (currentUserEmail != null) {
                val user = database.userDao().getUserByEmail(currentUserEmail)

                // Обновляем UI с данными пользователя
                user?.let {
                    binding.fullName.text = "${it.firstName} ${it.lastName}"
                    binding.registrationdate.text = "Присоединился ${it.registrationDate}"
                    binding.emailText.text = it.email
                    binding.gender.text = it.gender
                    binding.gmail.text = it.gmail

                    // Отображаем аватарку
                    if (!it.avatar.isNullOrEmpty()) {
                        binding.imageView1.setImageURI(it.avatar.toUri())
                    } else {
                        // Устанавливаем placeholder, если аватарки нет
                        binding.imageView1.setImageResource(R.drawable.ic_avatar)
                    }
                }
            } else {
                // Обработка случая, если email не найден
                println("Email пользователя не найден")
            }
        }
    }
}