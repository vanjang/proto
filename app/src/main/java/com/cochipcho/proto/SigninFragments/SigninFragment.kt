package com.cochipcho.proto.SigninFragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.cochipcho.proto.R
import com.cochipcho.proto.ViewModels.SharedSigninViewModel
import kotlinx.android.synthetic.main.fragment_signin.*
import timber.log.Timber

class SigninFragment : Fragment() {

    companion object {
        fun newInstance() = SigninFragment()
    }
//    companion object {
//        private const val MY_DATA_KEY = "my_data"
//        private const val ANOTHER_DATA_KEY = "another_data"
//        fun newInstance(mySerializableData: Any, anotherData: Int) = MyFragment().apply {
//            //bundleOf() is an exstension method from KTX https://developer.android.com/kotlin/ktx
//            arguments = bundleOf(MY_DATA_KEY to mySerializableData, ANOTHER_DATA_KEY to anotherData)
//        }
//    }
    //    private lateinit var binding: FragmentSigninBinding
    private lateinit var viewModel: SharedSigninViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container,false)
        Timber.i("onCreateView")
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }


    //    This is called after onCreateView and before onViewStateRestored(Bundle).
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i("onActivityCreated")
        viewModel = ViewModelProvider(this).get(SharedSigninViewModel::class.java)
        // TODO: Use the ViewModel
        emailTextChanged()
        proceed_button_signin.setOnClickListener { proceedButtonTapped() }
        // associated activity나 fragment가 deinit되면 view model도 deinit되지만 view model의 instance는 view model provider를 통해 retain된다
    }

    fun proceedButtonTapped() {
        val email = email_textfield_siginin.text.toString()
        viewModel.email = email
        view?.findNavController()?.navigate(R.id.signinPasswordFragment)
    }

    fun updateProceedButtonState() {
        if (email_textfield_siginin.text.isEmpty()) {
            proceed_button_signin.isClickable = false
            proceed_button_signin.setBackgroundColor(Color.LTGRAY)
        } else {
            proceed_button_signin.isClickable = true
            proceed_button_signin.setBackgroundColor(Color.BLUE)
        }
    }


    fun emailTextChanged() {
        email_textfield_siginin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Timber.i("_afterTextChanged")
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                Timber.i("_beforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Timber.i("_onTextChanged")
                updateProceedButtonState()
            }

        })
    }


}