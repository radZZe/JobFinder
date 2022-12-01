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
    private lateinit var chatAdapter: ChatListAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentChatListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }


    override fun onStart() {
        super.onStart()
        loading(true)
        initialization()
    }

    fun initialization(){
        mViewModel.getChats {
            loading(false)
            chatAdapter = ChatListAdapter(it)
            mBinding.rvChat.adapter = chatAdapter
        }

    }

    fun loading(active:Boolean){
        if (active) {
            mBinding.loading.visibility = View.VISIBLE
            mBinding.rvChat.visibility = View.GONE
        } else {
            mBinding.loading.visibility = View.GONE
            mBinding.rvChat.visibility = View.VISIBLE
        }
    }

}