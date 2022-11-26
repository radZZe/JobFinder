package com.example.jobfinder.data

<<<<<<< HEAD
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
=======
import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.data.models.Student

interface FirebaseRepositoryInterface {

    fun login(email:String,password:String,onComplete:()->Unit)
    fun signUpAsStudent(user:Student,onComplete:()->Unit)
    fun signUpAsEmployer(user:Employer,onComplete:()->Unit)
>>>>>>> a3dff51068c073539f014efb19c122486190c96d
}