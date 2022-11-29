package com.example.jobfinder.ui.chatManager.chat

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
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
class Chat : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var senderId: String
    private val  mViewModel : ChatViewModel by viewModels()
    private lateinit var chatName:String
    private lateinit var type:String
    private lateinit var teamId:String
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
        setupClickListener()
    }

    fun setupClickListener() {
        mBinding.sendBtn.setOnClickListener {
            var text = mBinding.typeMessageField.text.toString()
            if(text.isNotBlank()){
                sendMessage(text)
                mBinding.typeMessageField.text.clear()

            }
        }
        mBinding.backButton.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_chat_to_chatList)
        }
        mBinding.addUserToTeam.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(KEY_TYPE,type)
            bundle.putString(KEY_CHATS_NAME,chatName)
            bundle.putString(KEY_TEAM_ID,teamId)
            APP_ACTIVITY.navController.navigate(R.id.action_chat_to_addUserToTeamFragment,bundle)

        }
        mBinding.receiverDataSeciton.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(KEY_TYPE, type)
            bundle.putString(KEY_CHATS_NAME, chatName)
            bundle.putString(KEY_TEAM_ID, teamId)
            APP_ACTIVITY.navController.navigate(R.id.action_chat_to_chatMembersListFragment, bundle)
        }
    }

    private fun setupListeners() {
        listenMessage()
    }

    fun getData() {
        type = arguments?.get(KEY_TYPE) !! as String
        chatName = arguments?.get(KEY_CHATS_NAME) !! as String
        teamId = arguments?.get(KEY_TEAM_ID)!! as String
        senderId = mViewModel.getSenderId()
    }

    fun initViews() {
        if(type == KEY_TEAM) {
            mBinding.addUserToTeam.visibility = View.VISIBLE
        }
        mBinding.userNameSurnameField.setText(chatName)
    }

    fun setupRecyclerView() {
        userChatAdpater = UserChatAdapter(senderId)
        mBinding.rvUserChat.adapter = userChatAdpater
    }

    fun listenMessage() {
        if(type == KEY_TEAM){
            var teamId = arguments?.get(KEY_TEAM_ID)!! as String
            mViewModel.getTeamMembersChat(teamId){
                mViewModel.listenMessageTeam(teamId = teamId, members = it){
                    userChatAdpater.updateList(ArrayList(it))
                    if (it.size != 0) {
                        mBinding.rvUserChat.smoothScrollToPosition(it.size - 1)
                    }
                }
            }
            mBinding.icon.setImageResource(R.drawable.ic_wh_group)
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
            mBinding.icon.setImageResource(R.drawable.ic_wh_person)
        }
    }

    fun sendMessage(text:String){
        if(type == KEY_TEAM){
            var teamId = arguments?.get(KEY_TEAM_ID)!! as String
            mViewModel.sendMessageForTeam(teamId,text)
        }else if (type == KEY_PROJECT){
            var projectId = arguments?.get(KEY_PROJECT_ID)!! as String
            mViewModel.sendMessageForProject(projectId,text)
        }
    }

}