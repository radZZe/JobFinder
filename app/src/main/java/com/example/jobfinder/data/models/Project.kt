package com.example.jobfinder.data.models

import java.io.Serializable
import java.util.*

data class Project(
    val id: String = "",
    val createdAt: Date,
    val title: String = "",
    val description: String = "",
    val skills: String = "",
    val type: String = "",
    val state: Boolean = true,
    val creatorId: String = "",
    val salary: String = "",
    val employer: String = ""
): Serializable