package com.example.jobfinder.ui.chatManager.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentChatBinding
import com.example.jobfinder.utils.*
import com.squareup.picasso.Picasso

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Chat @Inject constructor(
    private val manager:PreferenceManager
) : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var senderId: String
    private val  mViewModel : ChatViewModel by viewModels()
    private lateinit var chatName:String
    private lateinit var type:String
    private lateinit var userChatAdpater: UserChatAdapter

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
        getData()
        initViews()
        setupRecyclerView()
        setupListeners()
        //setupClickListener()
    }

    private fun setupListeners() {
        listenMessage()
    }

    fun getData() {
        type = arguments?.get(KEY_TYPE) !! as String
        chatName = arguments?.get(KEY_CHATS_NAME) !! as String
        senderId = manager.getString(KEY_USER_ID)!!
    }

    fun initViews() {
        mBinding.userNameSurnameField.setText(chatName)
    }

    fun setupRecyclerView() {
        userChatAdpater = UserChatAdapter(senderId)
        mBinding.rvUserChat.adapter = userChatAdpater
    }

    fun listenMessage() {
        if(type == KEY_TEAM){
            TODO()
        }else if(type == KEY_PROJECT){
            var projectId = arguments?.get(KEY_PROJECT_ID)!! as String
            mViewModel.getProjectMembersChat(projectId){
                mViewModel.listenMessageProject(projectId=projectId, members = it) {
                    userChatAdpater.updateList(ArrayList(it))
                    if (it.size != 0) {
                        mBinding.rvUserChat.smoothScrollToPosition(it.size - 1)
                    }
                }
            }

        }
    }

}