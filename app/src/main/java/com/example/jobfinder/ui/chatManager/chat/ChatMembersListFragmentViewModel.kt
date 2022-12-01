package com.example.jobfinder.ui.chatManager.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.ChatMember
import com.example.jobfinder.data.models.ProjectMember
import com.example.jobfinder.data.models.TeamMember
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatMembersListFragmentViewModel @Inject constructor(
    private val firestore: FirebaseRepository
) : ViewModel() {

    val chatMembersLiveData = MutableLiveData<ArrayList<ChatMember>>()

    fun delProjectChatMember(userId:String,projectId:String,onSuccess:()->Unit,onFail:()->Unit){
        firestore.delProjectChatMember(userId,projectId,
        onSuccess = onSuccess,
        onFail = onFail)
    }

    fun delTeamChatMember(userId: String, teamId:String,onSuccess:()->Unit,onFail:()->Unit){
        firestore.delTeamChatMember(userId,teamId,onSuccess,onFail)
    }

    fun getProjectChatMember(id:String,onComplete:(ArrayList<ProjectMember>)->Unit){
        firestore.getProjectMembersChat(id,onComplete)
    }

    fun getTeamChatMember(id: String,onComplete: (ArrayList<TeamMember>) -> Unit){
        firestore.getTeamMembersChat(id,onComplete)
    }

//    fun getMembers(onSuccess: () -> Unit) {
//
//    }
}