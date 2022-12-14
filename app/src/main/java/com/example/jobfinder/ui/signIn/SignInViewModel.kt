package com.example.jobfinder.ui.signIn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(firebaseRepository: FirebaseRepository) : ViewModel() {
    val firebase = firebaseRepository
    fun login(email:String,password:String,onComplete:()->Unit,onFail:()->Unit){
        firebase.login(email,password,onComplete,onFail)

    }
}