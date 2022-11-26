package com.example.jobfinder.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.inflate
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.fragment.app.viewModels
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.Student
import com.example.jobfinder.databinding.ActivityMainBinding.inflate
import com.example.jobfinder.databinding.ChatItemContactBinding.inflate
import com.example.jobfinder.databinding.FragmentAddProjectBinding
import com.example.jobfinder.databinding.FragmentProfileBinding
import com.example.jobfinder.databinding.FragmentSignUpBinding
import com.example.jobfinder.utils.KEY_USER_ID
import com.example.jobfinder.utils.SNTPClient
import com.example.jobfinder.utils.generateItemId
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddProjectFragment : Fragment() {

    private var _binding: FragmentAddProjectBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: AddProjectFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProjectBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mBinding.btnApply.setOnClickListener {
            createProject()
        }
    }

    private fun createProject() {
        val id = generateItemId()
        val title = mBinding.etTitle.text.toString()
        val desc = mBinding.etDescription.text.toString()
        val skills = mBinding.etSkills.text.toString()
        val type = mBinding.tvType.text.toString()
        val state = true
        val creatorId = mViewModel.preferenceManager.getString(KEY_USER_ID).toString()
        val salary = mBinding.etSalary.text.toString()
        val employer = mViewModel.preferenceManager.getEmployer().companyName.toString()
        SNTPClient.getDate(
            TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString())
        ) { _, date, _ ->
            if (date != null) {
                val project = Project(
                    id = id,
                    createdAt = date,
                    title = title,
                    description = desc,
                    skills = skills,
                    type = type,
                    state = state,
                    creatorId = creatorId,
                    salary = salary,
                    employer = employer
                )
                mViewModel.addProject(project)
            }
        }
    }
}