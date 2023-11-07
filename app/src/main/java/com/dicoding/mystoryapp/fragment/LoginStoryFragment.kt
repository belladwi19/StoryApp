package com.dicoding.mystoryapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.activity.MainActivity
import com.dicoding.mystoryapp.databinding.FragmentLoginStoryBinding
import com.dicoding.mystoryapp.viewModel.AuthModel
import com.dicoding.mystoryapp.viewModel.FactoryModel

class LoginStoryFragment : Fragment() {

    private lateinit var binding: FragmentLoginStoryBinding
    private lateinit var factoryModel: FactoryModel
    private lateinit var message: String
    private val authModel: AuthModel by activityViewModels { factoryModel }

    private fun init(){
        authModel.message.observe(viewLifecycleOwner){ e ->
            e.getContentIfNotHandled()?.let {
                message = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        factoryModel = FactoryModel.getInstance(requireActivity())
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.btnLogin.setOnClickListener {
            val tvEmail = binding.tvEmail.text.toString().trim()
            val tvPassword = binding.tvPassword.text.toString().trim()

            if (tvEmail.isEmpty() || tvPassword.isEmpty()){
                Toast.makeText(context, R.string.required, Toast.LENGTH_SHORT).show()
            } else {
                authModel.login(tvEmail, tvPassword)
                authModel.error.observe(viewLifecycleOwner){ e ->
                    e.getContentIfNotHandled()?.let { error ->
                        if (!error){
                            init()
                            authModel.user.observe(viewLifecycleOwner){e ->
                                e.getContentIfNotHandled()?.let {
                                    authModel.saveUsername(it.name)
                                    authModel.saveToken(it.token)
                                    val intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Akun yang anda masukkan salah", Toast.LENGTH_SHORT).show()
                            binding.tvPassword.apply {
                                text?.clear()
                                setError(null)
                            }
                        }
                    }
                }
            }
        }

        binding.tvRedirectRegister.setOnClickListener{
            activity?.supportFragmentManager?.commit {
                replace(R.id.auth_fragment, RegisterStoryFragment())
                addToBackStack(null)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginStoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}