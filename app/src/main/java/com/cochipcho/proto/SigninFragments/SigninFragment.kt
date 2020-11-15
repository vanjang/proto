package com.cochipcho.proto.SigninFragments

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.media.tv.TvContract.Programs.Genres.decode
import android.net.Uri
import android.net.Uri.decode
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil.decode
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.navigation.findNavController
import com.cochipcho.proto.Activities.MainTabBarActivity
import com.cochipcho.proto.Auth.AppleAuth
import com.cochipcho.proto.Auth.GoogleAuth
import com.cochipcho.proto.Auth.GoogleAuth.RC_SIGN_IN
import com.cochipcho.proto.Auth.GoogleAuth.googleSignin
import com.cochipcho.proto.R
import com.cochipcho.proto.ViewModels.SharedSigninViewModel
import com.google.android.gms.common.util.Base64Utils.decode
import com.google.android.gms.signin.SignInOptions.DEFAULT
import kotlinx.android.extensions.CacheImplementation.Companion.DEFAULT
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import timber.log.Timber
import java.io.OutputStreamWriter
import java.lang.Byte.decode
import java.lang.Integer.decode
import java.lang.Long.decode
import java.lang.Short.decode
import java.net.URL
import java.net.URLDecoder.decode
import java.util.*
import java.util.Calendar.LONG
import javax.net.ssl.HttpsURLConnection

class SigninFragment : Fragment() {

    companion object {
        fun newInstance() = SigninFragment()
    }

    private lateinit var viewModel: SharedSigninViewModel
    private val googleAuth = GoogleAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onStart() {
        super.onStart()
//        https://conchip-b1ba6.firebaseapp.com/__/auth/handler
    }


    lateinit var appleAuthURLFull: String
    lateinit var appledialog: Dialog
    lateinit var appleAuthCode: String
    lateinit var appleClientSecret: String

    var appleId = ""
    var appleFirstName = ""
    var appleMiddleName = ""
    var appleLastName = ""
    var appleEmail = ""
    var appleAccessToken = ""




    //    This is called after onCreateView and before onViewStateRestored(Bundle).
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        googleAuth.init(requireActivity())
        viewModel = ViewModelProvider(requireActivity()).get(SharedSigninViewModel::class.java)
        emailTextChanged()
        proceed_button_signin.setOnClickListener { proceedButtonTapped() }
        create_account_button_signin.setOnClickListener { proceedToSignupButtonTapped() }
        google_signin_button_signin.setOnClickListener { googleSigninButtonTapped() }



        val state = UUID.randomUUID().toString()

        appleAuthURLFull =
            AppleAuth.AUTHURL + "?client_id=" +
                    AppleAuth.CLIENT_ID + "&redirect_uri=" +
                    AppleAuth.REDIRECT_URI + "&response_type=code&scope=" +
                    AppleAuth.SCOPE + "&response_mode=form_post&state=" + state

        apple_sign_in_button_signin.setOnClickListener {
            setupAppleWebViewDialog(appleAuthURLFull)
        }

