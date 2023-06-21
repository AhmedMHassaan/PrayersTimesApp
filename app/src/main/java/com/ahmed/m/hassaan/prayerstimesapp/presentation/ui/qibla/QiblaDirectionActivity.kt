package com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla

import android.graphics.Color
import android.os.Bundle
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.base.activity.BaseActivityBinding
import com.ahmed.m.hassaan.prayerstimesapp.databinding.ActivityQiblaDirectionBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions


class QiblaDirectionActivity : BaseActivityBinding<ActivityQiblaDirectionBinding>(),
    OnMapReadyCallback {


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val ka3ba = LatLng(21.4225, 39.8262)
        mMap.addMarker(
            MarkerOptions()
                .position(ka3ba)
                .title("Ka3ba")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ka3ba))
    }


    override fun getLayoutId() = R.layout.activity_qibla_direction

}