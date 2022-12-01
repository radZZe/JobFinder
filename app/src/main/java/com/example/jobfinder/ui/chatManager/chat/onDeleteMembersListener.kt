package com.example.jobfinder.ui.chatManager.chat

interface onDeleteMembersListener {
    fun deleteMember(userId:String,chatId:String,type:String)
}