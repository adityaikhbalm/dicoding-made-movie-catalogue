package com.dicoding.made.di

import com.dicoding.made.data.remote.repository.*
import org.koin.dsl.module

val RepositoryModule = module {
    factory { MovieRepository(get(),get(),get(),get()) }
    factory { MoviePagedRepository(get(),get()) }
    factory { MoviePagedFactory(get()) }
    factory { SearchPagedRepository(get(),get()) }
    factory { SearchPagedFactory(get()) }
}