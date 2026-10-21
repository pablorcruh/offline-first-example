package ec.com.pablorcruh.onlinefirst.features.task.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ec.com.pablorcruh.onlinefirst.coreUtils.SyncStatus
import ec.com.pablorcruh.onlinefirst.features.task.data.local.entities.TaskEntity
import ec.com.pablorcruh.onlinefirst.features.task.data.sync.SyncMetaDataEntity
import ec.com.pablorcruh.onlinefirst.features.task.domain.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {


    @Query("SELECT * FROM task WHERE syncStatus != 'PENDING_DELETE' ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun findById(id: String): TaskEntity?

    @Query("SELECT COUNT(*) FROM task WHERE syncStatus = 'PENDING_DELETE'")
    fun observePendingSyncCount(): Flow<Int>

    @Query("SELECT * FROM task WHERE syncStatus != 'SYNC' LIMIT :limit")
    suspend fun getPendingTasks(limit: Int): List<TaskEntity>

    @Upsert
    suspend fun upsert(task: TaskEntity)

    @Upsert
    suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("UPDATE task SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: SyncStatus)

    @Query("DELETE FROM task WHERE syncStatus = 'SYNC'")
    suspend fun deleteAllSync()

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteById(id: String)

    @Transaction
    suspend fun replaceAllSyncTasks(tasks: List<TaskEntity>){
        deleteAllSync()
        upsertAll(tasks)
    }

    @Upsert
    suspend fun upsertSyncMetaData(syncMetaData: SyncMetaDataEntity)

    @Query("SELECT * FROM sync_meta_data LIMIT 1")
    suspend fun getSyncMetaData(): SyncMetaDataEntity?
}