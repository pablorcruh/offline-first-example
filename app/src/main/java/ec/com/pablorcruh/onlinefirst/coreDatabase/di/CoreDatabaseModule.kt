package ec.com.pablorcruh.onlinefirst.coreDatabase.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ec.com.pablorcruh.onlinefirst.coreDatabase.AppDatabase
import ec.com.pablorcruh.onlinefirst.features.task.data.local.TaskDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoreDatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }
}