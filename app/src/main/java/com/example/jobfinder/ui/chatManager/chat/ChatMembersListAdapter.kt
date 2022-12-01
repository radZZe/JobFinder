package com.example.jobfinder.ui.chatManager.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.ChatMember

class ChatMembersListAdapter(
    var members: ArrayList<ChatMember>,
    private val onDeleteMembersListener: onDeleteMembersListener
) : RecyclerView.Adapter<ChatMembersListAdapter.ChatMembersListViewHolder>() {

    class ChatMembersListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.member_name_field)
        val tvUni: TextView = view.findViewById(R.id.member_specialization)
        val delMemberBtn : ImageView = view.findViewById(R.id.delMemberBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMembersListViewHolder {
        val layout = R.layout.chat_member_card
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatMembersListAdapter.ChatMembersListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatMembersListViewHolder, position: Int) {
        val student = members[position]
        holder.tvName.text = student.name
        holder.tvUni.text = student.specialization
        holder.view.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_anim)
        )
        holder.delMemberBtn.setOnClickListener {
            var userId = members[position].id
            var chatId = members[position].chatId
            var type = members[position].type
            onDeleteMembersListener.deleteMember(userId,chatId,type)
        }
    }

    fun updateList(newItems: ArrayList<ChatMember>) {
        val oldList = members
        val newList = newItems
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ChatMembersListDiffCallback(oldList, newList)
        )
        members = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    companion object {
        const val VIEW_TYPE = 1
        const val MAX_POOL_SIZE = 20
    }
}