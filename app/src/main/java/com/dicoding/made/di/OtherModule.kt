package com.dicoding.made.di

import com.dicoding.made.data.local.MoviesDatabase
import com.dicoding.made.data.local.MoviesLocalDataSource
import com.dicoding.made.data.remote.network.ResponseHandler
import com.dicoding.made.ui.notification.AlarmReceiver
import org.koin.dsl.module

val OtherModule = module {
    single { ResponseHandler() }
    single { MoviesDatabase(get()) }
    single { MoviesLocalDataSource(get(),get()) }
    factory { AlarmReceiver() }
}