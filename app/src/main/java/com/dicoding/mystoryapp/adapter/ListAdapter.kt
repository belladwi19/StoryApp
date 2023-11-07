package com.dicoding.mystoryapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mystoryapp.activity.DetailStoryActivity
import com.dicoding.mystoryapp.databinding.ItemStoryBinding
import com.dicoding.mystoryapp.response.ListStoryItem
import com.dicoding.mystoryapp.util.StoryCallback

class ListAdapter : RecyclerView.Adapter<ListAdapter.RecyclerViewHolder>() {
    private var arrayListStory = ArrayList<ListStoryItem>()
    lateinit var mContext: Context

    fun setData(newData: ArrayList<ListStoryItem>){
        val StoryCallback = StoryCallback(arrayListStory, newData)
        val diffResult = DiffUtil.calculateDiff(StoryCallback)

        arrayListStory = newData
        diffResult.dispatchUpdatesTo(this)
    }
    interface OnItemClicked {
        fun onItemClicked(dataStory: ListStoryItem)
    }

    private var onItemClicked: OnItemClicked? = null

    fun setOnItemClicked(onItemClicked: OnItemClicked) {
        this.onItemClicked = onItemClicked
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val list = arrayListStory[position]
        Glide.with(holder.itemView.context)
            .load(list.photoUrl)
            .apply(RequestOptions().override(300,300))
            .into(holder.image)
        with(holder){
            username.text = list.name
            description.text = list.description
            itemView.setOnClickListener {
                val dataStory = ListStoryItem(
                    list.photoUrl,
                    list.createdAt,
                    list.name,
                    list.description,
                    list.lon,
                    list.id,
                    list.lat
                )
                val intentActivity = Intent(mContext, DetailStoryActivity::class.java)
                intentActivity.putExtra(DetailStoryActivity.DETAIL, dataStory)
                mContext.startActivity(intentActivity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        mContext = parent.context
        return RecyclerViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = arrayListStory.size

    inner class RecyclerViewHolder(binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root){
        var image = binding.imgStory
        var username = binding.tvName
        var description = binding.tvDescription
    }
}