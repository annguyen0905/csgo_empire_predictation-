package com.example.coinempireprophet.view.home.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coinempireprophet.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}