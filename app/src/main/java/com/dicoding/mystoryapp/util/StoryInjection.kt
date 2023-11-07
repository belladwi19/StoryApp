package com.dicoding.mystoryapp.util

import android.content.Context
import com.dicoding.mystoryapp.BuildConfig
import com.dicoding.mystoryapp.activity.dataStore
import com.dicoding.mystoryapp.api.ApiUser
import com.dicoding.mystoryapp.pref.UserPreferences
import com.dicoding.mystoryapp.repos.StoryRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object StoryInjection {
    fun injectStory(context: Context): StoryRepository {
        val userPreferences = UserPreferences.getInstance(context.dataStore)
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val apiService = ApiUser.getApiUser(client)
        return StoryRepository.getInstance(userPreferences, apiService)
    }
}