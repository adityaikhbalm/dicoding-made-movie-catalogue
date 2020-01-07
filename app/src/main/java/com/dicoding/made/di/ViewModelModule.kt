package com.dicoding.made.di

import com.dicoding.made.ui.favorite.FavoriteViewModel
import com.dicoding.made.ui.main.search.SearchViewModel
import com.dicoding.made.ui.main.detail.DetailViewModel
import com.dicoding.made.ui.main.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}