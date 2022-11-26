package com.example.jobfinder.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.example.jobfinder.data.models.Project

class MainScreenDiffCallback(var oldList: ArrayList<Project>, var newList: ArrayList<Project>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}