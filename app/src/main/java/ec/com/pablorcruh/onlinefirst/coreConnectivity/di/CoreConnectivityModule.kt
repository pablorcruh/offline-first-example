package ec.com.pablorcruh.onlinefirst.coreConnectivity.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ec.com.pablorcruh.onlinefirst.coreConnectivity.ConnectivityMonitor
import ec.com.pablorcruh.onlinefirst.coreConnectivity.DefaultConnectivityMonitor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoreConnectivityModule {

    @Singleton
    @Provides
    fun provideConnectivityMonitor(@ApplicationContext context: Context): ConnectivityMonitor{
        return DefaultConnectivityMonitor(context)
    }
}