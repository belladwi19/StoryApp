package com.dicoding.mystoryapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.repos.StoryRepository
import com.dicoding.mystoryapp.response.StoryResponse
import com.dicoding.mystoryapp.util.Event
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class StoryModel(private val storyRepository: StoryRepository): ViewModel(){

    private var _message = MutableLiveData<Event<String>>()
    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    fun add(photo: MultipartBody.Part, description: RequestBody, token: String){
        val client = storyRepository.addStory(photo, description, token)
        client.enqueue(object : retrofit2.Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    _error.postValue(Event(false))
                } else {
                    _message.postValue(Event(response.message()))
                    _error.postValue(Event(true))
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }
}