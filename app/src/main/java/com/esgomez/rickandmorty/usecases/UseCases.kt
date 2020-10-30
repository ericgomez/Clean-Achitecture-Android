package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.data.CharacterRepository
import com.esgomez.rickandmorty.data.EpisodeRepository
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class GetAllCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(currentPage: Int): Single<List<Character>> =
        characterRepository.getAllCharacters(currentPage)
}

class GetAllFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(): Flowable<List<Character>> = characterRepository.getAllFavoriteCharacters()
}

//Paso 6: Cambiar parámetro "episodeRequest" de tipo EpisodeRequest por parámetro "episodeRepository" de tipo EpisodeRepository
class GetEpisodeFromCharacterUseCase(
    private val episodeRepository: EpisodeRepository
) {

    //Recibe un listado de URLS
    //Paso 7: Devolver el método "getEpisodeFromCharacter" del parámetro "episodeRepository"
    fun invoke(episodeUrlList: List<String>): Single<List<Episode>> =
        episodeRepository.getEpisodeFromCharacter(episodeUrlList)
}

//Paso 1: Pasar como parámetros "characterRepository" de tipo CharacterRepository
class GetFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    //Paso 2: Crear método "invoke"
    fun invoke(characterId: Int): Maybe<Boolean> =
        characterRepository.getFavoriteCharacterStatus(characterId)
}

//Paso 4: Pasar como parámetros "characterRepository" de tipo CharacterRepository
class UpdateFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    //Paso 5: Crear método "invoke"
    //Paso 5.1: Pasar como parámetro "characterEntity" de tipo CharacterEntity
    //Paso 5.2: Indicar que el método devuelve un valor de tipo Maybe<Boolean>
    fun invoke(character: Character): Maybe<Boolean> =
        characterRepository.updateFavoriteCharacterStatus(character)
}