package com.example.ECommerceApp.presentation.userAuthentication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.ECommerceApp.R
import com.example.ECommerceApp.common.util.Resource
import com.example.ECommerceApp.common.util.invisible
import com.example.ECommerceApp.common.util.isNullorEmpty
import com.example.ECommerceApp.common.util.visible
import com.example.ECommerceApp.common.viewBinding
import com.example.ECommerceApp.databinding.FragmentLoginBinding
import com.example.ECommerceApp.data.model.LoginRequest
import com.example.ECommerceApp.data.model.signInResponse
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class loginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()
    lateinit var userData: signInResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressCircular.invisible()

        if (checkUserLogin())
            findNavController().navigate(R.id.action_loginFragment_to_home2)

        // handle login process

        viewModel.state.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressCircular.invisible()
                    response.data?.let {
                        userData = it
                        val loginInfo = LoginInfo(binding.emailET.text.toString(),
                        binding.passwordET.text.toString() , it.email , it.id , it.image , it.token)
                        saveLogin(loginInfo)
                        findNavController().navigate(R.id.action_loginFragment_to_home2)
                    }
                }
                is Resource.Error -> {
                    binding.progressCircular.invisible()
                    response.message?.let {
                        Log.d("Problem", response.message.toString())
                        Snackbar.make(binding.root,
                            "wrong username or password",
                            Snackbar.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    binding.progressCircular.visible()
                }
            }
        })


        //username: 'kminchelle'
        //    password: '0lelplR'

        binding.appCompatButton.setOnClickListener(View.OnClickListener {
            if(binding.emailET.isNullorEmpty("please enter your mail") && binding.passwordET.isNullorEmpty("please enter your password ")){
                val username = binding.emailET.text.toString()
                val password = binding.passwordET.text.toString()
                val loginRequest = LoginRequest(username, password)
                viewModel.signIn(loginRequest)
            }


        })



    }

    private fun saveLogin(loginInfo: LoginInfo) {
        val sharedPref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(LoginInfo.KEY_USERNAME, loginInfo.username)
        editor.putString(LoginInfo.KEY_PASSWORD, loginInfo.password)
        editor.putBoolean(LoginInfo.KEY_IS_LOGGED_IN , true)
        editor.putString(LoginInfo.KEY_EMAIL, loginInfo.email)
        editor.putInt(LoginInfo.KEY_ID , loginInfo.id)
        editor.putString(LoginInfo.KEY_IMAGE , loginInfo.image)
        editor.putString(LoginInfo.KEY_TOKEN , loginInfo.token)
        editor.apply()

    }

    private fun checkUserLogin(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val phone = sharedPref.getString("username", null)
        return sharedPref.getBoolean("User_logIn" , false)
    }


}