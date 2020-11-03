package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.data.CharacterRepository
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


}