package com.esgomez.rickandmorty.requestmanager

import com.esgomez.rickandmorty.data.RemoteCharacterDataSource
import com.esgomez.rickandmorty.data.RemoteEpisodeDataSource
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Episode
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterRetrofitDataSource(
    private val characterRequest: CharacterRequest
): RemoteCharacterDataSource {
    override fun getAllCharacters(page: Int): Single<List<Character>> {
        return characterRequest
            .getService<CharacterService>()
            .getAllCharacters(page)
            .map(CharacterResponseServer::toCharacterDomainList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

}

//Paso 4: Pasar como parámetro "episodeRequest" de tipo EpisodeRequest
class EpisodeRetrofitDataSource(
    private val episodeRequest: EpisodeRequest
//Paso 4.1: Implementar la interfaz para fuente de datos remoto de episodio creada en el Paso 1 : RemoteEpisodeDataSource
): RemoteEpisodeDataSource {

    //Paso 5: Implementar método de la interfaz para fuente de datos remoto de episodio creada en el Paso 1
    //Paso 5.1: Migrar la lógica del caso de uso "getEpisodeFromCharacterUseCase
    override fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> {
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