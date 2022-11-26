package com.example.jobfinder.utils

import java.util.*

fun generateItemId(): String {
    return UUID.randomUUID().toString()
}

fun getUnixTime(): String {
    return System.currentTimeMillis().toString()
}