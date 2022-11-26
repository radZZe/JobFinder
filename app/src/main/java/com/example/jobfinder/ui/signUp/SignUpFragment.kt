package com.example.jobfinder.ui.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentSignInBinding
import com.example.jobfinder.databinding.FragmentSignUpBinding
import com.example.jobfinder.ui.signIn.SignInViewModel

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    fun initialization(){
        mViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
    }


}