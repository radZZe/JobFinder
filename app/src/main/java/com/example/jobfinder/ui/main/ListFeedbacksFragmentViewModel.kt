package com.example.jobfinder.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.UserFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListFeedbacksFragmentViewModel @Inject constructor(
    private val firestore: FirebaseRepository
) : ViewModel() {

    val liveFeedbacks = MutableLiveData<ArrayList<UserFeedback>>()

    fun acceptFeedback(project: Project) {
        firestore.addStudentToProject(project)
    }

    fun getFeedbacks(project:Project){
        firestore.getFeedbacks(project,liveFeedbacks){}
    }
}