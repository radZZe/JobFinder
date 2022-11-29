package com.example.jobfinder.ui.chatManager.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.ChatMember
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatMembersListFragmentViewModel @Inject constructor(
    private val firestore: FirebaseRepository
) : ViewModel() {

    val chatMembersLiveData = MutableLiveData<ArrayList<ChatMember>>()

    fun getMembers(onSuccess: () -> Unit) {

    }
}