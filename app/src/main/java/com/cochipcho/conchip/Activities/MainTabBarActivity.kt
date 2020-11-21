package com.cochipcho.conchip.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cochipcho.conchip.Auth.AppleAuth
import com.cochipcho.conchip.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main_tab_bar.*
import timber.log.Timber

class MainTabBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tab_bar)

        signout_button.setOnClickListener {
//            GoogleAuth.signInClient.signOut()
            Firebase.auth.signOut()

            startActivity(Intent(this, SigninNavHostActivity::class.java))
        }
    }
}