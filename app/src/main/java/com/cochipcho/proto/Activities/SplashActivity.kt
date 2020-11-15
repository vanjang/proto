package com.cochipcho.proto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cochipcho.proto.R
import com.cochipcho.proto.Services.UserData.updateProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT: Long = 3000 / 3 // 1 sec


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //to change title of activity
        val actionBar = supportActionBar
        actionBar!!.title = null
        val user = Firebase.auth.currentUser
        user?.reload()
        Timber.i("user email verified?: ${user?.isEmailVerified}")
//        Firebase.auth.signOut()

        Handler().postDelayed({
            if (user != null) {
                if (user.isEmailVerified) {
                    // should check whether profile data is there
                    Timber.i("email verified")
                    Timber.i("is email verified?: ${user.isEmailVerified}")
                    updateProfile()
                    startActivity(Intent(this, MainTabBarActivity::class.java))
                } else {
                    //Delete user from the database
                    Timber.i("email not verified")
                    Timber.i("is email verified?: ${user.isEmailVerified}")
                    startActivity(Intent(this, SigninNavHostActivity::class.java))
                }
            } else if (user == null) {
                Timber.i("no user")
                startActivity(Intent(this, SigninNavHostActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)

    }

    private fun verifyEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Timber.i("verify email: ${user.isEmailVerified()}")
        }
    }

}