        CoroutineScope(Dispatchers.Default).launch {
            val results = CoroutineScope(Dispatchers.IO).async { isLoggedIn() }
            val result = results.await()
            if (result) {
                // Show the Activity with the logged in user
                Log.d("LoggedIn?: ", "YES")
            } else {
                // Show the Home Activity
                Log.d("LoggedIn?: ", "NO")
            }
        }



    }



    suspend fun isLoggedIn(): Boolean {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val expireTime = sharedPref.getLong("verify_refresh_token_timer", 0)

        val currentTime = System.currentTimeMillis() / 1000L // Check the current Unix Time

        return if (currentTime >= expireTime) {
            // After 24 hours validate the Refresh Token and generate new Access Token
            val untilUnixTime =
                currentTime + (60 * 60 * 24) // Execute the method after 24 hours again
            sharedPref.edit().putLong("verify_refresh_token_timer", untilUnixTime).apply()
            verifyRefreshToken()
        } else {
            true
        }
    }


    // Show 'Sign in with Apple' login page in a dialog
    @SuppressLint("SetJavaScriptEnabled")
    fun setupAppleWebViewDialog(url: String) {
        appledialog = Dialog(requireActivity())
        val webView = WebView(requireActivity())
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = AppleWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        appledialog.setContentView(webView)
        appledialog.show()
    }

    // A client to know about WebView navigation
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class AppleWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(AppleAuth.REDIRECT_URI)) {

                handleUrl(request?.url.toString())

                // Close the dialog after getting the authorization code
                if (request?.url.toString().contains("success=")) {
                    appledialog.dismiss()
                }
                return true
            }
            return true
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(AppleAuth.REDIRECT_URI)) {

                handleUrl(url)

                // Close the dialog after getting the authorization code
                if (url.contains("success=")) {
                    appledialog.dismiss()
                }
                return true
            }
            return false
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            // retrieve display dimensions
            val displayRectangle = Rect()
            val window = requireActivity().window
            window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

            // Set height of the Dialog to 90% of the screen
            val layoutParams = view?.layoutParams
            layoutParams?.height = (displayRectangle.height() * 0.9f).toInt()
            view?.layoutParams = layoutParams
        }

        // Check WebView url for access token code or error
        @SuppressLint("LongLogTag")
        private fun handleUrl(url: String) {

            val uri = Uri.parse(url)

            val success = uri.getQueryParameter("success")
            if (success == "true") {

                // Get the Authorization Code from the URL
                appleAuthCode = uri.getQueryParameter("code") ?: ""
                Log.i("Apple Code: ", appleAuthCode)

                // Get the Client Secret from the URL
                appleClientSecret = uri.getQueryParameter("client_secret") ?: ""
                Log.i("Apple Client Secret: ", appleClientSecret)

                // Save the Client Secret (appleClientSecret) using SharedPreferences
                // This will allow us to verify if refresh Token is valid every time they open the app after cold start.
                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                sharedPref.edit().putString("client_secret", appleClientSecret).apply()

                //Check if user gave access to the app for the first time by checking if the url contains their email
                if (url.contains("email")) {

                    //Get user's First Name
                    val firstName = uri.getQueryParameter("first_name")
                    Log.i("Apple User First Name: ", firstName ?: "")
                    appleFirstName = firstName ?: "Not exists"

                    //Get user's Middle Name
                    val middleName = uri.getQueryParameter("middle_name")
                    Log.i("Apple User Middle Name: ", middleName ?: "")
                    appleMiddleName = middleName ?: "Not exists"

                    //Get user's Last Name
                    val lastName = uri.getQueryParameter("last_name")
                    Log.i("Apple User Last Name: ", lastName ?: "")
                    appleLastName = lastName ?: "Not exists"

                    //Get user's email
                    val email = uri.getQueryParameter("email")
                    Log.i("Apple User Email: ", email ?: "Not exists")
                    appleEmail = email ?: ""
                }

                // Exchange the Auth Code for Access Token
                requestForAccessToken(appleAuthCode, appleClientSecret)
            } else if (success == "false") {
                Log.e("ERROR", "We couldn't get the Auth Code")
            }
        }
    }

    private fun requestForAccessToken(code: String, clientSecret: String) {

        val grantType = "authorization_code"

        val postParamsForAuth =
            "grant_type=" + grantType + "&code=" + code + "&redirect_uri=" + AppleAuth.REDIRECT_URI + "&client_id=" + AppleAuth.CLIENT_ID + "&client_secret=" + clientSecret

        CoroutineScope(Dispatchers.Default).launch {
            val httpsURLConnection =
                withContext(Dispatchers.IO) { URL(AppleAuth.TOKENURL).openConnection() as HttpsURLConnection }
            httpsURLConnection.requestMethod = "POST"
            httpsURLConnection.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
            httpsURLConnection.doInput = true
            httpsURLConnection.doOutput = true
            withContext(Dispatchers.IO) {
                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                outputStreamWriter.write(postParamsForAuth)
                outputStreamWriter.flush()
            }
            val response = httpsURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8

            val jsonObject = JSONTokener(response).nextValue() as JSONObject

            val accessToken = jsonObject.getString("access_token") // Here is the access token
            Log.i("Apple Access Token is: ", accessToken)
            appleAccessToken = accessToken

            val expiresIn = jsonObject.getInt("expires_in") // When the access token expires
            Log.i("expires in: ", expiresIn.toString())

            val refreshToken =
                jsonObject.getString("refresh_token") // The refresh token used to regenerate new access tokens. Store this token securely on your server.
            Log.i("refresh token: ", refreshToken)

            // Save the RefreshToken Token (refreshToken) using SharedPreferences
            // This will allow us to verify if refresh Token is valid every time they open the app after cold start.
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            sharedPref.edit().putString("refresh_token", refreshToken).apply()


            val idToken =
                jsonObject.getString("id_token") // A JSON Web Token that contains the user’s identity information.
            Log.i("ID Token: ", idToken)

            // Get encoded user id by splitting idToken and taking the 2nd piece
            val encodedUserID = idToken.split(".")[1]

            //Decode encodedUserID to JSON

            val decodedUserData = String(android.util.Base64.decode(encodedUserID, android.util.Base64.DEFAULT))
//            val decodedUserData = String(Base64.decode(encodedUserID, Base64.DEFAULT))
            val userDataJsonObject = JSONObject(decodedUserData)
            // Get User's ID
            val userId = userDataJsonObject.getString("sub")
            Log.i("Apple User ID :", userId)
            appleId = userId

            withContext(Dispatchers.Main) {
                openDetailsActivity()
            }
        }
    }

    private fun openDetailsActivity() {
        val myIntent = Intent(requireActivity(), MainTabBarActivity::class.java)
        myIntent.putExtra("apple_id", appleId)
        myIntent.putExtra("apple_first_name", appleFirstName)
        myIntent.putExtra("apple_middle_name", appleMiddleName)
        myIntent.putExtra("apple_last_name", appleLastName)
        myIntent.putExtra("apple_email", appleEmail)
        myIntent.putExtra("apple_access_token", appleAccessToken)
        startActivity(myIntent)
    }

    private suspend fun verifyRefreshToken(): Boolean {

        // Verify Refresh Token only once a day
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val refreshToken = sharedPref.getString("refresh_token", "")
        val clientSecret = sharedPref.getString("client_secret", "")

        val postParamsForAuth =
            "grant_type=refresh_token" + "&client_id=" + AppleAuth.CLIENT_ID + "&client_secret=" + clientSecret + "&refresh_token=" + refreshToken

        val httpsURLConnection =
            withContext(Dispatchers.IO) { URL(AppleAuth.TOKENURL).openConnection() as HttpsURLConnection }
        httpsURLConnection.requestMethod = "POST"
        httpsURLConnection.setRequestProperty(
            "Content-Type",
            "application/x-www-form-urlencoded"
        )
        httpsURLConnection.doInput = true
        httpsURLConnection.doOutput = true
        withContext(Dispatchers.IO) {
            val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
            outputStreamWriter.write(postParamsForAuth)
            outputStreamWriter.flush()

        }
        return try {
            val response = httpsURLConnection.inputStream.bufferedReader()
                .use { it.readText() }  // defaults to UTF-8
            val jsonObject = JSONTokener(response).nextValue() as JSONObject
            val newAccessToken = jsonObject.getString("access_token")
            //Replace the Access Token on your server with the new one
            Log.d("New Access Token: ", newAccessToken)
            true
        } catch (e: Exception) {
            Log.e(
                "ERROR: ",
                "Refresh Token has expired or user revoked app credentials"
            )
            false
        }
    }










    private fun googleSigninButtonTapped() {
        val signinIntent = googleAuth.signInClient.signInIntent
        startActivityForResult(signinIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSignin(requestCode, data) { isSignedIn ->
            if (isSignedIn) {
                Timber.i("Signed in")
                val intent = Intent(activity, MainTabBarActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(requireContext(), "Sign In Failed", LENGTH_LONG).show()
            }

        }
    }

    private fun proceedToSignupButtonTapped() {
        view?.findNavController()?.navigate(R.id.signupEmailFragment)
    }


    fun proceedButtonTapped() {
        val email = email_textfield_siginin.text.toString()
        viewModel.email.value = email
        view?.findNavController()?.navigate(R.id.signinPasswordFragment)
    }

    fun updateProceedButtonState() {
        if (email_textfield_siginin.text.isEmpty()) {
            proceed_button_signin.isClickable = false
            proceed_button_signin.setBackgroundColor(Color.LTGRAY)
        } else {
            proceed_button_signin.isClickable = true
            proceed_button_signin.setBackgroundColor(Color.BLUE)
        }
    }


    fun emailTextChanged() {
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
//class SigninFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = SigninFragment()
//    }
//
//    private lateinit var viewModel: SharedSigninViewModel
//    private val googleAuth = GoogleAuth
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_signin, container, false)
//    }
//
//    override fun onStart() {
//        super.onStart()
////        https://conchip-b1ba6.firebaseapp.com/__/auth/handler
//    }
//
//    //    This is called after onCreateView and before onViewStateRestored(Bundle).
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        googleAuth.init(requireActivity())
//        viewModel = ViewModelProvider(requireActivity()).get(SharedSigninViewModel::class.java)
//        emailTextChanged()
//        proceed_button_signin.setOnClickListener { proceedButtonTapped() }
//        create_account_button_signin.setOnClickListener { proceedToSignupButtonTapped() }
//        google_signin_button_signin.setOnClickListener { googleSigninButtonTapped() }
//    }
//
//    private fun googleSigninButtonTapped() {
//        val signinIntent = googleAuth.signInClient.signInIntent
//        startActivityForResult(signinIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        googleSignin(requestCode, data) { isSignedIn ->
//            if (isSignedIn) {
//                Timber.i("Signed in")
//                val intent = Intent(activity, MainTabBarActivity::class.java)
//                activity?.startActivity(intent)
//                activity?.finish()
//            } else {
//                Toast.makeText(requireContext(), "Sign In Failed", LENGTH_LONG).show()
//            }
//
//        }
//    }
//
//    private fun proceedToSignupButtonTapped() {
//        view?.findNavController()?.navigate(R.id.signupEmailFragment)
//    }
//
//
//    fun proceedButtonTapped() {
//        val email = email_textfield_siginin.text.toString()
//        viewModel.email.value = email
//        view?.findNavController()?.navigate(R.id.signinPasswordFragment)
//    }
//
//    fun updateProceedButtonState() {
//        if (email_textfield_siginin.text.isEmpty()) {
//            proceed_button_signin.isClickable = false
//            proceed_button_signin.setBackgroundColor(Color.LTGRAY)
//        } else {
//            proceed_button_signin.isClickable = true
//            proceed_button_signin.setBackgroundColor(Color.BLUE)
//        }
//    }
//
//
//    fun emailTextChanged() {
//        email_textfield_siginin.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                Timber.i("_afterTextChanged")
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//                Timber.i("_beforeTextChanged")
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                Timber.i("_onTextChanged")
//                updateProceedButtonState()
//            }
//
//        })
//    }
//
//
//}