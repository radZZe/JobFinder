package com.example.jobfinder.ui.chatManager.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentChatBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Chat : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    fun initialization(){

    }

}