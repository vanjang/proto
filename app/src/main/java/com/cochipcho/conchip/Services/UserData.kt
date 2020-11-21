package com.cochipcho.conchip.Services

import com.cochipcho.conchip.Models.User
import com.cochipcho.conchip.Services.MyApplication.Companion.EMAIL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.lang.Exception

object UserData {


//    private val user = Firebase.auth.currentUser


    private val user = Firebase.auth.currentUser

    fun authProvider() : String? {

//        EmailAuthProviderID: password
//        PhoneAuthProviderID: phone
//        GoogleAuthProviderID: google.com
//        FacebookAuthProviderID: facebook.com
//        TwitterAuthProviderID: twitter.com
//        GitHubAuthProviderID: github.com
//        AppleAuthProviderID: apple.com
//        YahooAuthProviderID: yahoo.com
//        MicrosoftAuthProviderID: hotmail.com

        if (user != null) {
            val provider = user.providerData

            for (user in provider) {
                val providerId = user.providerId
                if (providerId != "firebase") {
                    return providerId
                }
            }
        }
        return null
    }


    val profileUpdate = userProfileChangeRequest {
//        displayName = ""
    }

    fun updateProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updateProfile(profileUpdate)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        task.exception
                        return@addOnCompleteListener
                    }

                    Timber.i("display name: ${user.displayName}")
                }
        }


    }

    fun createUserData(userData: User, uid: String, completion: (Exception?) -> Unit) {
        val ref = MyApplication.REFERENCE_USER.child(uid)

        MessageService.getFcmTokens { token, exception ->
            if (exception != null) {
                completion(exception)
                return@getFcmTokens
            }
            // I will need device unique id
            // let appStateManager = AppStateManager()
            // let deviceId = appStateManager.deviceId()
            // For now I am using FirebaseInstanceId

            ref.setValue(userData)
                .addOnSuccessListener {
                    completion(null)
                }
                .addOnFailureListener { exception ->
                    completion(exception)
                }

        }
    }

}