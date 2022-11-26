package com.example.jobfinder.ui.main

import com.example.jobfinder.data.models.Project

interface ProjectListener {
    fun onProjectClicked(project: Project)
}