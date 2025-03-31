package com.example.kweallapp

import android.content.Intent
import android.os.Bundle
import com.example.kweallapp.databinding.CongratulationsBinding
import com.example.kweallapp.databinding.Onboarding1Binding

class CongratulationsActivity : BaseActivity()  {

    private lateinit var binding: CongratulationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CongratulationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}