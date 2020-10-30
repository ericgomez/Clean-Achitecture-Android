package com.esgomez.rickandmorty.api

import com.esgomez.rickandmorty.data.RemoteCharacterDataSource
import com.esgomez.rickandmorty.domain.Character
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