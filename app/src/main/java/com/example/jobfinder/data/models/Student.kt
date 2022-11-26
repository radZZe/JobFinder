package com.example.jobfinder.data.models

import java.util.Date

data class Student(
    var id:String,
    var email:String,
    var password:String,
    var createdAt:Date?,
    var male:String,
    var Uni:String,
    var age:String,
    var image:String,
    var name:String,
    var surname:String,
    var lastName:String,
    var type:String? = "student",
)
