package com.cochipcho.conchip.Models

class User(
    val username: String,
    val fcm_tokens: HashMap<String, Any>?,
    val email: String,
    val queriable: String,
    val nickname: String,
    val profileImageUrl: String,
    val uid: String,
    val authMethod: String?,
    val appleId: String?,
    val os: String,
    val signupDate: Int
) {
}
