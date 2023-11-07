package com.dicoding.mystoryapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.repos.StoryRepository
import com.dicoding.mystoryapp.response.ListStoryItem
import com.dicoding.mystoryapp.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeModel(private val storyRepository: StoryRepository): ViewModel() {

    fun getStory(token: String){
        val client = storyRepository.getStory(token)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _message.value = t.message
            }

            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful){
                    val responses = response.body()?.listStory
                    _userStories.postValue(responses!! as ArrayList<ListStoryItem>?)
                }
            }
        })
    }
    fun getToken() = storyRepository.getToken()

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var _userStories = MutableLiveData<ArrayList<ListStoryItem>>()
    val userStories: LiveData<ArrayList<ListStoryItem>> = _userStories
}