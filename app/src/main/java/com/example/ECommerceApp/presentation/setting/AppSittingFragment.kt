package com.example.ECommerceApp.presentation.setting

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.ECommerceApp.R
import com.example.ECommerceApp.common.viewBinding
import com.example.ECommerceApp.databinding.FragmentAppSittingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

//
@AndroidEntryPoint
class AppSittingFragment : Fragment(R.layout.fragment_app_sitting) {
    private val binding: FragmentAppSittingBinding by viewBinding(FragmentAppSittingBinding::bind)
    private lateinit var preferences: SharedPreferences
    lateinit var currentLanguage: String
    lateinit var currentThemeIs: String
    var currentNotificationStateIs: Boolean = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleFragmentColorInDarkMood()
        rememberSettingLastModify()
        setAppLanguage()
        setAppMood()
        setNotificationStatus()
        saveChanges()

    }



    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireContext().resources.updateConfiguration(config,
            requireContext().resources.displayMetrics)
    }

    private fun rememberSettingLastModify() {
        preferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        currentLanguage = preferences.getString("language", "en") ?: "en"
        currentNotificationStateIs = preferences.getBoolean("notification", true)
        if (currentLanguage == "en") {
            binding.english.isChecked = true
        } else {
            binding.arabic.isChecked = true
        }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.darrk.isChecked = true
            currentThemeIs = "d"
        } else {
            // Light mode is enabled
            binding.lighht.isChecked = true
            currentThemeIs = "l"
        }
        if (currentNotificationStateIs == true) {
            binding.enable.isChecked = true
        } else {
            binding.disable.isChecked = true
        }
    }
    private fun setAppLanguage() {
        binding.radioLang.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.english -> {
                    setLocale("en")
                    currentLanguage = "en"
                }
                R.id.arabic -> {
                    setLocale("ar")
                    currentLanguage = "ar"

                }
            }
        }
    }

    private fun setAppMood() {

        binding.mood.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.darrk -> {
                    currentThemeIs = "d"
                }
                R.id.lighht -> {
                    currentThemeIs = "l"
                }
            }

        }
    }

    private fun setNotificationStatus() {
        binding.notification.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.enable -> {
                    currentNotificationStateIs = true
                }
                R.id.disable -> {
                    currentNotificationStateIs = false
                }
            }
        }
    }

    private fun saveChanges() {
        binding.sittingSaveChangesBtn.setOnClickListener {
            preferences.edit().putString("language", currentLanguage).apply()
            preferences.edit().putString("mood", currentThemeIs).apply()
            preferences.edit().putBoolean("notification", currentNotificationStateIs).apply()
            if (currentThemeIs == "d") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            requireActivity().recreate() // Refresh the activity to apply the new locale
        }
    }

    private fun handleFragmentColorInDarkMood(){
        val currentNightMode =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // Light mode
                binding.radioLang.setBackgroundResource(R.color.white)
                binding.mood.setBackgroundResource(R.color.white)
                binding.notification.setBackgroundResource(R.color.white)

            }
            Configuration.UI_MODE_NIGHT_YES -> {
                // Dark mode
                binding.radioLang.setBackgroundResource(R.color.white2_darkMood)
                binding.mood.setBackgroundResource(R.color.white2_darkMood)
                binding.notification.setBackgroundResource(R.color.white2_darkMood)

            }
        }
    }



}