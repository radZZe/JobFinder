package com.example.jobfinder.ui.signUp

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.data.models.Student
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.SNTPClient
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
) : ViewModel(), LifecycleObserver {

    val firebase = firebaseRepository

    fun signUpAsStudent(
        image: String,
        name: String,
        surname: String,
        lastName: String,
        uni: String,
        male: String,
        age: String,
        email: String,
        password: String,
        onComplete:()->Unit,
    ) {

        SNTPClient.getDate(
            TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString())
        ) { _, date, _ ->
            var id = UUID.randomUUID().mostSignificantBits.toString()
            if (date != null) {
                val student = Student(
                    id = id,
                    email = email,
                    name = name,
                    surname = surname,
                    lastName = lastName,
                    age = age,
                    male = male,
                    Uni = uni,
                    password = password,
                    image = image,
                    createdAt = date
                )
                firebase.signUpAsStudent(student) {
                    onComplete()
                }
            }
        }
    }

    fun signUpAsEmployer(
        image: String,
        name: String,
        surname: String,
        lastName: String,
        company: String,
        male: String,
        age: String,
        email: String,
        password: String,
        onComplete:()->Unit
    ){
        SNTPClient.getDate(
            TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString())
        ) { _, date, _ ->
            var id = UUID.randomUUID().mostSignificantBits.toString()
            if (date != null) {
                val employer = Employer(
                    id = id,
                    email = email,
                    name = name,
                    surname = surname,
                    lastName = lastName,
                    age = age,
                    male = male,
                    companyName = company,
                    password = password,
                    image = image,
                    createdAt = date
                )
                firebase.signUpAsEmployer(employer) {
                    onComplete()
                }
            }
        }
    }

}