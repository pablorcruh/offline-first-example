package ec.com.pablorcruh.onlinefirst.features.task.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ec.com.pablorcruh.onlinefirst.features.task.data.local.TaskDao
import ec.com.pablorcruh.onlinefirst.features.task.data.repository.TaskRepositoryImpl
import ec.com.pablorcruh.onlinefirst.features.task.data.sync.SyncManagerImpl
import ec.com.pablorcruh.onlinefirst.features.task.domain.repository.TaskRepository
import ec.com.pablorcruh.onlinefirst.features.task.domain.sync.SyncManager
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TaskDataModule {

    @Provides
    @Singleton
    fun provideSyncManager(@ApplicationContext context: Context): SyncManager{
        return SyncManagerImpl(context)
    }

    @Provides
    fun provideTaskRepository(
        taskDao: TaskDao,
        syncManager: SyncManager
    ): TaskRepository{
        return TaskRepositoryImpl(
            taskDao, syncManager
        )
    }
}