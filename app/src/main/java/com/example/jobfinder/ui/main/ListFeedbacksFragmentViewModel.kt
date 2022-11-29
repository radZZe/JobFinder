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

//    val liveFeedbacks = MutableLiveData<ArrayList<UserFeedback>>()

    fun acceptFeedback(project: Project,userId:String,onFail:()->Unit) {
        firestore.addStudentToProject(project,userId,onFail)
    }

    fun rejectFeedback(project: Project,userId:String){
        firestore.feedBackReject(project,userId)
    }

    fun getFeedbacks(project:Project,onSucces:(ArrayList<UserFeedback>)->Unit){
        firestore.getFeedbacks(project){
            onSucces(it)
        }
    }
}