package com.example.jobfinder.ui.chatManager.chatlist

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.jobfinder.R

class ChatContactViewHolder(view: View):ChatListAdapter.ChatHolder(view)
{
    val nameUser: TextView = view.findViewById(R.id.user_name_field)
    val imageUser: ImageView = view.findViewById(R.id.user_avatar)
    val unreadMessageCount: TextView = view.findViewById(R.id.unread_msg)
    val unreadMessageLayout: FrameLayout = view.findViewById(R.id.unread_msg_layout)
}