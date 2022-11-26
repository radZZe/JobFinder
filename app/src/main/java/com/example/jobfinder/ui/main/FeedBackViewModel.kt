package com.example.jobfinder.ui.main

import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedBackViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    fun sendFeedback(project: Project,brief:String){
        firebaseRepository.feedback(project, brief)
    }

}