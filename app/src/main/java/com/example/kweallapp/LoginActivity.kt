package com.example.kweallapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kweallapp.databinding.LoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener {
        }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, SignUp1Activity::class.java))
        }
    }
}