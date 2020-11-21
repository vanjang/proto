package com.cochipcho.conchip.SignupFragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.navigation.findNavController
import com.cochipcho.conchip.Auth.EmailAuth
import com.cochipcho.conchip.R
import com.cochipcho.conchip.ViewModels.SharedSignupViewModel
import kotlinx.android.synthetic.main.fragment_signup_password.*

class SignupPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = SignupPasswordFragment()
    }

    private lateinit var viewModel: SharedSignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedSignupViewModel::class.java)
        updateProceedButtonState()
        textChanged()
        proceed_to_verify_button_signup.setOnClickListener { proceedButtonTapped() }
    }

    private fun proceedButtonTapped() {
        EmailAuth.signup(viewModel) { error ->
            if (error != null) {
                Toast.makeText(requireActivity(), "Signup failed for reason: ${error.localizedMessage}!", LENGTH_LONG).show()
            } else {
                view?.findNavController()?.navigate(R.id.signupEmailVerificationFragment)
            }

        }
    }

    private fun updateProceedButtonState() {
        viewModel.password.value = password_upper_textfield_siginup.text.toString()
        viewModel.rePassword.value = password_lower_textfield_siginup.text.toString()

        val areTextsFilledIn = (!password_upper_textfield_siginup.text.isEmpty() && !password_lower_textfield_siginup.text.isEmpty())
        val arePasswordsMatching = viewModel.isQualifiedToSignup

        if (areTextsFilledIn && arePasswordsMatching) {
            proceed_to_verify_button_signup.isClickable = true
            proceed_to_verify_button_signup.setBackgroundColor(Color.BLUE)
        } else {
            proceed_to_verify_button_signup.isClickable = false
            proceed_to_verify_button_signup.setBackgroundColor(Color.LTGRAY)
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            updateProceedButtonState()
        }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }

    private fun textChanged() {
        password_upper_textfield_siginup.addTextChangedListener(textWatcher)
        password_lower_textfield_siginup.addTextChangedListener(textWatcher)
    }


}