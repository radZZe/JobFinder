package com.example.jobfinder.ui.chatManager.chat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.number.NumberFormatter.with
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Message
import com.example.jobfinder.utils.RECEIVED_TEXT
import com.example.jobfinder.utils.SENT_TEXT
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.time.format.DateTimeFormatter
import java.util.*

import kotlin.collections.ArrayList

class UserChatAdapter( senderId: String) :
    RecyclerView.Adapter<UserChatAdapter.UserChatViewHolder>() {

    var messages = arrayListOf<com.example.jobfinder.data.models.Message>()
    var senderID = senderId


    open class UserChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        @RequiresApi(Build.VERSION_CODES.N)
        private fun getReadableDateTime(date: Date): String {
            return SimpleDateFormat("MMMM dd - hh:mm a").format(date)
        }


        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(
            type: Int,
            view: View,
            position: Int,
            messages: List<Message>,
        ) {
            if (type == SENT_TEXT) {
                SentTextMessageViewHolder(view).message.text = messages[position].text
                SentTextMessageViewHolder(view).dateTime.text =
                    getReadableDateTime(messages.get(position).timestamp!!)
            } else if (type == RECEIVED_TEXT) {
                ReceivedTextMessageViewHolder(view).message.text = messages.get(position).text
                ReceivedTextMessageViewHolder(view).dateTime.text =
                    getReadableDateTime(messages.get(position).timestamp!!)
                ReceivedTextMessageViewHolder(view).owner.text = messages.get(position).owner
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatViewHolder {
        lateinit var viewHolder: UserChatViewHolder
        if (viewType == SENT_TEXT) {
            val layout = R.layout.item_container_sent_message
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            viewHolder = SentTextMessageViewHolder(view)
            SentTextMessageViewHolder(view).itemView.setOnClickListener {

            }
        } else if (viewType == RECEIVED_TEXT) {
            val layout = R.layout.item_container_received_message
            val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            viewHolder = ReceivedTextMessageViewHolder(view)
        }
        return viewHolder
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {

        holder.bind(
            holder.itemViewType,
            holder.itemView,
            position,
            messages,
        )

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun updateList(newMessages: ArrayList<Message>) {
        val oldList = messages
        val newList = newMessages
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            UserChatDiffCallBack(oldList, newList)
        )
        diffResult.dispatchUpdatesTo(this)
        messages = newMessages
    }


    override fun getItemViewType(position: Int): Int {
        if (senderID == messages.get(position).senderId) {
            return  SENT_TEXT
        } else {
            return RECEIVED_TEXT
        }

    }

}