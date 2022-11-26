package com.example.jobfinder.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.UserFeedback

class FeedbacksDiffCallback (var oldList: ArrayList<UserFeedback>, var newList: ArrayList<UserFeedback>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].userId == newList[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}