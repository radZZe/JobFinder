package com.example.jobfinder.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentMainScreenBinding
import com.example.jobfinder.databinding.FragmentProfileBinding
import com.example.jobfinder.ui.main.MainListAdapter
import com.example.jobfinder.ui.main.MainScreenViewModel
import com.example.jobfinder.utils.*


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var itemsArrayList: ArrayList<Project>
    private lateinit var adapter: MainListAdapter
    private val mViewModel: MainScreenViewModel by viewModels()
    private lateinit var rvShopList: RecyclerView

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

        mViewModel.getProjects { mBinding.progressBar.visibility = View.GONE }

    }

    private fun initialization() {
        val userType = mViewModel.preferenceManager.getString(KEY_USER_TYPE)
        val name = mViewModel.preferenceManager.getString(KEY_USER_NAME)
        val surname = mViewModel.preferenceManager.getString(KEY_USER_SURNAME)
        val lastName = mViewModel.preferenceManager.getString(KEY_USER_LASTNAME)
        mBinding.userNameField.text = "$name $surname $lastName"
        if (userType == "student") {
            mBinding.specialization.text = mViewModel.preferenceManager.getString(KEY_USER_UNI)
            mBinding.btnAddProject.visibility = View.GONE
            mBinding.rvMyProjects.visibility = View.GONE
        } else {
            mBinding.specialization.text = mViewModel.preferenceManager.getString(KEY_USER_COMPANY)
        }
    }


}