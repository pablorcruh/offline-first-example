package ec.com.pablorcruh.onlinefirst.features.task.data.sync

import android.content.Context
import ec.com.pablorcruh.onlinefirst.coreConnectivity.ConnectivityMonitor
import ec.com.pablorcruh.onlinefirst.features.task.domain.sync.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncCoordinator @Inject constructor(
    private val connectivityMonitor: ConnectivityMonitor,
    private val syncManager: SyncManager
) {
    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun start(context: Context){
        scope.launch {
            connectivityMonitor.isOnlineFlow()
                .distinctUntilChanged()
                .filter{it}
                .collect{
                    syncManager.requestSync()
                }
        }
    }

    fun cancel(){
        scope.cancel()
    }
}