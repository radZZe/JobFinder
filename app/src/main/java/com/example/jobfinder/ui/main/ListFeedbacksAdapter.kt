package com.example.jobfinder.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.UserFeedback

class ListFeedbacksAdapter(
    var feedbacks: ArrayList<UserFeedback>,
    val project: Project,
    private val onFeedbackClickListener: onFeedbackListener
) : RecyclerView.Adapter<ListFeedbacksAdapter.ListFeedbacksViewHolder>() {

    class ListFeedbacksViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.name)
        val tvAge: TextView = view.findViewById(R.id.age)
        val tvGender: TextView = view.findViewById(R.id.gender)
        val tvUni: TextView = view.findViewById(R.id.unic)
        val tvBrief: TextView = view.findViewById(R.id.description)
        val btnReject: Button = view.findViewById(R.id.reject)
        val btnAccept: Button = view.findViewById(R.id.accept)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFeedbacksViewHolder {
        val layout = R.layout.feedback_card
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ListFeedbacksViewHolder(view)
    }

//    fun updateList(newItems: java.util.ArrayList<UserFeedback>) {
//        val oldList = feedbacks
//        val newList = newItems
//        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
//            FeedbacksDiffCallback(oldList, newList)
//        )
//        feedbacks = newItems
//        diffResult.dispatchUpdatesTo(this)
//    }

    override fun onBindViewHolder(holder: ListFeedbacksViewHolder, position: Int) {
        val fb = feedbacks[position]
        holder.tvName.text = "${fb.name} ${fb.lastName} ${fb.surname}"
        holder.tvAge.text = fb.age
        holder.tvGender.text = fb.sex
        holder.tvUni.text = fb.uni
        holder.tvBrief.text = fb.brief
        holder.btnReject.setOnClickListener {
            onFeedbackClickListener.onFeedbackRejected(project,fb.userId)
        }
        holder.btnAccept.setOnClickListener {
            onFeedbackClickListener.onFeedbackAccepted(project,fb.userId)
        }
    }

    override fun getItemCount(): Int {
        return feedbacks.size
    }

    companion object {
        const val VIEW_TYPE = 1
        const val MAX_POOL_SIZE = 20
    }
}