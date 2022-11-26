package com.example.jobfinder.ui.main

import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val manager:PreferenceManager
): ViewModel() {

    fun feedback(project: Project){
//        firebaseRepository.feedback(project)
    }
}