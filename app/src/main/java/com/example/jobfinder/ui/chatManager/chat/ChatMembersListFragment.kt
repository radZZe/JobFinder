package com.example.jobfinder.ui.chatManager.chat

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.jobfinder.data.models.ProjectMember
import com.example.jobfinder.data.models.TeamMember
import com.example.jobfinder.databinding.FragmentChatMembersListBinding
import com.example.jobfinder.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatMembersListFragment : Fragment() {

    private var _binding: FragmentChatMembersListBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: ChatMembersListFragmentViewModel by viewModels()

    private lateinit var chatMembers: ArrayList<ChatMember>
    private lateinit var chatName:String
    private lateinit var type:String
    private lateinit var teamId:String
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
        setupRecyclerView()
        if(type == KEY_PROJECT){
            mViewModel.getProjectChatMember(teamId){
                var chatMembers = convertProjectMember(it,teamId,type)
                mViewModel.chatMembersLiveData.value = chatMembers
            }
        }else if(type == KEY_TEAM){
            mViewModel.getTeamChatMember(teamId){
                var chatMembers = convertTeamMember(it,teamId,type)
                mViewModel.chatMembersLiveData.value = chatMembers
            }
        }
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
        adapter = ChatMembersListAdapter(membersArrayList,object : onDeleteMembersListener {
            override fun deleteMember(userId: String, chatId: String, type: String) {
                if(type == KEY_PROJECT){
                    mViewModel.delProjectChatMember(userId,chatId, onSuccess = {
                        var bundle = Bundle()
                        bundle.putString(KEY_TYPE, type)
                        bundle.putString(KEY_CHATS_NAME, chatName)
                        bundle.putString(KEY_TEAM_ID, teamId)
                        APP_ACTIVITY.navController.navigate(R.id.action_chatMembersListFragment_to_chat, bundle)
                    },
                    onFail = {
                        AlertDialog.Builder(APP_ACTIVITY)
                            .setTitle("Deletion error ")
                            .setMessage("deletion error, or you are not the creator of the project  ")
                            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                                dialogInterface.cancel()
                            })
                            .create()
                            .show()
                    })
                }else if (type==KEY_TEAM){
                    mViewModel.delTeamChatMember(userId,chatId, onSuccess = {
                        var bundle = Bundle()
                        bundle.putString(KEY_TYPE, type)
                        bundle.putString(KEY_CHATS_NAME, chatName)
                        bundle.putString(KEY_TEAM_ID, teamId)
                        APP_ACTIVITY.navController.navigate(R.id.action_chatMembersListFragment_to_chat, bundle)
                    },
                    onFail = {
                        AlertDialog.Builder(APP_ACTIVITY)
                            .setTitle("Deletion error ")
                            .setMessage("deletion error, or you are not the creator of the command ")
                            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                                dialogInterface.cancel()
                            })
                            .create()
                            .show()
                    })
                }
            }

        })
        rvMembersList.adapter = adapter
    }

    fun getData() {
        type = arguments?.get(KEY_TYPE) !! as String
        chatName = arguments?.get(KEY_CHATS_NAME) !! as String
        teamId = arguments?.get(KEY_TEAM_ID)!! as String
//        senderId = mViewModel.getSenderId()
    }
     fun convertProjectMember(list: ArrayList<ProjectMember>, projectId:String, type:String): ArrayList<ChatMember>{
        val chatMembers = ArrayList<ChatMember>()
        for (member in list) {
            val chatMember = ChatMember(id = member.id, name = member.owner, "",projectId,type)
            chatMembers.add(chatMember)
        }
        return chatMembers
    }

    fun convertTeamMember(list: ArrayList<TeamMember>, teamId:String, type:String): ArrayList<ChatMember>{
        val chatMembers = ArrayList<ChatMember>()
        for (member in list) {
            val chatMember = ChatMember(id = member.id, name = "${member.name} ${member.surname}", specialization = member.uni,teamId,type)
            chatMembers.add(chatMember)
        }
        return chatMembers
    }
}