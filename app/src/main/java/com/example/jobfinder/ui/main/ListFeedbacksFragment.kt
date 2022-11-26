package com.example.jobfinder.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentListFeedbacksBinding
import com.example.jobfinder.databinding.FragmentProfileBinding

class ListFeedbacksFragment : Fragment() {

    private var _binding: FragmentListFeedbacksBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListFeedbacksBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }
}