package com.dicoding.mystoryapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.repos.StoryRepository
import com.dicoding.mystoryapp.response.LoginResponse
import com.dicoding.mystoryapp.response.LoginResult
import com.dicoding.mystoryapp.util.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class AuthModel(private val storyRepository: StoryRepository): ViewModel() {

    fun saveUsername(username: String?){
        viewModelScope.launch {
            if (username != null){
                storyRepository.saveUsername(username)
            }
        }
    }

    fun saveToken(token: String?){
        viewModelScope.launch {
            if (token != null){
                storyRepository.saveToken(token)
            }
        }
    }

    fun getToken() = storyRepository.getToken()

    fun logout(){
        viewModelScope.launch {
            storyRepository.clear()
        }
    }

    fun register(username: String, email: String, password: String){
        val client = storyRepository.register(username, email, password)
        client.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _error.value = Event(false)
                } else {
                    _error.value = Event(true)
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.value = Event(true)
                _message.value = Event(t.message.toString())
            }
        })
    }

    fun login(email: String, password: String){
        val client = storyRepository.login(email, password)
        client.enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.value = Event(true)
                _message.value = Event(t.message.toString())
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()?.loginResult
                    _error.value = Event(false)
                    _user.postValue(Event(responseBody!!))
                } else {
                    _error.value = Event(true)
                    _message.value = Event(response.message())
                }
            }
        })
    }

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    private var _user = MutableLiveData<Event<LoginResult>>()
    val user: LiveData<Event<LoginResult>> = _user

    private var _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message
}