package com.cochipcho.proto.SigninFragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.ViewModelProvider
import com.cochipcho.proto.Activities.MainTabBarActivity
import com.cochipcho.proto.Auth.EmailAuth
import com.cochipcho.proto.R
import com.cochipcho.proto.ViewModels.SharedSigninViewModel
import kotlinx.android.synthetic.main.fragment_signinpassword.*
import timber.log.Timber

class SigninPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = SigninPasswordFragment()
    }

    private lateinit var viewModel: SharedSigninViewModel// by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signinpassword, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedSigninViewModel::class.java)

        passwordTextChanged()
        login_button_signin.setOnClickListener { login() }
    }

    fun login() {
        val email = viewModel.email.value.toString()
        val password = password_textfield_siginin.text.toString()

        EmailAuth.login(email, password) { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(getActivity(), MainTabBarActivity::class.java)
                getActivity()?.startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(requireActivity(), "Login failed!", LENGTH_LONG).show()
            }
        }
    }

    fun passwordTextChanged() {
        password_textfield_siginin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateProceedButtonState()
            }

        })
    }

    fun updateProceedButtonState() {
        login_button_signin.isClickable = !(password_textfield_siginin.text.isEmpty())
        if (login_button_signin.isClickable) {
            login_button_signin.setBackgroundColor(Color.BLUE)
        } else {
            login_button_signin.setBackgroundColor(Color.LTGRAY)
        }
    }


}