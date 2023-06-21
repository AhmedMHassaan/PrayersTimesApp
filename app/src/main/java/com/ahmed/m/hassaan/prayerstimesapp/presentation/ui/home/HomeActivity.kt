package com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.ViewModelProvider
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.base.activity.BaseActivityBinding
import com.ahmed.m.hassaan.prayerstimesapp.databinding.ActivityHomeBinding
import com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla.QiblaDirectionActivity
import com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla_live.CompassActivity
import com.ahmed.m.hassaan.prayerstimesapp.presentation.viewmodels.HomeViewModel
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE


class HomeActivity : BaseActivityBinding<ActivityHomeBinding>(), View.OnClickListener {

    private val locationRequestCode = 123
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
        binding.listener = this
        observeViewModels()

        homeViewModel.initEasyWayLocation(
            EasyWayLocation(
                this@HomeActivity,
                true,
                false,
                homeViewModel.easyWayListener
            )
        )
        requestRuntimePermission(
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            locationRequestCode
        )

    }

    private fun observeViewModels() {
        homeViewModel.errorLiveData.observe(this) {
            ToastMessages.error(it)
        }

        homeViewModel.qiblaDirectionsLivedata.observe(this) {
            startActivity(
                Intent(
                    this@HomeActivity,
                    CompassActivity::class.java
                ).also { intent ->
                    intent.putExtra("lat", it.latitude)
                    intent.putExtra("long", it.longitude)
                    intent.putExtra("dir", it.direction)
                }
            )
        }

        internetConnectivityLiveData.observe(this){
            binding.dateLayoutWithButtons.btnNextDay.isVisible = it
            binding.dateLayoutWithButtons.btnPreviousDay.isVisible = it
        }
    }


    override fun getLayoutId() = R.layout.activity_home


    override fun onDestroy() {
        homeViewModel.stopFetchingLocation()
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.startFetchingLocation()
    }

    override fun onPermissionResult(result: MutableMap<String, Boolean>?, requestCode: Int) {
        super.onPermissionResult(result, requestCode)

        when (requestCode) {
            LOCATION_SETTING_REQUEST_CODE ->
                homeViewModel.locationPermissionResultSuccess(
                    if (result?.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == true) Activity.RESULT_OK
                    else Activity.RESULT_CANCELED
                )

            locationRequestCode -> {
                if (result?.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == true) {
                    homeViewModel.locationPermissionResultSuccess(Activity.RESULT_OK)
                } else {
                    homeViewModel.locationPermissionResultSuccess(Activity.RESULT_CANCELED)
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

            }


        }
    }

    override fun onResult(result: ActivityResult?, requestCode: Int) {
//        super.onResult(result, requestCode)
        when (requestCode) {
            LOCATION_SETTING_REQUEST_CODE -> result?.resultCode?.let {
                homeViewModel.locationPermissionResultSuccess(it)
            }

            locationRequestCode -> {
                result?.let {

                    homeViewModel.locationPermissionResultSuccess(it.resultCode)
                }
            }
        }
    }

    override fun onPause() {
        homeViewModel.stopFetchingLocation()
        super.onPause()
    }


    override fun onClick(p0: View?) {
        when (p0) {
            binding.locationRefusedLayout.btnRequestPermission -> {
                requestRuntimePermission(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    locationRequestCode
                )

            }

            binding.dateLayoutWithButtons.btnNextDay -> {
                nextDayClicked()
            }

            binding.dateLayoutWithButtons.btnPreviousDay -> {
                previousDayClicked()
            }

            binding.btnShowQiblaDirection -> {
                homeViewModel.getQiblaDirections()
            }
        }
    }

    private fun nextDayClicked() {
        homeViewModel.requestNextDayPrayersTimes()
    }

    private fun previousDayClicked() {
        homeViewModel.requestPreviousDayPrayersTimes()
    }


}