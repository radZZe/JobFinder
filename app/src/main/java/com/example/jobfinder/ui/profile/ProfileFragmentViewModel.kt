package com.example.jobfinder.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val firestore: FirebaseRepository,
    val preferenceManager: PreferenceManager
    ) : ViewModel() {

    val liveProjects = MutableLiveData<ArrayList<Project>>()

}