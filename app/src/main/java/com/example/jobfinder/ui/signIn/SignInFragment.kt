package com.example.jobfinder.ui.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentSignInBinding
import com.example.jobfinder.ui.signUp.SignUpViewModel
import com.example.jobfinder.utils.APP_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    fun initialization(){
        setupListeners()
    }

    fun setupListeners(){
        with(mBinding){
            signInButton.setOnClickListener {
                var email = email.text.toString()
                var password = password.text.toString()
                mViewModel.login(email,password) {
                    APP_ACTIVITY.navController.navigate(R.id.action_signInFragment_to_mainScreenFragment2)
                }
            }
            signUpButton.setOnClickListener {
                APP_ACTIVITY.navController.navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
    }

}