package com.example.jobfinder.ui.signUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentSignInBinding
import com.example.jobfinder.databinding.FragmentSignUpBinding
import com.example.jobfinder.ui.signIn.SignInViewModel
import com.example.jobfinder.utils.APP_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: SignUpViewModel by viewModels()

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
        setupListeners()
    }

    fun setupListeners(){
        mBinding.button.setOnClickListener {
            with(mBinding){
                if(checkBoxStudent.isChecked){
                    checkBoxEmployer.isChecked = false
                    startLayout.visibility = View.GONE
                    studentLayout.visibility = View.VISIBLE
                    employerLayout.visibility = View.GONE
                }else if(checkBoxEmployer.isChecked){
                    checkBoxEmployer.isChecked = false
                    startLayout.visibility = View.GONE
                    studentLayout.visibility = View.GONE
                    employerLayout.visibility = View.VISIBLE
                }
            }
        }
        mBinding.signUpAsStudent.setOnClickListener {
            with(mBinding){
                var name = nameField.text.toString()
                var surname = surnnameField.text.toString()
                var lastName = lastName.text.toString()
                var age = ageField.text.toString()
                var uni = uniField.text.toString()
                var email = emailField.text.toString()
                var password = passwordField.text.toString()
                var male = if(maleMan.isEnabled) "man" else "woman"
                mViewModel.signUpAsStudent(image = "jopa",name,surname,lastName,uni,male,age,email,password){
                    APP_ACTIVITY.navController.navigate(R.id.action_signUpFragment_to_mainScreenFragment2)
                }
            }
        }

        mBinding.signUpAsEmployer.setOnClickListener {
            with(mBinding){
                var name = nameEField.text.toString()
                var surname = surnnameEField.text.toString()
                var lastName = lastNameEField.text.toString()
                var age = ageFieldE.text.toString()
                var company = companyField.text.toString()
                var email = emailFieldE.text.toString()
                var password = passwordField3.text.toString()
                var male = if(maleMan.isEnabled) "man" else "woman"
                mViewModel.signUpAsEmployer(image = "jopa",name,surname,lastName,company,male,age,email,password){
                    APP_ACTIVITY.navController.navigate(R.id.action_signUpFragment_to_mainScreenFragment2)
                }
            }
        }
    }


}