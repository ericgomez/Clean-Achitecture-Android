package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.api.EpisodeRequest
import com.esgomez.rickandmorty.api.EpisodeServer
import com.esgomez.rickandmorty.api.EpisodeService
import com.esgomez.rickandmorty.api.toEpisodeDomain
import com.esgomez.rickandmorty.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetEpisodeFromCharacterUseCase(private val episodeRequest: EpisodeRequest) {

    //Recibe un listado de URLS
    fun invoke(episodeUrlList: List<String>) : Single<List<Episode>> {
        return Observable.fromIterable(episodeUrlList)
                .flatMap { episode: String ->
                    episodeRequest.baseUrl = episode
                    episodeRequest
                            .getService<EpisodeService>()
                            .getEpisode()
                            //Paso 10: Implementar función map para cambiar de episodio de servidor a episodio de dominio
                            .map(EpisodeServer::toEpisodeDomain)
                            .toObservable()
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}