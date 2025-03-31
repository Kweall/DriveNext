package com.example.kweallapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.kweallapp.databinding.Onboarding2Binding
import com.example.kweallapp.databinding.Onboarding3Binding

class Onboarding3Activity : BaseActivity() {

    private lateinit var binding: Onboarding3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Onboarding3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonContinue.setOnClickListener {
            startActivity(Intent(this, GettingStartedActivity::class.java))
        }
        binding.buttonSkip.setOnClickListener {
            startActivity(Intent(this, GettingStartedActivity::class.java))
        }
    }
}
