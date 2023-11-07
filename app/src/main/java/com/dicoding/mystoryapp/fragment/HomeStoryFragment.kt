package com.dicoding.mystoryapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystoryapp.activity.DetailStoryActivity
import com.dicoding.mystoryapp.adapter.ListAdapter
import com.dicoding.mystoryapp.databinding.FragmentHomeStoryBinding
import com.dicoding.mystoryapp.response.ListStoryItem
import com.dicoding.mystoryapp.viewModel.FactoryModel
import com.dicoding.mystoryapp.viewModel.HomeModel

class HomeStoryFragment : Fragment() {
    private lateinit var binding: FragmentHomeStoryBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var factoryModel: FactoryModel
    private val homeModel: HomeModel by activityViewModels {factoryModel}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factoryModel = FactoryModel.getInstance(requireActivity())
        listAdapter = ListAdapter()
        homeModel.getToken().observe(viewLifecycleOwner){
            homeModel.getStory(it)
        }
        homeModel.message.observe(viewLifecycleOwner){
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }
        binding.rvStoryApp.layoutManager = LinearLayoutManager(activity)
        homeModel.userStories.observe(viewLifecycleOwner){
            listAdapter.setData(it)
        }
        binding.rvStoryApp.adapter = listAdapter
        listAdapter.setOnItemClicked(object : ListAdapter.OnItemClicked{
            override fun onItemClicked(dataStory: ListStoryItem) {
                showSelectedStory(dataStory)
            }
        })
    }

    private fun showSelectedStory(story: ListStoryItem){
        ListStoryItem(
            story.photoUrl,
            story.createdAt,
            story.name,
            story.description,
            story.lon,
            story.id,
            story.lat
        )
        val intent = Intent(context, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.DETAIL, story)
        this.startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeStoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}