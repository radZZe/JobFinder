package com.example.jobfinder.ui.chatManager.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.ChatItem
import com.example.jobfinder.utils.*

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
        val icon: ImageView = view.findViewById(R.id.icon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val layout = R.layout.chat_item_teamorproject
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatHolder(view)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.nameChat.text = chats[position].name
        holder.cardView.setOnClickListener{
            if(chats[position].type == KEY_PROJECT){
                var bundle = Bundle()
                bundle.putString(KEY_TYPE,chats[position].type)
                bundle.putString(KEY_CHATS_NAME,chats[position].name)
                bundle.putString(KEY_PROJECT_ID,chats[position].id)
                APP_ACTIVITY.navController.navigate(R.id.action_chatList_to_chat,bundle)
            }
        }
        if (chats[position].type == KEY_TEAM) {
            holder.icon.setImageResource(R.drawable.ic_or_group)
        } else {
            holder.icon.setImageResource(R.drawable.ic_or_person)
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}
