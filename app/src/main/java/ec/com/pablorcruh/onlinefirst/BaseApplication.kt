package ec.com.pablorcruh.onlinefirst

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ec.com.pablorcruh.onlinefirst.features.task.data.sync.SyncCoordinator
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    @Inject
    lateinit var syncCoordinator: SyncCoordinator

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate(){
        super.onCreate()
        syncCoordinator.start(this)
    }

    override fun onTerminate(){
        super.onTerminate()
        syncCoordinator.cancel()
    }
}