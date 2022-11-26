package com.example.jobfinder.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentMainScreenBinding
import com.example.jobfinder.databinding.FragmentProjectBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.KEY_ITEM
import com.example.jobfinder.utils.KEY_PROJECT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : Fragment() {

    private var _binding: FragmentProjectBinding? = null
    private val mBinding get() = _binding!!
    private val project = arguments?.get(KEY_ITEM) as Project

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mBinding.btnBack.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_projectFragment_to_mainScreenFragment2)
        }
        initFields()
    }

    private fun initFields() {
        mBinding.title.text = project.title.toString()
        mBinding.company.text = project.employer.toString()
        mBinding.salary.text = project.salary.toString()
        mBinding.description.text = project.description.toString()
        mBinding.sklls.text = project.skills.toString()
    }
}