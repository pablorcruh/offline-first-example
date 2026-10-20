package ec.com.pablorcruh.onlinefirst.coreConnectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityMonitor {

    fun isOnline(): Boolean
    fun isOnlineFlow(): Flow<Boolean>
}