package com.example.jobfinder.data.models

import java.io.Serializable
import java.util.*

data class Project(
    val id: String = "",
    val createdAt: Date = Date(),
    var title: String = "",
    var description: String = "",
    var skills: String = "",
    var type: String = "",
    val state: Boolean = true,
    val creatorId: String = "",
    var salary: String = "",
    val employer: String = ""

): Serializable