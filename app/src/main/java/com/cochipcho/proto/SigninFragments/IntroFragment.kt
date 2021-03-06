package com.cochipcho.proto.SigninFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.cochipcho.proto.R
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        proceedButton_intro.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_introFragment_to_signinFragment)
        }


    }

}