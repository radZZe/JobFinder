package com.example.jobfinder.ui.chatManager.chatlist

import android.view.View
import android.widget.TextView
import com.example.jobfinder.R

class ChatTeamOrProjectViewHolder(view: View):ChatListAdapter.ChatHolder(view) {

    val name: TextView = view.findViewById(R.id.nameTeamOrProject_field)
}