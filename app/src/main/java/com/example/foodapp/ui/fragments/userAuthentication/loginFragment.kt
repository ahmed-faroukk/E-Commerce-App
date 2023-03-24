package com.example.foodapp.ui.fragments.userAuthentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentLoginBinding
import com.example.foodapp.models.LoginRequest
import com.example.foodapp.models.signInResponse
import com.example.foodapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class loginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    lateinit var userData: signInResponse
    lateinit var username: String
    lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        hideProgressbar()

        if (checkUserLogin())
            findNavController().navigate(R.id.action_loginFragment_to_home2)

        // handle login process

        viewModel.state.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressbar()
                    response.data?.let {
                        userData = it
                        saveLogin(binding.emailET.text.toString(),
                            binding.passwordET.text.toString() , it.email , it.gender , it.image , it.token)

                        findNavController().navigate(R.id.action_loginFragment_to_home2)
                    }
                }
                is Resource.Error -> {
                    hideProgressbar()
                    response.message?.let {
                        Log.d("Problem", response.message.toString())
                        Snackbar.make(binding.root,
                            "wrong username or password",
                            Snackbar.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressbar()
                }
            }
        })


        // send userAuth request
        binding.appCompatButton.setOnClickListener(View.OnClickListener {
            val username = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()
            val loginRequest = LoginRequest(username, password)
            viewModel.signIn(loginRequest)

        })



        return binding.root
    }

    fun showProgressbar() {

        binding.progressCircular.visibility = View.VISIBLE
    }

    fun hideProgressbar() {
        binding.progressCircular.visibility = View.INVISIBLE

    }


    private fun saveLogin(name: String, pass: String , email : String , gender : String , image : String , token : String) {
        val TAG = "ResponseFailure"
        val sharedPref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("username", name)
        editor.putString("password", pass)
        editor.putBoolean("User_logIn" , true)
        editor.putString("email", email)
        editor.putString("gender" , gender)
        editor.putString("image" , image)
        editor.putString("token" , token)
        editor.apply()

    }

    private fun checkUserLogin(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val phone = sharedPref.getString("username", null)
        return sharedPref.getBoolean("User_logIn" , false)
    }


}