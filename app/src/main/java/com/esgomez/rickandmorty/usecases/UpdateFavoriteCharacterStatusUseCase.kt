package com.esgomez.rickandmorty.usecases

import com.esgomez.rickandmorty.database.CharacterDao
import com.esgomez.rickandmorty.database.CharacterEntity
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Paso 4: Pasar como parámetros "characterDao" de tipo CharacterDao
class UpdateFavoriteCharacterStatusUseCase(
    private val characterDao: CharacterDao
) {

    //Paso 5: Crear método "invoke"
    //Paso 5.1: Pasar como parámetro "characterEntity" de tipo CharacterEntity
    //Paso 5.2: Indicar que el método devuelve un valor de tipo Maybe<Boolean>
    fun invoke(characterEntity: CharacterEntity): Maybe<Boolean> {
        return characterDao.getCharacterById(characterEntity.id)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    if(isEmpty){
                        characterDao.insertCharacter(characterEntity)
                    }else{
                        characterDao.deleteCharacter(characterEntity)
                    }
                    Maybe.just(isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}