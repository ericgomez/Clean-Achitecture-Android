package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.data.CharacterRepository
import com.esgomez.rickandmorty.database.CharacterDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import io.reactivex.Maybe

//Paso 1: Pasar como parámetros "characterRepository" de tipo CharacterRepository
class GetFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    //Paso 2: Crear método "invoke"
    fun invoke(characterId: Int): Maybe<Boolean> {
        return characterRepository.getFavoriteCharacterStatus(characterId)
    }
}