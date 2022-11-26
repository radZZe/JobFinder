package com.example.jobfinder.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentFilterProjectsBinding
import com.example.jobfinder.databinding.FragmentMainScreenBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.IS_FILTERED
import com.example.jobfinder.utils.KEY_FILTER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterProjectsFragment : Fragment() {

    private var _binding: FragmentFilterProjectsBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterProjectsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        IS_FILTERED = false
        val types = resources.getStringArray(R.array.types)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.types_dropdown_item, types)
        mBinding.tvCategory.setAdapter(arrayAdapter)

        mBinding.btnApply.setOnClickListener {
            IS_FILTERED = true
            val bundle = Bundle()
            bundle.putSerializable(KEY_FILTER, mBinding.tvCategory.text.toString().capitalize())
            APP_ACTIVITY.navController.navigate(
                R.id.action_filterProjectsFragment_to_mainScreenFragment2,
                bundle
            )
        }

        mBinding.backButton.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_filterProjectsFragment_to_mainScreenFragment2)
        }
    }


}