package com.cochipcho.proto.Services

import com.cochipcho.proto.Models.User
import com.cochipcho.proto.Services.MyApplication.Companion.EMAIL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import timber.log.Timber
import java.lang.Exception

object UserData {

    val profileUpdate = userProfileChangeRequest {
        displayName = "이동국"
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

    fun createUserData(email: String, uid: String, completion: (Exception?) -> Unit) {
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

            var userData = User(
                username = "",
                fcmTokens = null,
                email = email,
                queriable = "",
                nickname = "",
                profileImageUrl = "",
                uid = uid,
                appleId = null,
                authMethod = EMAIL
            )

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