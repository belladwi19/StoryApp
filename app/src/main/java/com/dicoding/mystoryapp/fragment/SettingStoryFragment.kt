package com.dicoding.mystoryapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dicoding.mystoryapp.activity.AuthActivity
import com.dicoding.mystoryapp.databinding.FragmentSettingStoryBinding
import com.dicoding.mystoryapp.viewModel.AuthModel
import com.dicoding.mystoryapp.viewModel.FactoryModel

class SettingStoryFragment : Fragment() {
    private lateinit var binding : FragmentSettingStoryBinding
    private val authModel: AuthModel by activityViewModels { factoryModel }
    private lateinit var factoryModel: FactoryModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factoryModel = FactoryModel.getInstance(requireActivity())

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(activity, AuthActivity::class.java))
            activity?.finish()
        }

        binding.btnLogout.setOnClickListener {
            authModel.logout()
            startActivity(Intent(activity, AuthActivity::class.java))
            activity?.finish()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingStoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}