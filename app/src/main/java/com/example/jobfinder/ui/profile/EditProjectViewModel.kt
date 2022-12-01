package com.example.jobfinder.ui.profile

import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProjectViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) :  ViewModel() {

    fun editProject(project: Project) {
        firebaseRepository.editProject(project)
    }

    fun deleteProject(project: Project,onComplete:()->Unit) {
        firebaseRepository.deleteProject(project,onComplete)
    }
}