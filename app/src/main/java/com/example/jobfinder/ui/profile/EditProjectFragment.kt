package com.example.jobfinder.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentEditProjectBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.KEY_CLICKED_PROJECT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProjectFragment : Fragment() {

    private var _binding: FragmentEditProjectBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var project: Project

    private val mViewModel: EditProjectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProjectBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        project = arguments?.get(KEY_CLICKED_PROJECT) as Project
        initialization()
    }

    private fun initialization() {
        val types = resources.getStringArray(R.array.types)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.types_dropdown_item, types)
        mBinding.tvType.setAdapter(arrayAdapter)
        mBinding.btnBack.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
        mBinding.tvType.setText(project.type)
        mBinding.etTitle.setText(project.title)
        mBinding.etDescription.setText(project.description)
        mBinding.etSkills.setText(project.skills)
        mBinding.etSalary.setText(project.salary)
        mBinding.btnApply.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_editProfileFragment_to_profileFragment)
            mViewModel.editProject(initProject())
        }
        mBinding.btnDelete.setOnClickListener {
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle("Delete project")
                .setMessage("You will not be able to recover this project's data")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                    mViewModel.deleteProject(project)
                    APP_ACTIVITY.navController.navigate(R.id.action_editProfileFragment_to_profileFragment)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                })
                .create()
                .show()

        }
    }

    private fun initProject(): Project {
        val type = mBinding.tvType.text.toString()
        val title = mBinding.etTitle.text.toString()
        val desc = mBinding.etDescription.text.toString()
        val skills = mBinding.etSkills.text.toString()
        val salary = mBinding.etSalary.text.toString()
        project.skills = skills
        project.salary = salary
        project.title = title
        project.description = desc
        project.type = type
        return project
    }
}