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
import com.example.jobfinder.utils.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    val mBinding get() = _binding!!
    @Inject
    lateinit var manager: PreferenceManager

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
        val fragment_chat = navController.findDestination(R.id.chat)
        val fragment_project = navController.findDestination(R.id.projectFragment)
        val fragment_list_fb = navController.findDestination(R.id.listFeedbacksFragment)
        val fragment_fb = navController.findDestination(R.id.feedBackFragment)
        val fragment_create_team = navController.findDestination(R.id.addTeamFragment)
        val fragment_chat_members = navController.findDestination(R.id.chatMembersListFragment)

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
                    fragment_add_project,
                    fragment_chat,
                    fragment_project,
                    fragment_list_fb,
                    fragment_fb,
                    fragment_create_team,
                    fragment_chat_members
                )
            }
        })
    }

    private fun navChangedListener(
        login: NavDestination?,
        signUp: NavDestination?,
        filter: NavDestination?,
        addProject: NavDestination?,
        chat: NavDestination?,
        project: NavDestination?,
        fb: NavDestination?,
        f_fb: NavDestination?,
        createTeam: NavDestination?,
        chatMembers: NavDestination?
    ) {
        val currentFragment = navController.currentDestination
        if (currentFragment != null) {
            if (currentFragment == login ||
                currentFragment == filter ||
                currentFragment == signUp ||
                currentFragment == addProject ||
                currentFragment == chat ||
                currentFragment == project ||
                currentFragment == fb ||
                currentFragment == f_fb ||
                currentFragment == createTeam ||
                currentFragment == chatMembers
            ) {
                mBinding.navMenu.visibility = View.GONE
            } else {
                mBinding.navMenu.visibility = View.VISIBLE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        manager.clearWithoutRemember()
    }
}