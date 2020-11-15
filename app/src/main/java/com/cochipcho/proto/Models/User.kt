package com.cochipcho.proto.Models

class User(val username: String,
           val fcmTokens: HashMap<String, Any>?,
           val email: String,
           val queriable: String,
           val nickname: String,
           val profileImageUrl: String,
           val uid: String,
           val authMethod: String?,
           var appleId: String?) {
}
