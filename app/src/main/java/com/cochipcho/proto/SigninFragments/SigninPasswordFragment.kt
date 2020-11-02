package com.cochipcho.proto.SigninFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cochipcho.proto.Activities.MainTabBarActivity
import com.cochipcho.proto.Auth.EmailAuth
import com.cochipcho.proto.R
import com.cochipcho.proto.ViewModels.SigninPasswordViewModel
import com.cochipcho.proto.ViewModels.SharedSigninViewModel
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.android.synthetic.main.fragment_signinpassword.*

class SigninPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = SigninPasswordFragment()
    }

//    private lateinit var viewModel: SigninViewModel//SigninPasswordViewModel

    private val viewModel: SharedSigninViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        return inflater.inflate(R.layout.fragment_signinpassword, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SigninPasswordViewModel::class.java)
//        viewModel = ViewModelProvider(this).get(SigninViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun login() {
        val email = email_textfield_siginin.text.toString()
        val password = password_textfield_siginin.text.toString()
        EmailAuth.login(email, password) { isLoggedIn ->
            if (isLoggedIn) {
                val intent = Intent(getActivity(), MainTabBarActivity::class.java)
                getActivity()?.startActivity(intent)
            } else {

            }
        }
    }
}