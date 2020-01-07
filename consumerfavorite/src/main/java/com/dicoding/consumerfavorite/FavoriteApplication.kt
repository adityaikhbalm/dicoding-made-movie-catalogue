package com.dicoding.consumerfavorite

import android.app.Application
import com.dicoding.consumerfavorite.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FavoriteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@FavoriteApplication)
            modules(listOf(AppModule))
        }
    }
}