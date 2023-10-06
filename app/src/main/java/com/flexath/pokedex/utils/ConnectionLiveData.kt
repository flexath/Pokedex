package com.flexath.pokedex.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import javax.inject.Inject

class LiveNetworkChecker @Inject constructor(private val connectivityManager: ConnectivityManager) :
    LiveData<Boolean>() {

    // invoke this callback when everytime network connection changes
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private val networksThatHasInternet: MutableSet<Network> = HashSet()

    init {
        checkIfNetworkHasInternet()
    }

    private fun checkIfNetworkHasInternet() = postValue(networksThatHasInternet.size > 0)

    override fun onActive() {
        super.onActive()

        networkCallback = createNetworkCallback()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest,networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)

    }

    private fun createNetworkCallback() = object: ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasInternet = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

            if(hasInternet == true) {
                networksThatHasInternet.add(network)
            }
            checkIfNetworkHasInternet()
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            networksThatHasInternet.remove(network)
            checkIfNetworkHasInternet()
        }
    }
}