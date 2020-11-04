package com.esgomez.rickandmorty.data.di

import com.esgomez.rickandmorty.data.*
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun characterRepositoryProvider(
        remoteCharacterDataSource: RemoteCharacterDataSource,
        localCharacterDataSource: LocalCharacterDataSource,
    )  = CharacterRepository(
        remoteCharacterDataSource, localCharacterDataSource
    )

    //Paso 7: Crear el método para proveer el repository "EpisodeRepository"
    @Provides
    fun episodeRepositoryProvider(
        remoteEpisodeDataSource: RemoteEpisodeDataSource
    ) = EpisodeRepository(
        remoteEpisodeDataSource
    )

}