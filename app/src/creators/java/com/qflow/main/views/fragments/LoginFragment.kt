package com.qflow.main.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.qflow.main.R
import com.qflow.main.core.Failure
import com.qflow.main.core.ScreenState
import com.qflow.main.utils.enums.ValidationFailureType
import com.qflow.main.views.screenstates.LoginFragmentScreenState
import com.qflow.main.views.viewmodels.LoginViewModel
import kotlinx.android.synthetic.creators.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.testFeature()
        initializeListeners()
    }

    private fun initializeListeners() {
        initializeButtons()
        viewModel.screenState.observe(::getLifecycle, ::updateUI)
        viewModel.failure.observe(::getLifecycle, ::handleErrors)
    }

    private fun initializeButtons() {
        btn_signIn.setOnClickListener {
            val email = inputEmail.text.toString()
            val pass = inputPass.text.toString()
            viewModel.login(pass, email)
        }
        btn_signUp.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun handleErrors(failure: Failure?) {
        when (failure) {
            is Failure.ValidationFailure -> {
                //Remember to stop loading when you need to
                loadingComplete()
                when (failure.validationFailureType) {
                    ValidationFailureType.EMAIL_OR_PASSWORD_EMPTY -> {
                        Toast.makeText(
                            this.context, "Email or password empty", Toast.LENGTH_LONG
                        ).show()
                        this.context?.let { ContextCompat.getColor(it, R.color.errorRedColor) }
                            ?.let {
                                inputPass.background.setTint(it)
                                inputEmail.background.setTint(it)
                            }
                    }
                }
            }
            else -> {
                loadingComplete()
                Toast.makeText(
                    this.context,
                    getString(R.string.login_not_successful),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun updateUI(screenState: ScreenState<LoginFragmentScreenState>?) {
        when (screenState) {
            ScreenState.Loading -> {
                //Start loading when state is change to loading in ViewModel
                loading()
            }
            is ScreenState.Render -> renderScreenState(screenState.renderState)
        }
    }

    private fun renderScreenState(renderState: LoginFragmentScreenState) {
        loadingComplete()
        when (renderState) {
            is LoginFragmentScreenState.LoginSuccessful -> {
                view?.findNavController()?.navigate(
                    R.id.action_loginFragment_to_navigation_home
                )
            }
        }
    }


    private fun loading(){
        //Make sure you've added the loader to the view
        loading_bar.visibility = View.VISIBLE
    }

    private fun loadingComplete(){
        loading_bar.visibility = View.INVISIBLE
    }

}
