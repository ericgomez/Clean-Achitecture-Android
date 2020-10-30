package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.api.*
import com.esgomez.rickandmorty.domain.Character
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetAllCharactersUseCase(private val characterRequest: CharacterRequest) {

    fun invoke(currentPage: Int): Single<List<Character>> = characterRequest
            .getService<CharacterService>()
            .getAllCharacters(currentPage)
            .map(CharacterResponseServer::toCharacterDomainList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}