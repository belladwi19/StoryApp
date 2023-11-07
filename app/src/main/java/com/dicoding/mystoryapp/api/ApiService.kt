package com.dicoding.mystoryapp.api

import com.dicoding.mystoryapp.response.LoginResponse
import com.dicoding.mystoryapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun register(
        @Body user: Map<String, String>
    ) : Call<LoginResponse>

    @POST("login")
    fun login(
        @Body user: Map<String, String>
    ) : Call<LoginResponse>

    @GET("stories")
    fun getStory() : Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Part photo : MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Call<StoryResponse>
}