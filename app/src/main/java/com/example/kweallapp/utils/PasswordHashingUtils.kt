package com.example.kweallapp.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordHashingUtils {

    // Метод для хэширования пароля
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Метод для проверки пароля
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}