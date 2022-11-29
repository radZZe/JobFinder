package com.example.jobfinder.data


import androidx.lifecycle.MutableLiveData
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.data.models.Student

interface FirebaseRepositoryInterface {

    fun login(email:String,password:String,onComplete:()->Unit,onFail:()->Unit)
    fun signUpAsStudent(user:Student,onComplete:()->Unit)
    fun signUpAsEmployer(user:Employer,onComplete:()->Unit)
    fun getProjects(
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {}

    fun getEmployerProjects(
        userId: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {}

    fun getEmployeeProjects(
        userId: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) {}

    fun getFilteredProjects(
        filter: String,
        liveData: MutableLiveData<ArrayList<Project>>,
        onSuccess: () -> Unit
    ) { }
    fun addProject(project: Project,) { }

}