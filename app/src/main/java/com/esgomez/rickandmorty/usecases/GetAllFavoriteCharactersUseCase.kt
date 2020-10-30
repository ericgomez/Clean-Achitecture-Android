package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.data.CharacterRepository
import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.database.toCharacterDomain
import com.esgomez.rickandmorty.database.toCharacterDomainList
import com.esgomez.rickandmorty.domain.Character
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class GetAllFavoriteCharactersUseCase(private val characterRepository: CharacterRepository) {

    fun invoke(): Flowable<List<Character>> = characterRepository.getAllFavoriteCharacters()
}