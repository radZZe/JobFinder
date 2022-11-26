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
<<<<<<< HEAD
    val employer: String = ""
=======
    val employer:String = ""
>>>>>>> af1e48ab76a40d235ba708bae18228b1770be10f
): Serializable