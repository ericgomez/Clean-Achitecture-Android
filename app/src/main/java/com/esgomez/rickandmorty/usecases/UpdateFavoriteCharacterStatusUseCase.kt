package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.data.CharacterRepository
import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.toCharacterEntity
import com.esgomez.rickandmorty.domain.Character
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Paso 4: Pasar como parámetros "characterRepository" de tipo CharacterRepository
class UpdateFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    //Paso 5: Crear método "invoke"
    //Paso 5.1: Pasar como parámetro "characterEntity" de tipo CharacterEntity
    //Paso 5.2: Indicar que el método devuelve un valor de tipo Maybe<Boolean>
    fun invoke(character: Character): Maybe<Boolean> {
        return characterRepository.updateFavoriteCharacterStatus(character)
    }
}