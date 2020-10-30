package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.database.toCharacterDomain
import com.esgomez.rickandmorty.database.toCharacterDomainList
import com.esgomez.rickandmorty.domain.Character
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class GetAllFavoriteCharactersUseCase(private val characterDao: CharacterDao) {

    fun invoke(): Flowable<List<Character>> = characterDao
            .getAllFavoriteCharacters()//Nos regresa la lista de personajes
            .map (List<CharacterEntity>::toCharacterDomainList)//Convertimos un tipo de dato toCharacterDomainList a una lista
            .onErrorReturn { emptyList() }//Si hay un error que nos regrese una lista vacia
            .subscribeOn(Schedulers.io())//Indicamos donde se va trabajar esta informacion
}