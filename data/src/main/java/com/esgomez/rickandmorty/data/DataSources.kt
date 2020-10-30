package com.esgomez.rickandmorty.data

import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RemoteCharacterDataSource {
    fun getAllCharacters(page: Int): Single<List<Character>>
}

interface LocalCharacterDataSource {
    fun getAllFavoriteCharacters(): Flowable<List<Character>>

    fun getFavoriteCharacterStatus(id: Int): Maybe<Boolean>

    fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean>
}

//Paso 1: Crear interfaz para fuente de datos remoto de episodio (RemoteEpisodeDataSource)
//Paso 1.1: Crear método "getEpisodeFromCharacter" que retorna un objeto de tipo Single<List<Episode>>
//Paso 1.2: Pasar como parámetro "episodeUrlList" de tipo List<String>
interface RemoteEpisodeDataSource {
    fun getEpisodeFromCharacter(episodeUrlList : List<String>): Single<List<Episode>>
}