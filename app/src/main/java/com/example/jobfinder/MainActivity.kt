package com.example.jobfinder

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.jobfinder.databinding.ActivityMainBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        navController = Navigation.findNavController(this, R.id.nav_host)

        mBinding.navMenu.setupWithNavController(navController) // Setup navigation  menu

        val navView: BottomNavigationView = mBinding.navMenu
        NavigationUI.setupWithNavController(navView, navController)

        val fragment_login = navController.findDestination(R.id.signInFragment)
        val fragment_filter = navController.findDestination(R.id.filterProjectsFragment)
        val fragment_signUp = navController.findDestination(R.id.signUpFragment)
        val fragment_add_project = navController.findDestination(R.id.addProjectFragment)

        navController.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination, @Nullable arguments: Bundle?
            ) {
                navChangedListener(
                    fragment_login,
                    fragment_filter,
                    fragment_signUp,
                    fragment_add_project
                )
            }
        })
    }

    private fun navChangedListener(
        login: NavDestination?,
        signUp: NavDestination?,
        filter: NavDestination?,
        addProject: NavDestination?
    ) {
        val currentFragment = navController.currentDestination
        if (currentFragment != null) {
            if (currentFragment == login ||
                currentFragment == filter ||
                currentFragment == signUp ||
                currentFragment == addProject
            ) {
                mBinding.navMenu.visibility = View.GONE
            } else {
                mBinding.navMenu.visibility = View.VISIBLE
            }
        }
    }
}