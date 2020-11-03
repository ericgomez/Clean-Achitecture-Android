package com.esgomez.rickandmorty.data

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

}