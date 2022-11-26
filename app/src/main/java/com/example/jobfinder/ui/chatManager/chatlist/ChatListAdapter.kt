package com.example.jobfinder.ui.chatManager.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.ChatItem
import com.example.jobfinder.utils.TYPE_CONTACT
import com.example.jobfinder.utils.TYPE_PROJECT
import com.example.jobfinder.utils.TYPE_TEAM

class ChatListAdapter: RecyclerView.Adapter<ChatListAdapter.ChatHolder> {

    lateinit var chats: List<ChatItem>


    constructor(
        chats: List<ChatItem>

    ) {
        this.chats = chats
    }

    class ChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameChat: TextView = view.findViewById(R.id.nameTeamOrProject_field)
        val cardView:CardView = view.findViewById(R.id.chat_item_cardView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val layout = R.layout.chat_item_teamorproject
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatHolder(view)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.nameChat.text = chats[position].name
        holder.cardView.setOnClickListener{
            TODO()
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}
