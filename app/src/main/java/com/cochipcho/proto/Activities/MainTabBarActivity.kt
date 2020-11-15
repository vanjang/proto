package com.cochipcho.proto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cochipcho.proto.Auth.GoogleAuth
import com.cochipcho.proto.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main_tab_bar.*

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