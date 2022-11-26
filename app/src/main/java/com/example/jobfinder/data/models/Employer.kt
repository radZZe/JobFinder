package com.example.jobfinder.data.models

import java.util.*

data class Employer(
    var id:String,
    var email:String,
    var password:String,
    var companyName:String,
    var createdAt: Date?,
    var male:String,
    var age:String,
    var image:String,
    var name:String,
    var surname:String,
    var lastName:String,
    var type:String? = "employer",
)
