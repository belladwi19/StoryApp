package com.dicoding.mystoryapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mystoryapp.databinding.ActivityScreenAppBinding
import com.dicoding.mystoryapp.viewModel.FactoryModel
import com.dicoding.mystoryapp.viewModel.SettingModel

class ScreenAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScreenAppBinding
    private val settingModel: SettingModel by viewModels { factoryModel }
    private lateinit var factoryModel: FactoryModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factoryModel = FactoryModel.getInstance(this)

        binding.btnScreen.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            settingModel.saveIsLogin(false)
            finish()
        }

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}