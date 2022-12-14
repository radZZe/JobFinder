package com.example.jobfinder.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentProfileBinding
import com.example.jobfinder.ui.main.MainListAdapter
import com.example.jobfinder.ui.main.ProjectListener
import com.example.jobfinder.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var itemsArrayList: ArrayList<Project>
    private lateinit var adapter: MainListAdapter
    private val mViewModel: ProfileFragmentViewModel by viewModels()
    private lateinit var rvProjects: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
        mViewModel.liveProjects.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }
        })
        if (mViewModel.preferenceManager.getString(KEY_USER_TYPE) == "student") {
            mViewModel.getEmployeeProjects(
                mViewModel.preferenceManager.getString(KEY_USER_ID).toString()
            ) { mBinding.progressBar.visibility = View.GONE }
        } else {
            mViewModel.getEmployerProjects(
                mViewModel.preferenceManager.getString(KEY_USER_ID).toString()
            ) { mBinding.progressBar.visibility = View.GONE }
        }

    }

    private fun setupRecyclerView() {
        rvProjects = mBinding.rvMyProjects
        with(rvProjects) {
            recycledViewPool.setMaxRecycledViews(
                MainListAdapter.VIEW_TYPE,
                MainListAdapter.MAX_POOL_SIZE
            )
            layoutManager = LinearLayoutManager(APP_ACTIVITY)
            setHasFixedSize(true)
        }

        itemsArrayList = arrayListOf()
        adapter = MainListAdapter(itemsArrayList, object : ProjectListener {

            override fun onProjectClicked(project: Project) {
                val bundle = Bundle()
                bundle.putSerializable(KEY_CLICKED_PROJECT, project)
                APP_ACTIVITY.navController.navigate(
                    R.id.action_profileFragment_to_listFeedbacksFragment,
                    bundle
                )
            }

            override fun onProjectLongClicked(project: Project) {
                val bundle = Bundle()
                bundle.putSerializable(KEY_CLICKED_PROJECT, project)
                APP_ACTIVITY.navController.navigate(
                    R.id.action_profileFragment_to_editProfileFragment,
                    bundle
                )
            }
        })
        rvProjects.adapter = adapter
    }

    private fun setupStudentsRecyclerView() {
        rvProjects = mBinding.rvMyProjects
        with(rvProjects) {
            recycledViewPool.setMaxRecycledViews(
                MainListAdapter.VIEW_TYPE,
                MainListAdapter.MAX_POOL_SIZE
            )
            layoutManager = LinearLayoutManager(APP_ACTIVITY)
            setHasFixedSize(true)
        }

        itemsArrayList = arrayListOf()
        adapter = MainListAdapter(itemsArrayList, object : ProjectListener {
            override fun onProjectClicked(project: Project) {
                val bundle = Bundle()
                bundle.putSerializable(KEY_ITEM, project)
            }

            override fun onProjectLongClicked(project: Project) {
                nothing()
            }
        })
        rvProjects.adapter = adapter
    }

    private fun initialization() {
        val userType = mViewModel.preferenceManager.getString(KEY_USER_TYPE)
        val name = mViewModel.preferenceManager.getString(KEY_USER_NAME)
        val surname = mViewModel.preferenceManager.getString(KEY_USER_SURNAME)
        val lastName = mViewModel.preferenceManager.getString(KEY_USER_LASTNAME)
        mBinding.userNameField.text = "$name $surname $lastName"
        mBinding.signOutBtn.setOnClickListener {
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle(getString(R.string.sign_out_message))
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                    mViewModel.signOut(){
                        APP_ACTIVITY.navController.navigate(R.id.action_profileFragment_to_signInFragment)
                    }
                })
                .setNegativeButton(getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                })
                .create()
                .show()

        }
        mBinding.userNameField.text = "$name $lastName $surname "
        if (userType == "student") {
            mBinding.specialization.text = mViewModel.preferenceManager.getString(KEY_USER_UNI)
            mBinding.btnAddProject.setOnClickListener {
                APP_ACTIVITY.navController.navigate(R.id.action_profileFragment_to_addTeamFragment)
            }
            mBinding.title.text = (APP_ACTIVITY.applicationContext.getString(R.string.projects_you_are_working_on))
            setupStudentsRecyclerView()
        } else {
            mBinding.specialization.text = mViewModel.preferenceManager.getString(KEY_USER_COMPANY)
            mBinding.btnAddProject.setOnClickListener {
                APP_ACTIVITY.navController.navigate(R.id.action_profileFragment_to_addProjectFragment)
            }
            setupRecyclerView()
            mBinding.title.text = (APP_ACTIVITY.applicationContext.getString(R.string.your_projects))
        }
    }


}