package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.api.*
import com.esgomez.rickandmorty.data.CharacterRepository
import com.esgomez.rickandmorty.domain.Character
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetAllCharactersUseCase(private val characterRepository: CharacterRepository) {

    fun invoke(currentPage: Int): Single<List<Character>> =
            characterRepository.getAllCharacters(currentPage)
}