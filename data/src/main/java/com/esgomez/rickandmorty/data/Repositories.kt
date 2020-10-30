package com.esgomez.rickandmorty.data

import com.esgomez.rickandmorty.domain.Character

//Aqui tendrmos todos los repositorios
class CharacterRepository (
    private val remoteCharacterDataSource: RemoteCharacterDataSource,
    private val localCharacterDataSource: LocalCharacterDataSource
){
    fun getAllCharacters(page: Int) = remoteCharacterDataSource.getAllCharacters(page)

    //Agregamos los tres metodos necesarios para uso local
    fun getAllFavoriteCharacters() = localCharacterDataSource.getAllFavoriteCharacters()

    fun getFavoriteCharacterStatus(id: Int) = localCharacterDataSource.getFavoriteCharacterStatus(id)

    fun updateFavoriteCharacterStatus(character: Character) = localCharacterDataSource.updateFavoriteCharacterStatus(character)

}