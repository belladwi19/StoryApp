package com.dicoding.mystoryapp.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dicoding.mystoryapp.api.ApiService
import com.dicoding.mystoryapp.pref.UserPreferences
import com.dicoding.mystoryapp.response.LoginResponse
import com.dicoding.mystoryapp.response.StoryResponse
import com.dicoding.mystoryapp.util.StoryInterceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StoryRepository(private val userPreferences: UserPreferences, private val apiService: ApiService){

    fun register(name: String, email: String, password: String): Call<LoginResponse> {
        val register: Map<String, String> = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )
        return apiService.register(register)
    }

    fun addStory(photo: MultipartBody.Part, description: RequestBody, token: String): Call<StoryResponse>{
        val client = OkHttpClient.Builder()
            .addInterceptor(StoryInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.postStory(photo, description)
    }

    fun login(email: String, password: String): Call<LoginResponse>{
        val login: Map<String, String> = mapOf(
            "email" to email,
            "password" to password
        )
        return apiService.login(login)
    }

    fun getStory(token: String): Call<StoryResponse>{
        val client = OkHttpClient.Builder()
            .addInterceptor(StoryInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        return apiService.getStory()
    }

    fun getUsername(): LiveData<String> = userPreferences.getUsername().asLiveData()
    fun getToken(): LiveData<String> = userPreferences.getToken().asLiveData()
    fun isLogin(): LiveData<Boolean> = userPreferences.isLogin().asLiveData()

    suspend fun saveUsername(value: String) = userPreferences.saveUsername(value)
    suspend fun saveToken(value: String) = userPreferences.saveToken(value)
    suspend fun saveIsLogin(value: Boolean) = userPreferences.saveisLogin(value)
    suspend fun clear() = userPreferences.clear()

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        @JvmStatic
        fun getInstance(userPreferences: UserPreferences, apiService: ApiService) : StoryRepository
                = synchronized(this){
            instance ?: StoryRepository(userPreferences, apiService)
                .also { instance = it }
        }
    }
}