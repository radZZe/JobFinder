package com.example.jobfinder.ui.signIn

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.jobfinder.utils.KEY_REMEMBER
import com.example.jobfinder.utils.PreferenceManager
import com.example.jobfinder.utils.isValidPassword
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: SignInViewModel by viewModels()
    @Inject lateinit var manager:PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        isLoggedIn()
        loading(false)
        initialization()
    }

    fun initialization(){
        setupListeners()
    }

    fun setupListeners(){
        with(mBinding){
            signInButton.setOnClickListener {
                loading(true)
                var email = email.text.toString()
                var password = password.text.toString()
                var isRemember = rememberMeCb.isChecked
                if(email.isNotEmpty() && password.isNotEmpty()){
                    mViewModel.login(email,password, onComplete = {
                        manager.putBoolean(KEY_REMEMBER,isRemember)
                        APP_ACTIVITY.navController.navigate(R.id.action_signInFragment_to_mainScreenFragment2)
                    }, onFail = {
                        loading(false)
                    })
                }else{
                    loading(false)
                    AlertDialog.Builder(APP_ACTIVITY)
                        .setTitle("failed to log in")
                        .setMessage("Wrong email or password")
                        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                            dialogInterface.cancel()
                        })
                        .create()
                        .show()
                }

            }
            signUpButton.setOnClickListener {
                APP_ACTIVITY.navController.navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
    }

    fun isLoggedIn(){
        if(manager.getBoolean(KEY_REMEMBER)){
            APP_ACTIVITY.navController.navigate(R.id.action_signInFragment_to_mainScreenFragment2)
        }
    }

    fun loading(active: Boolean) {
        if (active) {
            mBinding.progressBarSignin.visibility = View.VISIBLE
            mBinding.signInButton.visibility = View.GONE
        } else {
            mBinding.progressBarSignin.visibility = View.GONE
            mBinding.signInButton.visibility = View.VISIBLE
        }
    }

}