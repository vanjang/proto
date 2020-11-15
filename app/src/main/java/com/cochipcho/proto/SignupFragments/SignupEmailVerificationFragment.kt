package com.cochipcho.proto.SignupFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cochipcho.proto.R
import com.cochipcho.proto.ViewModels.SharedSignupViewModel

class SignupEmailVerificationFragment : Fragment() {

    companion object {
        fun newInstance() = SignupEmailVerificationFragment()
    }

    private lateinit var viewModelShared: SharedSignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup_email_verification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelShared = ViewModelProvider(this).get(SharedSignupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}