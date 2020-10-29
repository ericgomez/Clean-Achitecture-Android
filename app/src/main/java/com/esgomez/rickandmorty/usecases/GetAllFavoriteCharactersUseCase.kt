package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.database.CharacterDao
import io.reactivex.schedulers.Schedulers

class GetAllFavoriteCharactersUseCase(private val characterDao: CharacterDao) {

    fun invoke() = characterDao
            .getAllFavoriteCharacters()//Nos regresa la lista de personajes
            .onErrorReturn { emptyList() }//Si hay un error que nos regrese una lista vacia
            .subscribeOn(Schedulers.io())//Indicamos donde se va trabajar esta informacion
}