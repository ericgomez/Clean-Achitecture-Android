package com.esgomez.rickandmorty.requestmanager

import com.esgomez.rickandmorty.data.RemoteCharacterDataSource
import com.esgomez.rickandmorty.requestmanager.APIConstants.BASE_API_URL
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
}

