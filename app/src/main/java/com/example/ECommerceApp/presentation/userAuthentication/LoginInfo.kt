package com.example.ECommerceApp.presentation.userAuthentication

data class LoginInfo(val username: String, val password: String, val email: String, val id: Int, val image: String, val token: String )
{
    companion object{
        // Define constants for keys
         const val KEY_USERNAME = "username"
         const val KEY_PASSWORD = "password"
         const val KEY_IS_LOGGED_IN = "User_logIn"
         const val KEY_EMAIL = "email"
         const val KEY_ID = "id"
         const val KEY_IMAGE = "image"
         const val KEY_TOKEN = "token"

    }
}
