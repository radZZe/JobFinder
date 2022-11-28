package com.example.jobfinder.utils

import java.util.*

fun generateItemId(): String {
    return UUID.randomUUID().toString()
}

fun getUnixTime(): String {
    return System.currentTimeMillis().toString()
}

fun isValidPassword(password: String): Boolean {
    var pattern = "(?=.*[a-z])(?=.*[A-Z]).{8,}";
    val regex = Regex(pattern)
    return password.matches(regex)
}