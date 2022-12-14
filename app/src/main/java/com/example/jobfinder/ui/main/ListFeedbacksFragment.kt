package com.example.jobfinder.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.data.models.UserFeedback
import com.example.jobfinder.databinding.FragmentListFeedbacksBinding
import com.example.jobfinder.databinding.FragmentProfileBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.KEY_CLICKED_PROJECT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFeedbacksFragment : Fragment() {

    private var _binding: FragmentListFeedbacksBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: ListFeedbacksFragmentViewModel by viewModels()

    private lateinit var itemsArrayList: ArrayList<UserFeedback>
    private lateinit var adapter: ListFeedbacksAdapter
    private lateinit var rvFeedbacks: RecyclerView
    private lateinit var project: Project

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListFeedbacksBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        project = arguments?.get(KEY_CLICKED_PROJECT) as Project
        initialization()
    }

    private fun setupRecyclerView() {
//        mViewModel.liveFeedbacks.value = arrayListOf()
        mViewModel.getFeedbacks(project){
            rvFeedbacks = mBinding.rvFeedbacks
            with(rvFeedbacks) {
                recycledViewPool.setMaxRecycledViews(
                    ListFeedbacksAdapter.VIEW_TYPE,
                    ListFeedbacksAdapter.MAX_POOL_SIZE
                )
                layoutManager = LinearLayoutManager(com.example.jobfinder.utils.APP_ACTIVITY)
                setHasFixedSize(true)
            }
            adapter = ListFeedbacksAdapter(it, project, object : onFeedbackListener {

                override fun onFeedbackAccepted(project: Project,userId:String) {
                    mViewModel.acceptFeedback(project,userId){
                        AlertDialog.Builder(APP_ACTIVITY)
                            .setTitle("Failed to add a member")
                            .setMessage("member has already been added")
                            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                                dialogInterface.cancel()
                            })
                            .create()
                            .show()
                    }
                }

                override fun onFeedbackRejected(project: Project,userId:String) {
                    mViewModel.rejectFeedback(project,userId)
                }
            })
            rvFeedbacks.adapter = adapter
        }




    }

    fun initialization() {
        setupRecyclerView()
        val bundle = Bundle()
        bundle.putSerializable(KEY_CLICKED_PROJECT, project)
        mBinding.btnBack.setOnClickListener {
            APP_ACTIVITY.navController.navigate(
                R.id.action_listFeedbacksFragment_to_profileFragment,
                bundle
            )
        }
    }
}