package com.example.coinempireprophet.view.splash.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coinempireprophet.databinding.SplashActivityBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: SplashActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SplashActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}