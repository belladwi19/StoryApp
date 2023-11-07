package com.dicoding.mystoryapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.FragmentRegisterStoryBinding
import com.dicoding.mystoryapp.viewModel.AuthModel
import com.dicoding.mystoryapp.viewModel.FactoryModel

class RegisterStoryFragment : Fragment() {

    private lateinit var binding: FragmentRegisterStoryBinding
    private lateinit var factoryModel: FactoryModel
    private lateinit var message: String
    private val authModel: AuthModel by activityViewModels {factoryModel}

    private fun init(){
        authModel.message.observe(viewLifecycleOwner){event ->
            event.getContentIfNotHandled()?.let {
                message = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        factoryModel = FactoryModel.getInstance(requireActivity())
        super.onViewCreated(view, savedInstanceState)
        init()

        binding.btnRegister.setOnClickListener {
            val tvEmail = binding.tvEmail.text.toString().trim()
            val tvUsername = binding.tvNama.text.toString().trim()
            val tvPassword = binding.tvPassword.text.toString()

            if (tvUsername.isEmpty() || tvEmail.isEmpty() || tvPassword.isEmpty()){
                Toast.makeText(requireContext(), R.string.required, Toast.LENGTH_SHORT).show()
            } else {
                if (tvPassword.length < 6){
                    Toast.makeText(requireContext(), R.string.pass_length_acc, Toast.LENGTH_SHORT).show()
                } else{
                    authModel.register(tvUsername, tvEmail, tvPassword)
                    authModel.error.observe(viewLifecycleOwner){e ->
                        e.getContentIfNotHandled()?.let { error ->
                            if (!error){
                                activity?.supportFragmentManager?.commit {
                                    replace(R.id.auth_fragment, LoginStoryFragment())
                                    Toast.makeText(activity, "Berhasil Register", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(requireContext(), "Gagal Register", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        binding.tvRedirectLogin.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                replace(R.id.auth_fragment, LoginStoryFragment())
                addToBackStack(null)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterStoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}