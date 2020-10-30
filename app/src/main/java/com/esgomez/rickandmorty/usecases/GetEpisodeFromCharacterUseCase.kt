package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.api.EpisodeRequest
import com.esgomez.rickandmorty.api.EpisodeServer
import com.esgomez.rickandmorty.api.EpisodeService
import com.esgomez.rickandmorty.api.toEpisodeDomain
import com.esgomez.rickandmorty.data.EpisodeRepository
import com.esgomez.rickandmorty.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Paso 6: Cambiar parámetro "episodeRequest" de tipo EpisodeRequest por parámetro "episodeRepository" de tipo EpisodeRepository
class GetEpisodeFromCharacterUseCase(private val episodeRepository: EpisodeRepository) {

    //Recibe un listado de URLS
    //Paso 7: Devolver el método "getEpisodeFromCharacter" del parámetro "episodeRepository"
    fun invoke(episodeUrlList: List<String>) : Single<List<Episode>> {
        return episodeRepository.getEpisodeFromCharacter(episodeUrlList)
    }
}