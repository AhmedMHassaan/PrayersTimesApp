package com.ahmed.m.hassaan.prayerstimesapp.base.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.core.activity.CoreActivity
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : CoreActivity() {

    protected val internetConnectivityLiveData = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        observeInternetConnection()
    }

    private val connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    private var callback: ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                internetConnectivityLiveData.postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                internetConnectivityLiveData.postValue(false)

            }

            override fun onUnavailable() {
                super.onUnavailable()
                ToastMessages.msg(getString(R.string.not_connected_to_network))
                internetConnectivityLiveData.postValue(false)
            }
        }


    private fun observeInternetConnection() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(callback)
        } else {

            val isConnected =
                connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo?.isConnected() == true
            internetConnectivityLiveData.postValue(isConnected)

        }
    }


    fun isNetworkConnected(): Boolean {
        return internetConnectivityLiveData.value ?: false
    }

    override fun onDestroy() {
        connectivityManager.unregisterNetworkCallback(callback)
        super.onDestroy()
    }


}