package com.example.kweallapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kweallapp.User
import com.example.kweallapp.UserDao

class SignUpViewModel(private val userDao: UserDao) : ViewModel() {
    var email: String = ""
    var password: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var patronymicName: String = ""
    var birthDate: String = ""
    var gender: String = ""
    var driverLicenseNumber: String = ""
    var driverLicenseIssueDate: String = ""
    var avatar: String = "" // Путь к аватарке
    var registrationDate: String = ""
    var driverLicensePhoto: String = ""
    var passportPhoto: String = ""
    var gmail: String = ""

    companion object {
        @Volatile
        private var instance: SignUpViewModel? = null

        fun getInstance(userDao: UserDao): SignUpViewModel {
            return instance ?: synchronized(this) {
                instance ?: SignUpViewModel(userDao).also { instance = it }
            }
        }
    }

    suspend fun saveUserToDatabase() {
        val user = User(
            email = email,
            passwordHash = password,
            firstName = firstName,
            lastName = lastName,
            patronymicName = patronymicName,
            birthDate = birthDate,
            gender = gender,
            driverLicenseNumber = driverLicenseNumber,
            driverLicenseIssueDate = driverLicenseIssueDate,
            registrationDate = registrationDate,
            avatar = avatar,
            driverLicensePhoto = driverLicensePhoto,
            passportPhoto = passportPhoto,
            gmail = gmail
        )
        Log.d("SignUpViewModel", "Saving user to database: $user")
        userDao.insert(user)
        Log.d("SignUpViewModel", "User saved successfully")
    }

    suspend fun checkIfUserExists(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    class SignUpViewModelFactory(
        private val userDao: UserDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel.getInstance(userDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}