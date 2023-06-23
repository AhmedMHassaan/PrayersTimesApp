package com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.base.App
import com.ahmed.m.hassaan.prayerstimesapp.base.activity.BaseActivityBinding
import com.ahmed.m.hassaan.prayerstimesapp.databinding.ActivityQiblaDirectionBinding
import com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla_live.Compass
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class QiblaDirectionActivity : BaseActivityBinding<ActivityQiblaDirectionBinding>(),
    OnMapReadyCallback {


    private var compass: Compass? = null

    private lateinit var mMap: GoogleMap

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var direction: Double = 0.0

    private val ka3ba = LatLng(21.4225, 39.8262)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        latitude = intent?.getDoubleExtra("lat", 0.0)!!
        longitude = intent?.getDoubleExtra("long", 0.0)!!
        direction = intent?.getDoubleExtra("dir", 0.0)!!


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        mMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.kaaba_icon))
                .position(ka3ba)
                .title("Ka3ba")
        )


        drawArrowOnMap()

        drawMyLocation()


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ka3ba, 4f))




        compass = context?.let {
            val sensorManager = it.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            Compass(sensorManager)
        }
//        compass?.start()

        compass?.setListener(object : Compass.CompassListener {
            override fun onNewAzimuth(azimuth: Float) {

                val cameraPosition: CameraPosition = CameraPosition.Builder()
                    .bearing(azimuth)
                    .target(ka3ba)
                    .zoom(5f)
                    .build()

                Log.d(
                    App.APP_TAG,
                    "QiblaDirectionActivity - onNewAzimuth:  camera rotated $azimuth"
                )
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),50,object : GoogleMap.CancelableCallback {
                    override fun onCancel() {

                    }

                    override fun onFinish() {

                    }
                });


            }
        })

        compass?.start()

    }


    private fun drawMyLocation() {
        mMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
                .position(LatLng(latitude, longitude))
                .title("Your Location")
        )
    }

    private fun drawArrowOnMap() {
//        val bitmap = BitmapFactory.decodeResource(resources, com.ahmed.m.hassaan.prayerstimesapp.R.drawable.qibla_arrow)

        val overlayOptions = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.map_arrow))
//            .position(
//                LatLng(latitude, longitude),
//                700000f, 700000f
//            )
            .positionFromBounds(
                LatLngBounds.Builder().include(ka3ba).include(LatLng(latitude, longitude)).build()
            )
            .bearing(direction.toFloat())
            .visible(true)


        mMap.addGroundOverlay(overlayOptions)
    }


    override fun getLayoutId() = R.layout.activity_qibla_direction


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


}