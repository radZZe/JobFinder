package com.example.jobfinder.ui.chatManager.chatlist

import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.ChatItem
import com.example.jobfinder.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
    preferenceManager: PreferenceManager
): ViewModel() {

    val firebase = firebaseRepository
    fun getChats(onComplete:(ArrayList<ChatItem>)->Unit){
        firebase.getChats(){
            onComplete(it)
        }
    }
}