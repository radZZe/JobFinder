package com.example.jobfinder.data

import androidx.lifecycle.MutableLiveData
import com.example.jobfinder.data.models.Project

interface FirebaseRepositoryInterface {

    fun getProjects(
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {}

    fun getEmployerProjects(
        userId: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {}

    fun addProject(project: Project) {}
}