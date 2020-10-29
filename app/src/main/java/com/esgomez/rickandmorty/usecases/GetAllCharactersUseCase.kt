package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.api.CharacterRequest
import com.esgomez.rickandmorty.api.CharacterResponseServer
import com.esgomez.rickandmorty.api.CharacterService
import com.esgomez.rickandmorty.api.toCharacterServerList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetAllCharactersUseCase(private val characterRequest: CharacterRequest) {

    fun invoke(currentPage: Int) = characterRequest
            .getService<CharacterService>()
            .getAllCharacters(currentPage)
            .map(CharacterResponseServer::toCharacterServerList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}