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

            }
        }
//        mBinding.backButton.setOnClickListener {
//            APP_ACTIVITY.navController.navigate(com.google.firebase.firestore.R.id.action_userChat_to_chatsFragment)
//        }
//        mBinding.attachBtn.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            pickImage.launch(intent)
//        }
//        mBinding.mapBtn.setOnClickListener {
//            var bundle = Bundle()
//            getСoordinates(bundle)
//        }
//        mBinding.receiverDataSeciton.setOnClickListener {
//            var bundle = Bundle()
//            bundle.putSerializable(KEY_USER, receiverUser)
//            APP_ACTIVITY.navController.navigate(com.google.firebase.firestore.R.id.action_userChat_to_sellerProfile, bundle)
//        }
    }

    private fun setupListeners() {
        listenMessage()
    }

    fun getData() {
        type = arguments?.get(KEY_TYPE) !! as String
        chatName = arguments?.get(KEY_CHATS_NAME) !! as String
        senderId = mViewModel.getSenderId()
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
            var teamId = arguments?.get(KEY_TEAM_ID)!! as String
            mViewModel.getTeamMembersChat(teamId){
                mViewModel.listenMessageTeam(teamId = teamId, members = it){
                    userChatAdpater.updateList(ArrayList(it))
                    if (it.size != 0) {
                        mBinding.rvUserChat.smoothScrollToPosition(it.size - 1)
                    }
                }
            }
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