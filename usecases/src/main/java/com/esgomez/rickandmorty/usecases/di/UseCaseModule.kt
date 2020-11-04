package com.esgomez.rickandmorty.usecases.di

import com.esgomez.rickandmorty.data.CharacterRepository
import com.esgomez.rickandmorty.data.EpisodeRepository
import com.esgomez.rickandmorty.usecases.*
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    //Caso de uso de todos los personajes
    @Provides
    fun getAllCharacterUseCaseProvider(characterRepository: CharacterRepository) =
        GetAllCharactersUseCase(characterRepository)

    @Provides
    fun getAllFavoriteCharacterUseCaseProvider(characterRepository: CharacterRepository) =
        GetAllFavoriteCharactersUseCase(characterRepository)

    //Paso 4: Crear el método para proveer el caso de uso "GetFavoriteCharacterStatusUseCase"
    @Provides
    fun getFavoriteCharacterStatusUseCaseProvider(characterRepository: CharacterRepository) =
        GetFavoriteCharacterStatusUseCase(characterRepository)

    //Paso 5: Crear el método para proveer el caso de uso "UpdateFavoriteCharacterStatusUseCase"
    @Provides
    fun updateFavoriteCharacterStatusUseCaseProvider(characterRepository: CharacterRepository) =
        UpdateFavoriteCharacterStatusUseCase(characterRepository)

    //Paso 6: Crear el método para proveer el caso de uso "GetEpisodeFromCharacterUseCase"
    @Provides
    fun getEpisodeFromCharacterUseCaseProvider(episodeRepository: EpisodeRepository) =
        GetEpisodeFromCharacterUseCase(episodeRepository)


}