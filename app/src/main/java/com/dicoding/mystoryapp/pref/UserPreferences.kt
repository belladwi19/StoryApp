package com.dicoding.mystoryapp.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveUsername(username: String){
        dataStore.edit {
            it[IS_USERNAME] = username
        }
    }

    suspend fun saveToken(token: String){
        dataStore.edit {
            it[IS_TOKEN] = token
        }
    }

    suspend fun saveisLogin(isLogin: Boolean){
        dataStore.edit {
            it[IS_LOGIN] = isLogin
        }
    }

    suspend fun clear(){
        dataStore.edit {
            it.clear()
        }
    }

    fun getUsername(): Flow<String> = dataStore.data.map {
        it[IS_USERNAME] ?: USER_DEFAULT
    }

    fun getToken(): Flow<String> = dataStore.data.map {
        it[IS_TOKEN] ?: USER_DEFAULT
    }

    fun isLogin(): Flow<Boolean> = dataStore.data.map {
        it[IS_LOGIN] ?: true
    }

    companion object{
        private val IS_LOGIN = booleanPreferencesKey("IS_LOGIN")
        private val IS_USERNAME = stringPreferencesKey("IS_USERNAME")
        private val IS_TOKEN = stringPreferencesKey("IS_TOKEN")

        @Volatile
        private var INSTANCE: UserPreferences? = null
        const val USER_DEFAULT = "kosong"
        fun getInstance(dataStore: DataStore<Preferences>) : UserPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}