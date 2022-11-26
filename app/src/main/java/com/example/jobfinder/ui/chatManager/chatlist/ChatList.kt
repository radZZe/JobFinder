package com.example.jobfinder.ui.chatManager.chatlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentChatListBinding
import com.example.jobfinder.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatList : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: ChatListViewModel by viewModels()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentChatListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        initialization()
    }

    fun initialization(){

    }

}