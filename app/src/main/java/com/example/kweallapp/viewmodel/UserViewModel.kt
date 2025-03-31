package com.example.kweallapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kweallapp.UserDao
import com.example.kweallapp.utils.PasswordHashingUtils
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {
    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Получаем пользователя по email
                val user = userDao.getUserByEmail(email)
                if (user != null) {
                    // Проверяем пароль с помощью PasswordHashingUtils
                    if (PasswordHashingUtils.verifyPassword(password, user.passwordHash)) {
                        onSuccess()
                    } else {
                        Log.d("UserViewModel", "Invalid password")
                        onError("Неверный email или пароль")
                    }
                } else {
                    Log.d("UserViewModel", "User not found for email: $email")
                    onError("Пользователь с таким email не найден")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Database error: ${e.message}")
                onError("Ошибка базы данных: ${e.message}")
            }
        }
    }
}