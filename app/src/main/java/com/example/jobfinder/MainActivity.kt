package com.example.jobfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatDelegate
=======
import androidx.navigation.NavController
import androidx.navigation.Navigation
>>>>>>> a3dff51068c073539f014efb19c122486190c96d
import com.example.jobfinder.utils.APP_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APP_ACTIVITY = this
<<<<<<< HEAD
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
=======
        navController = Navigation.findNavController(this, R.id.nav_host)

>>>>>>> a3dff51068c073539f014efb19c122486190c96d
    }
}