package com.esgomez.rickandmorty.di

import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.presentation.CharacterDetailViewModel
import com.esgomez.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.esgomez.rickandmorty.usecases.GetFavoriteCharacterStatusUseCase
import com.esgomez.rickandmorty.usecases.UpdateFavoriteCharacterStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class CharacterDetailModule(
        private val character: Character?
) {

    @Provides
    fun characterDetailViewModelProvider(
            getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
            getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusUseCase,
            updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase
    ) = CharacterDetailViewModel(
            character,
            getEpisodeFromCharacterUseCase,
            getFavoriteCharacterStatusUseCase,
            updateFavoriteCharacterStatusUseCase
    )
}

@Subcomponent(modules = [(CharacterDetailModule::class)])
interface CharacterDetailComponent {
    val characterDetailViewModel: CharacterDetailViewModel
}
