package com.dicoding.made

import android.app.Application
import com.dicoding.made.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieApplication : Application() {

    companion object {
        var refreshMovie = false
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(listOf(RepositoryModule,RetrofitModule,OtherModule,ViewModelModule))
        }
    }
}