package com.esgomez.rickandmorty.database

import com.esgomez.rickandmorty.data.LocalCharacterDataSource
import com.esgomez.rickandmorty.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Creamos la fuen te de datos apatir de room
class CharacterRoomDataSource(
    database: CharacterDatabase
): LocalCharacterDataSource {

    //Inicializamos characterDao
    private val characterDao by lazy { database.characterDao() }

    override fun getAllFavoriteCharacters(): Flowable<List<Character>> {
        return characterDao
            .getAllFavoriteCharacters()//Nos regresa la lista de personajes
            .map (List<CharacterEntity>::toCharacterDomainList)//Convertimos un tipo de dato toCharacterDomainList a una lista
            .onErrorReturn { emptyList() }//Si hay un error que nos regrese una lista vacia
            .subscribeOn(Schedulers.io())//Indicamos donde se va trabajar esta informacion
    }

    //Paso 2.1: Pasar como parámetro "Id" de tipo Int
    //Paso 2.2: Indicar que el método devuelve un valor de tipo Maybe<Boolean>
    override fun getFavoriteCharacterStatus(id: Int): Maybe<Boolean> {
        return characterDao.getCharacterById(id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                Maybe.just(!isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> {
        val characterEntity = character.toCharacterEntity()
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