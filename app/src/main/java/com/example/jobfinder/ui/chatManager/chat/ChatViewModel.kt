package com.example.jobfinder.ui.chatManager.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Message
import com.example.jobfinder.data.models.ProjectMember
import com.example.jobfinder.data.models.Team
import com.example.jobfinder.data.models.TeamMember
import com.example.jobfinder.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val manager:PreferenceManager
) : ViewModel() {
    var messages = MutableLiveData<java.util.ArrayList<Message>>()

    val firebase = firebaseRepository
    fun getTeamMembersChat(id: String, onComplete: (members: ArrayList<TeamMember>) -> Unit) {
        var members = arrayListOf<TeamMember>()
        firebase.database.collection(KEY_COLLECTION_TEAMS).document(id)
            .collection(KEY_COLLECTION_MEMBERS).get().addOnCompleteListener {
                it.result.documents.forEach {
                    var id = it.get(KEY_USER_ID)!! as String
                    var name = it.get(KEY_USER_NAME)!! as String
                    var surname = it.get(KEY_USER_SURNAME)!! as String
                    var uni = it.get(KEY_USER_UNI)!! as String
                    var member = TeamMember(
                        id = id,
                        name = name,
                        surname = surname,
                        uni = uni
                    )
                    members.add(member)
                }
                onComplete(members)

            }
    }

    fun getProjectMembersChat(id: String, onComplete: (ArrayList<ProjectMember>) -> Unit) {
        var members = arrayListOf<ProjectMember>()
        firebase.database.collection(KEY_COLLECTION_PROJECTS).document(id)
            .collection(KEY_COLLECTION_MEMBERS).get().addOnCompleteListener {
                it.result.documents.forEach {
                    var id = it.get(KEY_USER_ID)!! as String
                    var owner = it.get(KEY_OWNER)!! as String
                    var member = ProjectMember(
                        id = id,
                        owner = owner
                    )
                    members.add(member)
                }
                onComplete(members)

            }
    }

    fun listenMessageProject(
        members: ArrayList<ProjectMember>,
        projectId: String,
        onSuccess: (java.util.ArrayList<Message>) -> Unit
    ) {
        messages.value = arrayListOf<Message>()
        firebaseRepository.listenMessageProject(members, projectId, messages) {
            onSuccess(it)
        }

    }



    fun listenMessageTeam(
        members: ArrayList<TeamMember>,
        teamId: String,
        onSuccess: (java.util.ArrayList<Message>) -> Unit
    ) {
        messages.value = arrayListOf<Message>()
        firebaseRepository.listenMessageTeam(members,teamId,messages){
            onSuccess(it)
        }

    }

    fun getSenderId():String{
        return manager.getString(KEY_USER_ID)!!
    }

    fun sendMessageForProject(projectId:String,text:String){
        var senderId = getSenderId()
        firebaseRepository.sendMessageForProject(projectId,text,senderId)
    }

    fun sendMessageForTeam(teamId: String,text: String){
        var senderId = getSenderId()
        firebaseRepository.sendMessageForTeam(teamId,text,senderId)
    }

    fun addUserToTeam(email:String,teamName:String,teamId:String,onComplete:()->Unit,onFail:()->Unit){
        firebase.addUserToTeam(email,teamName,teamId, onComplete = {name , surname ->
            var text = "*Added member ${name} ${surname}*"
            sendMessageForTeam(teamId,text)
            onComplete()
        },onFail={
            onFail()
        })
    }

    fun getOwnerTeamId(teamId: String,onComplete: (ownerId:String) -> Unit){
        firebaseRepository.getOwnerTeamId(teamId){
            onComplete(it)
        }
    }
}