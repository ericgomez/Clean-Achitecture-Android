package com.esgomez.rickandmorty.di

import com.esgomez.rickandmorty.presentation.FavoriteListViewModel
import com.esgomez.rickandmorty.usecases.GetAllFavoriteCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class FavoriteListModule {

    @Provides
    fun favoriteListViewModelProvider(
        getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase
    ) = FavoriteListViewModel(
        getAllFavoriteCharactersUseCase
    )
}

@Subcomponent(modules = [(FavoriteListModule::class)])
interface FavoriteListComponent {
    val favoriteListViewModel: FavoriteListViewModel
}