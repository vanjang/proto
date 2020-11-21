package com.cochipcho.conchip.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.cochipcho.conchip.R
import com.cochipcho.conchip.Services.UserData
import com.cochipcho.conchip.Services.UserData.updateProfile
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

        Handler(Looper.getMainLooper()).postDelayed({
        when(UserData.authProvider()) {
            "password" -> {
                if (user!!.isEmailVerified) {
                    startActivity(Intent(this, MainTabBarActivity::class.java))
                } else {
                    //Delete user from the database
                    startActivity(Intent(this, SigninNavHostActivity::class.java))
                }
            }
            null -> {
                startActivity(Intent(this, SigninNavHostActivity::class.java))
            }
            else -> {
//                if (UserData.authProvider() == "facebook.com") {
//                    // check access token validity
//                    // if expired the user should be directed to sign in page
//                    // if valid the user is ok to proceed with main tabbar activity
//                    return@postDelayed
//                }
//                maybe no need: https://stackoverflow.com/questions/56996776/facebook-login-via-firebase-should-i-verify-both-facebook-access-token-and-fire
                startActivity(Intent(this, MainTabBarActivity::class.java))
            }
        }
            finish()
        }, SPLASH_TIME_OUT)

    }



}