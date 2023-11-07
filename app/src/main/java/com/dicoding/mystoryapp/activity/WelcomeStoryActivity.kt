package com.dicoding.mystoryapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mystoryapp.databinding.ActivityWelcomeStoryBinding
import com.dicoding.mystoryapp.viewModel.AuthModel
import com.dicoding.mystoryapp.viewModel.FactoryModel
import com.dicoding.mystoryapp.viewModel.SettingModel

class WelcomeStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeStoryBinding
    private val authModel: AuthModel by viewModels { factoryModel }
    private val settingModel: SettingModel by viewModels { factoryModel }
    private lateinit var factoryModel: FactoryModel

    private fun startAc(){
        val delay: Long = 200
        settingModel.getIsLogin().observe(this) { isFirstTime ->
            if (isFirstTime) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@WelcomeStoryActivity, ScreenAppActivity::class.java))
                    finish()
                }, delay)
            } else {
                authModel.getToken().observe(this) { token ->
                    if (token.isNullOrEmpty() || token == "kosong") {
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this@WelcomeStoryActivity, AuthActivity::class.java))
                            finish()
                        }, delay)
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            startActivity(Intent(this@WelcomeStoryActivity, MainActivity::class.java))
                            finish()
                        }, delay)
                    }
                }
            }
        }
    }

    private fun init(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWelcomeStoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        factoryModel = FactoryModel.getInstance(this)
        init()
        supportActionBar?.hide()
        startAc()
    }
}