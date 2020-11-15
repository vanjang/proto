package com.cochipcho.proto.ProfileFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cochipcho.proto.R
import com.cochipcho.proto.ViewModels.SharedSignupViewModel

class InterestFragment : Fragment() {

    companion object {
        fun newInstance() = InterestFragment()
    }

    private lateinit var viewModel: SharedSignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_interest, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SharedSignupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}