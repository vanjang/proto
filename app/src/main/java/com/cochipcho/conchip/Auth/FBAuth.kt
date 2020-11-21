package com.cochipcho.conchip.Auth

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.cochipcho.conchip.Helpers.signupDate
import com.cochipcho.conchip.Models.User
import com.cochipcho.conchip.Services.MyApplication
import com.cochipcho.conchip.Services.UserData
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import java.lang.Exception


object FBAuth {

    private lateinit var auth: FirebaseAuth
    var username: String = String()
    var email: String = String()
    var profileImageUrl: String? = null

    //    The Facebook Login SDK returns a long-lived token that lasts for about 60 days.
//    If the user opens your app within the day, Facebook automatically refreshes this token. This happens only once every day.
//    If the user hasn’t used your app for over 60 days, the token will expire and you have to log out the user from your app and ask them to re-login again to get a new access token.
    fun setupFbCallback(
        callbackManager: CallbackManager,
        completion: (User?, Exception?) -> Unit
    ) {
        auth = FirebaseAuth.getInstance()

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {

                override fun onSuccess(loginResult: LoginResult?) {
                    Timber.i("Login succeeded")
                    // Get User's Info
                    if (loginResult != null) {
                        val accessToken = loginResult.accessToken
                        val fbUserId = accessToken.userId
                        val credential = FacebookAuthProvider.getCredential(accessToken.token)

                        auth.signInWithCredential(credential)
                            .addOnSuccessListener { result ->

                                val additionalInfo = result.additionalUserInfo
                                val uid = result.user?.uid
                                val username = additionalInfo?.username
                                val email = result.user?.email

                                // Create user data only when it is a new user
                                if (additionalInfo != null &&
                                    uid != null &&
                                    additionalInfo.isNewUser
                                ) {
                                    // should get Graph info here
                                    getUserProfile(accessToken, fbUserId) { fbUserDetail ->

                                        var userData = User(
                                            username = fbUserDetail.username ?: "",
                                            fcm_tokens = null,
                                            email = fbUserDetail.email ?: "",
                                            queriable = fbUserDetail.username ?: "",
                                            nickname = "",
                                            profileImageUrl = "",
                                            uid = uid,
                                            appleId = null,
                                            authMethod = MyApplication.GOOGLE_AUTH,
                                            os = MyApplication.ANDROID_OS,
                                            signupDate = signupDate
                                        )
                                        UserData.createUserData(userData, uid) { exception ->
                                            fbSignout()
                                            if (exception != null) {
                                                completion(null, exception)
                                                return@createUserData
                                            }
                                            // Alway signs user out as soon as sign-in finishes so that the sign-in flow window pops up next time signing in
                                            completion(userData, null)
                                            return@createUserData
                                        }
                                    }
                                } else {
                                    fbSignout()
                                    completion(null, null)
                                }
                            }
                            .addOnFailureListener { exception ->
                                completion(null, exception)
                            }
                    }
                }

                override fun onCancel() {
                    Timber.i("Login cancelled")
                }

                override fun onError(exception: FacebookException) {
                    Timber.i("Login error occurred")
                    completion(null, exception)
                }
            })
    }

    fun fbSignout() {
        LoginManager.getInstance().logOut()
    }

    class FBUserDetail(var username: String = "", var profileImageUrl: String = "", var email: String = "")

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?, completion: (FBUserDetail) -> Unit) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                var userDetail = FBUserDetail()

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                // Facebook Id
                if (jsonObject.has("id")) {
                    val facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }


                // Facebook First Name
                if (jsonObject.has("first_name")) {
                    val facebookFirstName = jsonObject.getString("first_name")
                    Log.i("Facebook First Name: ", facebookFirstName)
                } else {
                    Log.i("Facebook First Name: ", "Not exists")
                }


                // Facebook Middle Name
                if (jsonObject.has("middle_name")) {
                    val facebookMiddleName = jsonObject.getString("middle_name")
                    Log.i("Facebook Middle Name: ", facebookMiddleName)
                } else {
                    Log.i("Facebook Middle Name: ", "Not exists")
                }


                // Facebook Last Name
                if (jsonObject.has("last_name")) {
                    val facebookLastName = jsonObject.getString("last_name")
                    Log.i("Facebook Last Name: ", facebookLastName)
                } else {
                    Log.i("Facebook Last Name: ", "Not exists")
                }


                // Facebook Name
                if (jsonObject.has("name")) {
                    val facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                        userDetail.username = facebookName
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }


                // Facebook Profile Pic URL
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                            userDetail.profileImageUrl = facebookProfilePicURL
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                }

                // Facebook Email
                if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                    userDetail.email = facebookEmail
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }

                completion(userDetail)
            }).executeAsync()
    }

}


