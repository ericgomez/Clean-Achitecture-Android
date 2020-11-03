package com.esgomez.rickandmorty.di

import com.esgomez.rickandmorty.presentation.CharacterListViewModel
import com.esgomez.rickandmorty.usecases.GetAllCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class CharacterListModule {

    @Provides
    fun characterListViewModelProvider(
        getAllCharactersUseCase: GetAllCharactersUseCase
    ) = CharacterListViewModel(
        getAllCharactersUseCase
    )
}

@Subcomponent(modules = [(CharacterListModule::class)])
interface CharacterListComponent {
    val characterListViewModel: CharacterListViewModel
}