package ec.com.pablorcruh.onlinefirst.features.task.data.sync

import android.content.Context
import ec.com.pablorcruh.onlinefirst.features.task.data.SyncWorker
import ec.com.pablorcruh.onlinefirst.features.task.domain.sync.SyncManager

class SyncManagerImpl(private val context: Context): SyncManager {
    override fun requestSync() {
        SyncWorker.SyncScheduler.scheduleTask(context)
    }
}