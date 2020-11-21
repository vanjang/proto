package com.cochipcho.conchip.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedSignupViewModel : ViewModel() {

    //MARK:- Email and username properties
    var email = MutableLiveData<String>()
    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var rePassword = MutableLiveData<String>()
    var nickname = MutableLiveData<String>()
    var profileImageUrl = MutableLiveData<String>()

    val isQualifiedToSignup: Boolean
        get() {
            val pw = password.value.toString()
            val rePw = rePassword.value.toString()
            val arePwMatching = pw == rePw

            if (isValidPassword(pw) && arePwMatching) { return true }
            else { return false }
        }

    fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[\\?\\!@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    override fun onCleared() {
        super.onCleared()
    }


//    var isPasswordFormValid: Boolean {
//        return password?.isEmpty == false && rePassword?.isEmpty == false// && isPasswordMatching
//    }



//    var verifyingText: String? = ""


//    var email = MutableLiveData<String>()
//    var password = MutableLiveData<String>()
//
//    fun passEmail(passingEmail: String) {
//        email.value = passingEmail
//    }




//    var isEmailFormValid: Boolean {
//        guard let email = email, !email.isEmpty, let username = username, !username.isEmpty else { return false }
//        return true
//    }

    // MARK:- Nickname properties
//    var nickname: String? = ""

//    var isNicknameFormValid: Bool {
//        guard let nickname = self.nickname else { return false }
//        return !nickname.isEmpty
//    }



}