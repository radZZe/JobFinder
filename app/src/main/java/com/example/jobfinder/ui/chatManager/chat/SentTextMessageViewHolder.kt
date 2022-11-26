package com.example.jobfinder.ui.chatManager.chat

import android.view.View
import android.widget.TextView
import com.example.jobfinder.R

class SentTextMessageViewHolder(view: View) : UserChatAdapter.UserChatViewHolder(view) {
    val message: TextView = view.findViewById(R.id.message_text)
    val dateTime: TextView = view.findViewById(R.id.message_dateTime)
}