package com.example.kweallapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.kweallapp.LoginActivity
import com.example.kweallapp.SignUp1Activity
import com.example.kweallapp.databinding.GettingStartedBinding

class GettingStartedActivity : BaseActivity() {

    private lateinit var binding: GettingStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = GettingStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(this, SignUp1Activity::class.java))
        }
    }
}
