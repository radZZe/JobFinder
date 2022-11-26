package com.example.jobfinder.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentFeedBackBinding
import com.example.jobfinder.databinding.FragmentProjectBinding
import com.example.jobfinder.utils.KEY_PROJECT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedBackFragment : Fragment() {
    private var _binding: FragmentFeedBackBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel:FeedBackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBackBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        var project = arguments?.get(KEY_PROJECT) as Project
        mBinding.btnSend.setOnClickListener {
            var brief = mBinding.editTextTextMultiLine.text.toString()
            mViewModel.sendFeedback(project,brief)
        }
    }


}