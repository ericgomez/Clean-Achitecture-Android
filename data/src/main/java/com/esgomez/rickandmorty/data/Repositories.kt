package com.esgomez.rickandmorty.data

import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

//Aqui tendrmos todos los repositorios
class CharacterRepository (
    private val remoteCharacterDataSource: RemoteCharacterDataSource,
    private val localCharacterDataSource: LocalCharacterDataSource
){
    fun getAllCharacters(page: Int): Single<List<Character>> =
            remoteCharacterDataSource.getAllCharacters(page)

    fun getAllFavoriteCharacters(): Flowable<List<Character>> =
            localCharacterDataSource.getAllFavoriteCharacters()

    fun getFavoriteCharacterStatus(characterId: Int): Maybe<Boolean> =
            localCharacterDataSource.getFavoriteCharacterStatus(characterId)

    fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> =
            localCharacterDataSource.updateFavoriteCharacterStatus(character)

}

//Paso 2: Pasar como parámetro "remoteEpisodeDataSource" de tipo RemoteEpisodeDataSource
class EpisodeRepository(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource
) {

    //region Public Methods

    //Paso 3: Crear método "getEpisodeFromCharacter" que retorna un objeto de tipo Single<List<Episode>>
    //Paso 3.1: Pasar como parámetro "episodeUrlList" de tipo List<String>
    //Paso 3.2: Devolver el método "getEpisodeFromCharacter" del parámetro "episodeUrlList"
    fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> =
            remoteEpisodeDataSource.getEpisodeFromCharacter(episodeUrlList)

    //endregion
}