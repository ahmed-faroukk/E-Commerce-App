package com.example.foodapp.ui.fragments.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.foodapp.Repository.RepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class profileViewModel @Inject constructor(
    private val repo: RepositoryImp,
   private val app: Application,
) : AndroidViewModel(app) {

}