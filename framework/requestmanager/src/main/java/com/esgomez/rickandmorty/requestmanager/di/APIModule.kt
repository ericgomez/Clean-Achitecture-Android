package com.esgomez.rickandmorty.requestmanager.di

import com.esgomez.rickandmorty.data.RemoteCharacterDataSource
import com.esgomez.rickandmorty.data.RemoteEpisodeDataSource
import com.esgomez.rickandmorty.requestmanager.APIConstants.BASE_API_URL
import com.esgomez.rickandmorty.requestmanager.CharacterRequest
import com.esgomez.rickandmorty.requestmanager.CharacterRetrofitDataSource
import com.esgomez.rickandmorty.requestmanager.EpisodeRequest
import com.esgomez.rickandmorty.requestmanager.EpisodeRetrofitDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class APIModule  {

    @Provides
    fun characterRequestProvider (
        @Named("baseUrl") baseUrl: String
    ) = CharacterRequest(baseUrl)

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(): String = BASE_API_URL

    @Provides
    fun remoteCharacterDataSourceProvider(
        characterRequest: CharacterRequest
    ): RemoteCharacterDataSource = CharacterRetrofitDataSource(characterRequest)

    //Paso 8: Crear el método para proveer la petición de episodio "EpisodeRequest"
    @Provides
    fun episodeRequestProvider(
        @Named("baseUrl") baseUrl: String
    ) = EpisodeRequest(baseUrl)

    //Paso 9: Crear el método para proveer el recurso de datos de episodio remoto "EpisodeRetrofitDataSource"
    @Provides
    fun remoteEpisodeDataSourceProvider (
        episodeRequest: EpisodeRequest
    ): RemoteEpisodeDataSource = EpisodeRetrofitDataSource(episodeRequest)
}

