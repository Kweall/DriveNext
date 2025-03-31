package com.example.kweallapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.kweallapp.databinding.Onboarding1Binding
import com.example.kweallapp.databinding.Onboarding2Binding

class Onboarding2Activity : BaseActivity() {

    private lateinit var binding: Onboarding2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Onboarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonContinue.setOnClickListener {
            startActivity(Intent(this, Onboarding3Activity::class.java))
        }
        binding.buttonSkip.setOnClickListener {
            startActivity(Intent(this, GettingStartedActivity::class.java))
        }
    }
}
