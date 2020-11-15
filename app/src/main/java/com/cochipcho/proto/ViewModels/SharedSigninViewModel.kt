package com.cochipcho.proto.ViewModels

import android.content.ClipData
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedSigninViewModel : ViewModel() {
    // TODO: Implement the ViewModel
//    var email: String?
//
//
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
//
//    fun passEmail(passingEmail: String) {
//        email.value = passingEmail
//    }

    override fun onCleared() {
        super.onCleared()
    }


}