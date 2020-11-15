package com.cochipcho.proto.ProfileFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cochipcho.proto.R
import com.cochipcho.proto.ViewModels.SharedSignupViewModel

class NicknameFragment : Fragment() {

    companion object {
        fun newInstance() = NicknameFragment()
    }

    private lateinit var viewModelShared: SharedSignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nickname, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelShared = ViewModelProvider(this).get(SharedSignupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}