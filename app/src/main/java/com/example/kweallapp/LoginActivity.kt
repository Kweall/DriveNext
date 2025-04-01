package com.example.kweallapp

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kweallapp.databinding.LoginBinding
import com.example.kweallapp.viewmodel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import androidx.room.Room

class LoginActivity : BaseActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userDao = (application as MyApp).database.userDao()
        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userDao)
        )[UserViewModel::class.java]

        setupButtons()

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, SignUp1Activity::class.java))
        }
    }
    private fun setupButtons() {
        binding.button2.setOnClickListener { handleLogin() }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, SignUp1Activity::class.java))
        }
    }

    private fun handleLogin() {
        val email = binding.editText.text.toString().trim()
        val password = binding.editText1.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showError("Все поля должны быть заполнены")
            return
        }

        userViewModel.loginUser(email, password,
            onSuccess = {
                Log.d("SignUp3Activity", "УСПЕХ ДАЛЬШЕ")
                startActivity(
                    Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                )
                Log.d("SignUp3Activity", "ЗАКАНЧИВАЕМ")
                finish()
            },
            onError = {
                errorMessage ->
                showError(errorMessage)
                Log.d("SignUp3Activity", "ЭЭЭЭ ОШИБКА")
            }
        )
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}

class MyApp : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration() // Удаляет старую базу данных при изменении схемы
            .build()
    }
}

// Создайте фабрику для ViewModel
class UserViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
