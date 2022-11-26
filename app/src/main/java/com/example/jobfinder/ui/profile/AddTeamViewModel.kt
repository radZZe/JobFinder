package com.example.jobfinder.ui.profile

import androidx.lifecycle.ViewModel
import com.example.jobfinder.data.FirebaseRepository
import com.example.jobfinder.data.models.Team
import com.example.jobfinder.utils.KEY_USER_NAME
import com.example.jobfinder.utils.KEY_USER_SURNAME
import com.example.jobfinder.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTeamViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val manager:PreferenceManager
):ViewModel() {

    fun createTeam(teamName:String){
        var team = Team(
            id = UUID.randomUUID().mostSignificantBits.toString(),
            name = teamName,
            owner = "${manager.getString(KEY_USER_NAME)!!} ${manager.getString(KEY_USER_SURNAME)}"
        )
        firebaseRepository.createTeam(team)

    }
}