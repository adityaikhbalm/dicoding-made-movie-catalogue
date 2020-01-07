package com.dicoding.consumerfavorite.di
import com.dicoding.consumerfavorite.FavoriteViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    viewModel { FavoriteViewModel(androidApplication().contentResolver) }
}