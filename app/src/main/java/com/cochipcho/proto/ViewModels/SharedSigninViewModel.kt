package com.cochipcho.proto.ViewModels

import android.content.ClipData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedSigninViewModel : ViewModel() {
    // TODO: Implement the ViewModel
//    var email: String?
//
//
//    val selected = MutableLiveData<ClipData.Item>()
//
//    fun select(item: ClipData.Item) {
//        selected.value = item
//    }

    var email: String = ""
    var password: String = ""

    override fun onCleared() {
        super.onCleared()


    }


}