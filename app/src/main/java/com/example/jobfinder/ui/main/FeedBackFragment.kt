package com.example.jobfinder.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentFeedBackBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.KEY_CLICKED_PROJECT
import com.example.jobfinder.utils.KEY_PROJECT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedBackFragment : Fragment() {
    private var _binding: FragmentFeedBackBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: FeedBackViewModel by viewModels()

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
            val bundle = Bundle()
            bundle.putSerializable(KEY_CLICKED_PROJECT, project)
            var brief = mBinding.editTextTextMultiLine.text.toString()
            mViewModel.sendFeedback(project, brief){
                AlertDialog.Builder(APP_ACTIVITY)
                    .setTitle("Failed to add a reply")
                    .setMessage("you have already sent a reply")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                        dialogInterface.cancel()
                    })
                    .create()
                    .show()
            }
            mBinding.editTextTextMultiLine.text.clear()
            APP_ACTIVITY.navController.navigate(R.id.action_feedBackFragment_to_projectFragment,bundle)
        }

        mBinding.btnBack.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(KEY_CLICKED_PROJECT, project)
            APP_ACTIVITY.navController.navigate(
                R.id.action_feedBackFragment_to_projectFragment,
                bundle
            )
        }
    }


}