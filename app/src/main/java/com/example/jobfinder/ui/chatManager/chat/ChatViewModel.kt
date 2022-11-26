package com.example.jobfinder.ui.chatManager.chat

import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel()
{
    fun getMembersChat(id:String){

    }
}