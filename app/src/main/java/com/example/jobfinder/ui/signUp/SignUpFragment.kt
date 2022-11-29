package com.example.jobfinder.ui.signUp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.data.models.Employer
import com.example.jobfinder.databinding.FragmentSignInBinding
import com.example.jobfinder.databinding.FragmentSignUpBinding
import com.example.jobfinder.ui.signIn.SignInViewModel
import com.example.jobfinder.utils.APP_ACTIVITY
import com.example.jobfinder.utils.KEY_REMEMBER
import com.example.jobfinder.utils.PreferenceManager
import com.example.jobfinder.utils.isValidPassword
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: SignUpViewModel by viewModels()
    @Inject  lateinit var manager: PreferenceManager

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
        loadingForStudent(false)
        loadingForEmployer(false)
        setupListeners()
    }

    fun setupListeners(){
        val types = resources.getStringArray(R.array.universities)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.types_dropdown_item, types)
        mBinding.uniField.setAdapter(arrayAdapter)

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
                loadingForStudent(true)
                var name = nameField.text.toString()
                var surname = surnnameField.text.toString()
                var lastName = lastName.text.toString()
                var age = ageField.text.toString()
                var uni = uniField.text.toString()
                var email = emailField.text.toString().trim()
                var password = passwordField.text.toString().trim()
                var male = if(maleMan.isEnabled) "man" else "woman"
                var confirmPassword = mBinding.confirmPasswordField.text.toString().trim()
                var isRemember = rememberMeStudent.isChecked
                if (isValidStudentData(name, surname, lastName, email, password, uni, male, confirmPassword, age)) {
                    mViewModel.signUpAsStudent(
                        image = "img",
                        name,
                        surname,
                        lastName,
                        uni,
                        male,
                        age,
                        email,
                        password
                    ) {
                        manager.putBoolean(KEY_REMEMBER,isRemember)
                        APP_ACTIVITY.navController.navigate(R.id.action_signUpFragment_to_mainScreenFragment2)
                    }
                }
            }
        }

        mBinding.signUpAsEmployer.setOnClickListener {
            with(mBinding){
                loadingForEmployer(true)
                var name = nameEField.text.toString()
                var surname = surnnameEField.text.toString()
                var lastName = lastNameEField.text.toString()
                var age = 0.toString()
                var company = companyField.text.toString()
                var email = emailFieldE.text.toString().trim()
                var password = passwordField3.text.toString().trim()
                var male = if(maleMan.isEnabled) "man" else "woman"
                var confirmPassword = mBinding.passwordField3Confirm2.text.toString().trim()
                var isRemember = mBinding.rememberMeEmployer.isChecked
                if (isValidEmployerData(name, surname, lastName, email, password, company, male, confirmPassword)) {
                    mViewModel.signUpAsEmployer(
                        image = "img",
                        name,
                        surname,
                        lastName,
                        company,
                        male,
                        age,
                        email,
                        password
                    ) {
                        manager.putBoolean(KEY_REMEMBER,isRemember)
                        APP_ACTIVITY.navController.navigate(R.id.action_signUpFragment_to_mainScreenFragment2)
                    }
                }
            }
        }
    }

    private fun isValidEmployerData(
        name: String,
        surname: String,
        lastName: String,
        email: String,
        password: String,
        companyName: String,
        sex: String,
        confirmPassword: String
    ): Boolean {
        if (
            name.isEmpty() ||
            surname.isEmpty() ||
            lastName.isEmpty() ||
            email.isEmpty() ||
            password.isEmpty() ||
            companyName.isEmpty() ||
            sex.isEmpty()
        ) {
            loadingForEmployer(false)
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle("Empty fields")
                .setMessage("Fill in all the fields")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                })
                .create()
                .show()
            return false
        }
        else if (password != confirmPassword) {
            loadingForEmployer(false)
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle("Invalid password")
                .setMessage("You should enter equal passwords")
                .setPositiveButton(
                    "Ok",
                    DialogInterface.OnClickListener { dialogInterface, _ ->
                        dialogInterface.cancel()
                    })
                .create()
                .show()
            return false
        } else if (!isValidPassword(password)) {
            loadingForEmployer(false)
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle("Invalid password")
                .setMessage("Your password should contain:\nUpper and lower case letters, numbers and special characters")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                })
                .create()
                .show()
            return false
        } else {
            return true
        }
    }

    private fun isValidStudentData(
        name: String,
        surname: String,
        lastName: String,
        email: String,
        password: String,
        university: String,
        sex: String,
        confirmPassword: String,
        age: String
    ): Boolean {
        if (
            name.isEmpty() ||
            surname.isEmpty() ||
            lastName.isEmpty() ||
            email.isEmpty() ||
            password.isEmpty() ||
            university.isEmpty() ||
            sex.isEmpty() ||
            age.isEmpty()
        ) {
            loadingForStudent(false)
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle("Empty fields")
                .setMessage("Fill in all the fields")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                })
                .create()
                .show()
            return false
        }
        else if (password != confirmPassword) {
            loadingForStudent(false)
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle("Invalid password")
                .setMessage("You should enter equal passwords")
                .setPositiveButton(
                    "Ok",
                    DialogInterface.OnClickListener { dialogInterface, _ ->
                        dialogInterface.cancel()
                    })
                .create()
                .show()
            return false
        } else if (!isValidPassword(password)) {
            loadingForStudent(false)
            AlertDialog.Builder(APP_ACTIVITY)
                .setTitle("Invalid password")
                .setMessage("Your password should contain:\nUpper and lower case letters, numbers")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                })
                .create()
                .show()
            return false
        } else {
            return true
        }
    }

    fun loadingForStudent(active: Boolean) {
        if (active) {
            mBinding.progressBarSignupStudent.visibility = View.VISIBLE
            mBinding.signUpAsStudent.visibility = View.GONE
        } else {
            mBinding.progressBarSignupStudent.visibility = View.GONE
            mBinding.signUpAsStudent.visibility = View.VISIBLE
        }
    }

    fun loadingForEmployer(active: Boolean) {
        if (active) {
            mBinding.progressBarSignupEmployer.visibility = View.VISIBLE
            mBinding.signUpAsEmployer.visibility = View.GONE
        } else {
            mBinding.progressBarSignupEmployer.visibility = View.GONE
            mBinding.signUpAsEmployer.visibility = View.VISIBLE
        }
    }

}