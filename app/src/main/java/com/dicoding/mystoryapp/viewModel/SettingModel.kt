package com.dicoding.mystoryapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.repos.StoryRepository
import kotlinx.coroutines.launch

class SettingModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun saveIsLogin(value: Boolean) {
        viewModelScope.launch {
            storyRepository.saveIsLogin(value)
        }
    }

    fun getIsLogin(): LiveData<Boolean> = storyRepository.isLogin()
}