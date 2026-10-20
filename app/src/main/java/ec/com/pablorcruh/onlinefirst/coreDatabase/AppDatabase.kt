package ec.com.pablorcruh.onlinefirst.coreDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ec.com.pablorcruh.onlinefirst.features.task.data.local.TaskDao
import ec.com.pablorcruh.onlinefirst.features.task.data.local.entities.TaskEntity
import ec.com.pablorcruh.onlinefirst.features.task.data.sync.SyncMetaDataEntity

@Database(
    entities = [
        TaskEntity::class,
        SyncMetaDataEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase(){

    companion object{
        fun getInstance(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).fallbackToDestructiveMigration(
            dropAllTables = true
        ).build()
    }

    abstract fun taskDao(): TaskDao
}