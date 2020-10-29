package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.api.EpisodeRequest
import com.esgomez.rickandmorty.api.EpisodeService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetEpisodeFromCharacterUseCase(private val episodeRequest: EpisodeRequest) {

    //Recibe un listado de URLS
    fun invoke(episodeUrlList: List<String>) =   Observable.fromIterable(episodeUrlList)
            .flatMap { episode: String ->
                episodeRequest.baseUrl = episode
                episodeRequest
                        .getService<EpisodeService>()
                        .getEpisode()
                        .toObservable()
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}