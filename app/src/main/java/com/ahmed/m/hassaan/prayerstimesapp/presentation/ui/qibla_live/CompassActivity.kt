package com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla_live

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.base.App
import com.ahmed.m.hassaan.prayerstimesapp.base.activity.BaseActivityBinding
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.QiblaDirection
import com.ahmed.m.hassaan.prayerstimesapp.databinding.ActivityCompassBinding

class CompassActivity : BaseActivityBinding<ActivityCompassBinding>() {
    private lateinit var locationManager: LocationManager


    private var compass: Compass? = null
//    private var currentAzimuth: Float = 0.toFloat()

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var direction: Double = 0.0


    private var currentAngle: Float = 0f


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.imgQiblaArrow.visibility = INVISIBLE
        binding.imgQiblaArrow.visibility = View.GONE
        locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager



        latitude = intent?.getDoubleExtra("lat",0.0)!!
        longitude = intent?.getDoubleExtra("long",0.0)!!
        direction = intent?.getDoubleExtra("dir",0.0)!!



        setupCompass()

    }


    override fun onStart() {
        super.onStart()
        Log.d(App.APP_TAG, "CompassActivity - onStart: start compass ")
        compass?.start()
    }

    override fun onStop() {
        super.onStop()
        Log.d(App.APP_TAG, "CompassActivity - onStop: stop compass ")
        compass?.stop()
    }

    private fun setupCompass() {


        compass = context?.let {
            val sensorManager = it.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            Compass(sensorManager)
        }
        compass?.setListener(object : Compass.CompassListener {
            override fun onNewAzimuth(azimuth: Float) {
                adjustGambarDial(azimuth)
                adjustArrowQiblat(azimuth)
            }
        })
    }


    private fun adjustGambarDial(azimuth: Float) {
        // Log.d(TAG, "will set rotation from " + currentAzimuth + " to "                + azimuth);

        val an = RotateAnimation(
            -currentAngle, -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAngle = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.imgCompass.startAnimation(an)
    }

    private fun adjustArrowQiblat(azimuth: Float) {
        //Log.d(TAG, "will set rotation from " + currentAzimuth + " to "                + azimuth);

        val qiblaDir = direction
        val an = RotateAnimation(
            -currentAngle + qiblaDir.toFloat(), -azimuth,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAngle = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.imgQiblaArrow.startAnimation(an)
        if (qiblaDir > 0) {
            binding.imgQiblaArrow.visibility = View.VISIBLE
        } else {
            binding.imgQiblaArrow.visibility = INVISIBLE
            binding.imgQiblaArrow.visibility = View.GONE
        }
    }


    override fun getLayoutId() = R.layout.activity_compass



}