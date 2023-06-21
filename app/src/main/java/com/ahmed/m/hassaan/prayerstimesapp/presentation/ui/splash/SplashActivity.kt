package com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.base.activity.BaseActivityBinding
import com.ahmed.m.hassaan.prayerstimesapp.databinding.ActivitySplashBinding
import com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.home.HomeActivity

class SplashActivity : BaseActivityBinding<ActivitySplashBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(
                    this@SplashActivity,
                    HomeActivity::class.java
                )
            )
            finish()
        }, 2000)

    }

    override fun getLayoutId() = R.layout.activity_splash
}