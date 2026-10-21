package ec.com.pablorcruh.onlinefirst.features.task.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ec.com.pablorcruh.onlinefirst.coreDatabase.AppDatabase
import ec.com.pablorcruh.onlinefirst.coreNetwork.ApiService
import ec.com.pablorcruh.onlinefirst.coreUtils.Operation
import ec.com.pablorcruh.onlinefirst.coreUtils.SyncStatus
import ec.com.pablorcruh.onlinefirst.features.task.data.local.TaskDao
import ec.com.pablorcruh.onlinefirst.features.task.data.local.entities.TaskEntity
import ec.com.pablorcruh.onlinefirst.features.task.data.mappers.toCreateTaskRequest
import ec.com.pablorcruh.onlinefirst.features.task.data.mappers.toEntity
import ec.com.pablorcruh.onlinefirst.features.task.data.mappers.toUpdateTaskeEquest
import ec.com.pablorcruh.onlinefirst.features.task.data.sync.SyncMetaDataEntity
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    private val taskDao: TaskDao,
    private val api: ApiService,
    private val appDataBase: AppDatabase,

    @Assisted context: Context,
    @Assisted params: WorkerParameters

)
    : CoroutineWorker(context, params){
    override suspend fun doWork(): Result {
        uploadPendingTasks()
        downloadAndMerge()
        return Result.success()
    }

    private suspend fun downloadAndMerge() {
        val syncMetaData = taskDao.getSyncMetaData()
        val lastCursor = syncMetaData?.lastCursor?: 0
        val response = api.sync(lastCursor).body()
        if(response?.changes.isNullOrEmpty()) return
        appDataBase.withTransaction {
            response.changes.forEach { change ->
                when(change.operation){
                    Operation.CREATE.name, Operation.UPDATE.name ->{
                        val localTask = taskDao.findById(change.taskId)
                        if(localTask == null || localTask.syncStatus == SyncStatus.SYNC){
                            change.task.toEntity().let{
                                taskDao.upsert(it)
                            }
                        }
                    }

                    Operation.DELETE.name ->{
                        val localTask = taskDao.findById(change.taskId)
                        if(localTask?.syncStatus == SyncStatus.SYNC){
                            taskDao.deleteById(change.taskId)
                        }
                    }
                    else -> {}
                }
            }
            taskDao.upsertSyncMetaData(
                SyncMetaDataEntity(id = 1, lastCursor = checkNotNull(response).nextCursor.toInt())
            )
        }
    }

    private suspend fun uploadPendingTasks() {
        var hasFailure = false
        while(true){
            val pendingTasks = taskDao.getPendingTasks(50)
            pendingTasks.forEach { pendingTask ->
                try{
                    uploadTask(pendingTask)
                }catch(e: Exception){
                    hasFailure = true
                }
                if(hasFailure) break
            }
        }
    }

    private suspend fun uploadTask(pendingTask: TaskEntity) {
       when(pendingTask.syncStatus){
           SyncStatus.SYNC -> {}
           SyncStatus.PENDING_CREATE -> {
               val response =  api.createTask(createTaskRequest = pendingTask.toCreateTaskRequest()
               ).body()
               taskDao.upsert(
                   pendingTask.copy(
                       syncStatus = SyncStatus.SYNC,
                       version = checkNotNull(response).version
                   )
               )
           }
           SyncStatus.PENDIND_UPDATE -> {
               val response = api.updateTask(
                   id = pendingTask.id,
                   updateTaskRequest = pendingTask.toUpdateTaskeEquest()
               ).body()
               taskDao.upsert(
                   pendingTask.copy(
                       syncStatus = SyncStatus.SYNC,
                       version = checkNotNull(response).version
                   )
               )
           }

           SyncStatus.PENDING_DELETE -> {
               api.deleteTask(pendingTask.id)
               taskDao.deleteById(pendingTask.id)
           }
       }
    }

    object SyncScheduler{
        fun scheduleTask(context: Context){
            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                ).setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    30,
                    TimeUnit.SECONDS
                ).build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork("sync_task", ExistingWorkPolicy.KEEP, request)

        }
    }
}