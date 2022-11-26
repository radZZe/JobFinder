package com.example.jobfinder.data.models

import java.util.*

data class Message(
    var id:String,
    var receiverId:String,
    var text:String,
    var timestamp: Date,
    var type:String,
)
