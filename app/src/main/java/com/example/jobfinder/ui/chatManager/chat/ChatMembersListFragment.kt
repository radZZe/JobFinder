package com.example.jobfinder.ui.chatManager.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.R
import com.example.jobfinder.data.models.ChatMember
import com.example.jobfinder.data.models.Project
import com.example.jobfinder.databinding.FragmentAddUserToTeamBinding
import com.example.jobfinder.databinding.FragmentChatMembersListBinding
import com.example.jobfinder.ui.main.MainListAdapter
import com.example.jobfinder.ui.main.MainScreenViewModel
import com.example.jobfinder.ui.main.ProjectListener
import com.example.jobfinder.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatMembersListFragment : Fragment() {

    private var _binding: FragmentChatMembersListBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: ChatMembersListFragmentViewModel by viewModels()

    private lateinit var chatName:String
    private lateinit var type:String
    private lateinit var teamId:String
//    private lateinit var senderId: String
    private lateinit var rvMembersList: RecyclerView
    private lateinit var adapter: ChatMembersListAdapter
    private lateinit var membersArrayList: ArrayList<ChatMember>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatMembersListBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        getData()
        mBinding.backButton.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(KEY_TYPE, type)
            bundle.putString(KEY_CHATS_NAME, chatName)
            bundle.putString(KEY_TEAM_ID, teamId)
            APP_ACTIVITY.navController.navigate(R.id.action_chatMembersListFragment_to_chat, bundle)
        }
        mViewModel.chatMembersLiveData.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }
        })
        if (type == KEY_TEAM) {
            mViewModel.getMembers { mBinding.chatMembersProgressBar.visibility = View.GONE }
        } else {
            mViewModel.getMembers { mBinding.chatMembersProgressBar.visibility = View.GONE }
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        rvMembersList = mBinding.rvChatMembers
        with(rvMembersList) {
            recycledViewPool.setMaxRecycledViews(
                ChatMembersListAdapter.VIEW_TYPE,
                ChatMembersListAdapter.MAX_POOL_SIZE
            )
            layoutManager = LinearLayoutManager(com.example.jobfinder.utils.APP_ACTIVITY)
            setHasFixedSize(true)
        }

        membersArrayList = arrayListOf()
        adapter = ChatMembersListAdapter(membersArrayList)
        rvMembersList.adapter = adapter
    }

    fun getData() {
        type = arguments?.get(KEY_TYPE) !! as String
        chatName = arguments?.get(KEY_CHATS_NAME) !! as String
        teamId = arguments?.get(KEY_TEAM_ID)!! as String
//        senderId = mViewModel.getSenderId()
    }
}