package ec.com.pablorcruh.onlinefirst.coreConnectivity

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class DefaultConnectivityMonitor(
    private val context: Context
): ConnectivityMonitor {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun isOnline(): Boolean {
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun isOnlineFlow(): Flow<Boolean> = callbackFlow {
        val callback = object: ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                trySend(true)
            }

            override fun onUnavailable() {
                trySend(false)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {

            }
            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager?.registerNetworkCallback(request, callback)
        trySend(isOnline())
        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}