package com.cochipcho.proto.Auth

import com.cochipcho.proto.Services.UserData
import com.cochipcho.proto.ViewModels.SharedSignupViewModel
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.lang.Exception

object EmailAuth {


    private lateinit var auth: FirebaseAuth

    fun signup(credential: SharedSignupViewModel, completion: (Exception?) -> Unit) {
        auth = Firebase.auth

        val email = credential.email.value.toString()
        val password = credential.password.value.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                sendVerificationEmail(result) { exception ->
                    if (exception != null) {
                        completion(exception)
                    } else {
                        // verification email sent but not yet verified
                        // create user data here
                        val uid = result.user?.uid

                        if (result.user?.uid != null) {
                            UserData.createUserData(email, uid!!) { exception ->
                                if (exception != null) {
                                    completion(exception)
                                } else {
                                    completion(null)
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                completion(exception)
            }
    }


    fun login(email: String, password: String, completion: (Boolean, Exception?) -> Unit) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                Timber.i("succeeded")
                completion(true, null)
            }
        }
            .addOnFailureListener {
                Timber.i("Failure reason: ${it.localizedMessage}")
                completion(false, it)
            }
    }

    private fun sendVerificationEmail(result: AuthResult, completion: (Exception?) -> Unit) {
        val user = result.user

        if (user != null) {

            val url = "https://conchip-b1ba6.firebaseapp.com/?email=" + user.email
            val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl(url)
                .setIOSBundleId("com.cochipcho.conchip")
                // The default for this is populated with the current android package name.
                .setAndroidPackageName("com.cochipcho.proto", false, null)
                .build()

            user.sendEmailVerification(actionCodeSettings)
                .addOnSuccessListener {
                    Timber.i("verfication email sent")
                    completion(null)
                }
                .addOnFailureListener {
                    Timber.i("verification email sent failed")
                    completion(it)
                }
        }
    }

}