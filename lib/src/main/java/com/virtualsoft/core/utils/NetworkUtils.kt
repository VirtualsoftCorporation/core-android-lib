package com.virtualsoft.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.virtualsoft.core.utils.AppUtils.checkVersionCompatibility

object NetworkUtils {

    private var context: Context? = null

    var isConnected: Boolean = false
        private set
        get() = checkNetworkConnection()

    private var isConnectedCompat = false

    fun initialize(context: Context) {
        this.context = context
        if (checkVersionCompatibility(Build.VERSION_CODES.N))
            setNetworkCallback()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setNetworkCallback() {
        try {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val builder = NetworkRequest.Builder()

            connectivityManager?.registerNetworkCallback(builder.build(), object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnectedCompat = true
                }

                override fun onLost(network: Network) {
                    isConnectedCompat = false
                }
            })
        }
        catch (e: Exception) {
            isConnectedCompat = false
        }
    }

    private fun checkNetworkConnection(): Boolean {
        if (!checkVersionCompatibility(Build.VERSION_CODES.N)) {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val activeNetwork = connectivityManager?.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
        return isConnectedCompat
    }
}