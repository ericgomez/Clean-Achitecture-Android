package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.database.CharacterDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import io.reactivex.Maybe

//Paso 1: Pasar como parámetros "characterDao" de tipo CharacterDao
class GetFavoriteCharacterStatusUseCase(
    private val characterDao: CharacterDao
) {

    //Paso 2: Crear método "invoke"
    //Paso 2.1: Pasar como parámetro "characterId" de tipo Int
    //Paso 2.2: Indicar que el método devuelve un valor de tipo Maybe<Boolean>
    fun invoke(characterId: Int): Maybe<Boolean> {
        return characterDao.getCharacterById(characterId)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    Maybe.just(!isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}