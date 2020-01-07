package com.dicoding.made.di

import com.dicoding.made.data.remote.network.Connectivity
import com.dicoding.made.data.remote.service.MovieApiService
import org.koin.dsl.module

val RetrofitModule = module {
    single { Connectivity(get()) }
    single { MovieApiService() }
}