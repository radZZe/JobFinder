package com.example.jobfinder.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentAddTeamBinding
import com.example.jobfinder.databinding.FragmentProfileBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTeamFragment : Fragment() {

    private var _binding: FragmentAddTeamBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel:AddTeamViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTeamBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        initialization()
    }

    fun initialization(){
        setupClickListener()
    }

    fun setupClickListener(){
        mBinding.btnApply.setOnClickListener {
            var teamName = mBinding.etTeamName.text.toString()
            mViewModel.createTeam(teamName)
            mBinding.etTeamName.text.clear()
            APP_ACTIVITY.navController.navigate(R.id.action_addTeamFragment_to_profileFragment)
        }

        mBinding.backButton.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_addTeamFragment_to_profileFragment)
        }
    }


}