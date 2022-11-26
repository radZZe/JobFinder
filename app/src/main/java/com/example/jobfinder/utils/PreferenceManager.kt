package com.example.jobfinder.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.data.models.Student

class PreferenceManager(sharedPreference: SharedPreferences) {
    var sharedPreference = sharedPreference
    var editor: SharedPreferences.Editor = sharedPreference.edit()

    fun putStudent(user: Student) {
        editor.putString(KEY_USER_ID, user.id)!!
        editor.putString(KEY_USER_PASSWORD, user.password)!!
        editor.putString(KEY_USER_EMAIL, user.email)!!
        editor.putString(KEY_USER_NAME, user.name)!!
        editor.putString(KEY_USER_SURNAME, user.surname)!!
        editor.putString(KEY_USER_LASTNAME, user.lastName)!!
        editor.putString(KEY_USER_AGE, user.age)!!
        editor.putString(KEY_USER_MALE, user.male)!!
        editor.putString(KEY_USER_IMAGE, user.image)!!
        editor.putString(KEY_USER_UNI, user.Uni)!!
        editor.putString(KEY_USER_TYPE,user.type)
        editor.apply()
    }

    fun getStudent(): Student {
        var id = getString(KEY_USER_ID)!!
        var password = getString(KEY_USER_PASSWORD)!!
        var email = getString(KEY_USER_EMAIL)!!
        var name = getString(KEY_USER_NAME)!!
        var surname = getString(KEY_USER_SURNAME)!!
        var lastName = getString(KEY_USER_LASTNAME)!!
        var age = getString(KEY_USER_AGE)!!
        var male = getString(KEY_USER_MALE)!!
        var image = getString(KEY_USER_IMAGE)!!
        var uni = getString(KEY_USER_UNI)!!
        var student = Student(
            id = id,
            email = email,
            password = password,
            null,
            male = male,
            Uni = uni,
            age = age,
            image = image,
            name = name,
            surname = surname,
            lastName = lastName
        )
        return student
    }

    fun putEmployer(user: Employer) {
        editor.putString(KEY_USER_ID, user.id)
        editor.putString(KEY_USER_PASSWORD, user.password)!!
        editor.putString(KEY_USER_EMAIL, user.email)!!
        editor.putString(KEY_USER_NAME, user.name)!!
        editor.putString(KEY_USER_SURNAME, user.surname)!!
        editor.putString(KEY_USER_LASTNAME, user.lastName)!!
        editor.putString(KEY_USER_AGE, user.age)!!
        editor.putString(KEY_USER_MALE, user.male)!!
        editor.putString(KEY_USER_IMAGE, user.image)!!
        editor.putString(KEY_USER_COMPANY, user.companyName)!!
        editor.putString(KEY_USER_TYPE,user.type)
        editor.apply()
    }

    fun getEmployer(): Employer {
        var id = getString(KEY_USER_ID)!!
        var password = getString(KEY_USER_PASSWORD)!!
        var email = getString(KEY_USER_EMAIL)!!
        var name = getString(KEY_USER_NAME)!!
        var surname = getString(KEY_USER_SURNAME)!!
        var lastName = getString(KEY_USER_LASTNAME)!!
        var age = getString(KEY_USER_AGE)!!
        var male = getString(KEY_USER_MALE)!!
        var image = getString(KEY_USER_IMAGE)!!
        var company = getString(KEY_USER_COMPANY)!!
        var employer = Employer(
            id = id,
            email = email,
            password = password,
            createdAt = null,
            companyName = company,
            male = male,
            age = age,
            image = image,
            name = name,
            surname = surname,
            lastName = lastName
        )
        return employer
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return sharedPreference.getString(key, null)
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

}