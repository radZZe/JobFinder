package com.example.jobfinder.ui.main

import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.UserFeedback

interface onFeedbackListener {

    fun onFeedbackAccepted(project: Project)

    fun onFeedbackRejected(project: Project)
}