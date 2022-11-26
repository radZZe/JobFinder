package com.example.jobfinder.ui.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: SignInViewModel

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
        mViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
    }

}