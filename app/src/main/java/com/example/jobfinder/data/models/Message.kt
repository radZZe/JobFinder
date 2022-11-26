package com.example.jobfinder.data.models

import java.util.*

data class Message(
    var id:String,
    var senderId:String,
    var text:String,
    var timestamp: Date,
    var owner:String,
)
