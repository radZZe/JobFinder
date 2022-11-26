package com.example.jobfinder.ui.profile

import androidx.lifecycle.ViewModel
import com.example.jobfinder.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    val preferenceManager: PreferenceManager
    ) : ViewModel() {

}