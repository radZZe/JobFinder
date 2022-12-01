package com.example.jobfinder.ui.chatManager.chat

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jobfinder.R
import com.example.jobfinder.data.models.StudentTeam
import com.example.jobfinder.data.models.Team
import com.example.jobfinder.databinding.FragmentAddTeamBinding
import com.example.jobfinder.databinding.FragmentAddUserToTeamBinding
import com.example.jobfinder.databinding.FragmentChatBinding
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.KEY_CHATS_NAME
import com.example.jobfinder.utils.KEY_TEAM_ID
import com.example.jobfinder.utils.KEY_TYPE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserToTeamFragment : Fragment() {

    private var _binding: FragmentAddUserToTeamBinding? = null
    private val mBinding get() = _binding!!
    private  val mViewModel: ChatViewModel by viewModels()
    private lateinit var type:String
    private lateinit var teamName: String
    private lateinit var teamId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUserToTeamBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    fun initialization(){
        getData()
        setupClickListener()
    }

    fun getData(){
        type = arguments?.get(KEY_TYPE) !! as String
        teamName = arguments?.get(KEY_CHATS_NAME) !! as String
        teamId = arguments?.get(KEY_TEAM_ID)!! as String
    }

    fun setupClickListener(){
        var bundle = Bundle()
        bundle.putString(KEY_TYPE,type)
        bundle.putString(KEY_CHATS_NAME,teamName)
        bundle.putString(KEY_TEAM_ID,teamId)
        mBinding.addNewMemberBTN.setOnClickListener {
            var email = mBinding.memberEmailET.text.toString()
            mViewModel.addUserToTeam(email = email, teamId = teamId, teamName = teamName, onComplete = {
                mBinding.memberEmailET.text.clear()
                APP_ACTIVITY.navController.navigate(R.id.action_addUserToTeamFragment_to_chat,bundle)
            }, onFail = {
                AlertDialog.Builder(APP_ACTIVITY)
                    .setTitle("Failed to add a member")
                    .setMessage("adding error , or you have already added this user ")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                        dialogInterface.cancel()
                    })
                    .create()
                    .show()
            })
        }
        mBinding.backButton.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_addUserToTeamFragment_to_chat,bundle)
        }
    }
}