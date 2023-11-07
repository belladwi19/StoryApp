package com.dicoding.mystoryapp.util

open class Event<T>(private val content: T) {

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    var hasBeenHandled = false
        private set
}