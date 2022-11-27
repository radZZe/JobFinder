package com.example.jobfinder.ui.main

import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.UserFeedback

interface onFeedbackListener {

    fun onFeedbackAccepted(project: Project,userId:String)

    fun onFeedbackRejected(project: Project,userId:String)
}