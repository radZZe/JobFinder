package com.example.jobfinder.data

import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.data.models.Student

interface FirebaseRepositoryInterface {

    fun login(email:String,password:String,onComplete:()->Unit)
    fun signUpAsStudent(user:Student,onComplete:()->Unit)
    fun signUpAsEmployer(user:Employer,onComplete:()->Unit)
}