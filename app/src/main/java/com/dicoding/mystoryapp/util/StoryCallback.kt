package com.dicoding.mystoryapp.util

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.mystoryapp.response.ListStoryItem

class StoryCallback(private val lastList: ArrayList<ListStoryItem>, private val firstList: ArrayList<ListStoryItem>) : DiffUtil.Callback(){

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = when{
        lastList[oldItemPosition].id != firstList[newItemPosition].id -> false
        lastList[oldItemPosition].name != firstList[newItemPosition].name -> false
        lastList[oldItemPosition].description != firstList[newItemPosition].description -> false
        lastList[oldItemPosition].photoUrl != firstList[newItemPosition].photoUrl -> false
        else -> true
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = lastList[oldItemPosition].id == firstList[newItemPosition].id

    override fun getNewListSize(): Int = lastList.size

    override fun getOldListSize(): Int = firstList.size
}