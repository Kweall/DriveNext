package com.example.kweallapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isInternetAvailable()) {
                val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val isOnboardingShown = sharedPreferences.getBoolean("is_onboarding_shown", false)

                val intent = if (isOnboardingShown) {
                    Intent(this, TestActivity::class.java)
                } else {
                    sharedPreferences.edit().putBoolean("is_onboarding_shown", true).apply()
                    Intent(this, Onboarding1Activity::class.java)
                }

                startActivity(intent)
            } else {
                startActivity(Intent(this, NoConnectionActivity::class.java))
            }
            finish()
        }, 3000)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}
