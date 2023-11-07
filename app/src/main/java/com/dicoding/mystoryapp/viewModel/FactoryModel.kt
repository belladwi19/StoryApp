package com.dicoding.mystoryapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.repos.StoryRepository
import com.dicoding.mystoryapp.util.StoryInjection

class FactoryModel private constructor(private val storyRepository: StoryRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(SettingModel::class.java) -> SettingModel(storyRepository) as T
            modelClass.isAssignableFrom(AuthModel::class.java) -> AuthModel(storyRepository) as T
            modelClass.isAssignableFrom(HomeModel::class.java) -> HomeModel(storyRepository) as T
            modelClass.isAssignableFrom(StoryModel::class.java) -> StoryModel(storyRepository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found: ${modelClass.name}")
        }
    }
    companion object {
        private var instance: FactoryModel? = null

        fun getInstance(context: Context): FactoryModel = instance ?: synchronized(this){
            instance ?: FactoryModel(
                StoryInjection.injectStory(context)
            )
        }.also {
            instance = it
        }
    }
}