package com.cochipcho.proto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cochipcho.proto.R

class SplashActivity : AppCompatActivity() {

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 3000/3 // 1 sec


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //to change title of activity
        val actionBar = supportActionBar
        actionBar!!.title = null

        Handler().postDelayed( {
            startActivity(Intent(this, SigninNavHostActivity::class.java))
                finish()
        }, SPLASH_TIME_OUT)

    }
}