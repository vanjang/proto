package com.cochipcho.conchip.Auth

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cochipcho.conchip.Activities.MainTabBarActivity
import com.cochipcho.conchip.Helpers.signupDate
import com.cochipcho.conchip.Models.User
import com.cochipcho.conchip.Services.MyApplication
import com.cochipcho.conchip.Services.UserData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import timber.log.Timber
import java.io.OutputStreamWriter
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

object AppleAuth {

    val auth = Firebase.auth
    lateinit var provider: OAuthProvider.Builder

    fun init() {
        provider = OAuthProvider.newBuilder("apple.com")
        provider.setScopes(listOf("email", "name"))
    }

    fun appleSignin(activity: Activity, completion: (User?, Exception?) -> Unit) {
        // pendingAuthResult - sign in 하는 동안에 크롬탭이 열리면서 앱이 백그라운드로 가는데,
        // 시스템의 메모리 관리에 따라, 필요에 의해 앱이 clean up(terminate) 될 수 있다.
        // 이 경우 사용자가 다시 로그인 하는 것을 방지하기 위해 pending result 를 갖고 있는 것이다
        val pending = auth.pendingAuthResult
        if (pending != null) {
            pending.addOnSuccessListener { authResult ->
                navigatePerResult(authResult) { user, error ->
                    if (error != null) {
                        completion(null, error)
                        return@navigatePerResult
                    }
                    completion(user, null)
                }
            }.addOnFailureListener { exception ->
                completion(null, exception)
            }
        } else {
            auth.firebaseAuthSettings
            auth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener { authResult ->
                    navigatePerResult(authResult) { user, error ->
                        if (error != null) {
                            completion(null, error)
                            return@navigatePerResult
                        }
                        completion(user, null)
                    }
                }
                .addOnFailureListener { exception ->
                    completion(null, exception)
                }
        }
    }

    private fun navigatePerResult(authResult: AuthResult, completion: (User?, Exception?) -> Unit) {
        val credential = authResult.credential
        val user = authResult.user
        val uid = user?.uid
        val email = authResult.additionalUserInfo?.profile?.get("email")// as? String
        val username = authResult.user?.displayName// as? String
        val isNewUser = authResult.additionalUserInfo?.isNewUser

        Timber.i("email: ${email}")
        Timber.i("email: ${username}")

        // Reauthenticate user to check if user is existing or new user. Also check the username's nullability to check if user is returning from iOS.
        if (user != null && credential != null && email != null && uid != null && isNewUser != null) {
            if (isNewUser) {
                var userData = User(
                    username = username as? String ?: "",
                    fcm_tokens = null,
                    email = email as String,
                    queriable = username as? String ?: "",
                    nickname = "",
                    profileImageUrl = "",
                    uid = uid,
                    appleId = null,
                    authMethod = MyApplication.APPLE_AUTH,
                    os = MyApplication.ANDROID_OS,
                    signupDate = signupDate
                )

                UserData.createUserData(userData, uid) { exception ->
                    if (exception != null) {
                        completion(null, exception)
                        return@createUserData
                    }
                    completion(userData, null)
                }
            } else {
                // existing user
                Timber.i("it is an existing user")
                completion(null, null)
            }
        }
    }

}