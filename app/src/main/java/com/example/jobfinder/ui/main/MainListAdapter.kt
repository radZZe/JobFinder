package com.example.jobfinder.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import java.util.*
import kotlin.collections.ArrayList

class MainListAdapter(
    var projects: ArrayList<Project>,
    private val onProjectClickListener: ProjectListener
)  : RecyclerView.Adapter<MainListAdapter.MainListViewHolder>() {

    class MainListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.title)
        val tvCompany: TextView = view.findViewById(R.id.company)
        val tvSalary: TextView = view.findViewById(R.id.salary)
        val tvDescription: TextView = view.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val layout = R.layout.project_card
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MainListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val project = projects[position]
        holder.tvTitle.text = project.title.capitalize()
        holder.tvCompany.text = project.employer.capitalize()
        holder.tvSalary.text = project.salary.toString()
        holder.tvDescription.text = project.description
        holder.view.setOnClickListener {
            onProjectClickListener.onProjectClicked(project)
        }
        holder.view.setOnLongClickListener {
            onProjectClickListener.onProjectLongClicked(project)
            return@setOnLongClickListener true
        }
        holder.view.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.rv_anim)
        )
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    fun updateList(newItems: java.util.ArrayList<Project>) {
        val oldList = projects
        val newList = newItems
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            MainScreenDiffCallback(oldList, newList)
        )
        projects = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun getFilter(): Filter {
        return nameFilter
    }

    private val nameFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: java.util.ArrayList<Project> = java.util.ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                projects.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                projects.forEach {
                    if (it.title.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is java.util.ArrayList<*>) {
                val newItemsList = java.util.ArrayList<Project>()
                newItemsList.addAll(results.values as java.util.ArrayList<Project>)
                updateList(newItemsList)
            }
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return super.convertResultToString(resultValue)
        }
    }

    companion object {
        const val VIEW_TYPE = 1
        const val MAX_POOL_SIZE = 20
    }
}

