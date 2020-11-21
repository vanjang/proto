package com.cochipcho.conchip.SigninFragments

import android.annotation.SuppressLint
import android.content.Intent
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
import com.cochipcho.conchip.Activities.MainTabBarActivity
import com.cochipcho.conchip.Activities.ProfileNavHostActivity
import com.cochipcho.conchip.Auth.AppleAuth
import com.cochipcho.conchip.Auth.FBAuth.setupFbCallback
import com.cochipcho.conchip.Auth.GoogleAuth
import com.cochipcho.conchip.Auth.GoogleAuth.RC_SIGN_IN
import com.cochipcho.conchip.Auth.GoogleAuth.googleSignin
import com.cochipcho.conchip.Models.User
import com.cochipcho.conchip.R
import com.cochipcho.conchip.ViewModels.SharedSigninViewModel
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_signin.*
import timber.log.Timber
import java.lang.Exception

class SigninFragment : Fragment() {

    companion object {
        fun newInstance() = SigninFragment()
    }

    private lateinit var viewModel: SharedSigninViewModel
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Facebook SDK Callback handler
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Google SDK Callback handler
        googleSignin(requestCode, data) { userData, exception ->
            navigatePerResult(userData, exception)
        }
    }

    //    This is called after onCreateView and before onViewStateRestored(Bundle).
    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SharedSigninViewModel::class.java)

        GoogleAuth.init(requireActivity())
        AppleAuth.init()
        setupFbSigninCallback()

        emailTextChanged()

        proceed_button_signin.setOnClickListener { proceedButtonTapped() }
        create_account_button_signin.setOnClickListener { proceedToSignupButtonTapped() }
        google_signin_button_signin.setOnClickListener { googleSigninButtonTapped() }
        fb_signin_button_signin.setOnClickListener { fbSigninButtonTapped() }
        apple_sign_in_button_signin.setOnClickListener { appleSigninButtonTapped() }

    }


    // Facebook Callback handler
    private fun setupFbSigninCallback() {
        callbackManager = CallbackManager.Factory.create()
        setupFbCallback(callbackManager) { userData, exception ->
            navigatePerResult(userData, exception)
        }
    }

    // Implement FB sign-in flow
    private fun fbSigninButtonTapped() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
    }

    private fun appleSigninButtonTapped() {
        // spinner comes here
        AppleAuth.appleSignin(requireActivity()) { userData, exception ->
            // spinner disappears here
            navigatePerResult(userData, exception)
        }
    }

    private fun googleSigninButtonTapped() {
        val signinIntent = GoogleAuth.signInClient.signInIntent
        startActivityForResult(signinIntent, RC_SIGN_IN)
    }

    private fun navigatePerResult(userData: User?, exception: Exception?) {
        if (exception != null) {
            Toast.makeText(
                requireContext(),
                "Sign In Failed\nReason: ${exception.localizedMessage}",
                LENGTH_LONG
            ).show()
            return
        }

        Timber.i("navvvvv")

        if (userData != null) {
            val intent = Intent(activity, ProfileNavHostActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        } else {
            Timber.i("Signed in")
            val intent = Intent(activity, MainTabBarActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }
    }

    private fun proceedToSignupButtonTapped() {
        view?.findNavController()?.navigate(R.id.signupEmailFragment)
    }


    private fun proceedButtonTapped() {
        val email = email_textfield_siginin.text.toString()
        viewModel.email.value = email
        view?.findNavController()?.navigate(R.id.signinPasswordFragment)
    }

    private fun updateProceedButtonState() {
        if (email_textfield_siginin.text.isEmpty()) {
            proceed_button_signin.isClickable = false
            proceed_button_signin.setBackgroundColor(Color.LTGRAY)
        } else {
            proceed_button_signin.isClickable = true
            proceed_button_signin.setBackgroundColor(Color.BLUE)
        }
    }


    private fun emailTextChanged() {
        email_textfield_siginin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Timber.i("_afterTextChanged")
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                Timber.i("_beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Timber.i("_onTextChanged")
                updateProceedButtonState()
            }

        })
    }


}
