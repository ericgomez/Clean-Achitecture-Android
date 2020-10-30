package com.esgomez.rickandmorty.data

//Aqui tendrmos todos los repositorios
class CharacterRepository (
    private val remoteCharacterDataSource: RemoteCharacterDataSource
){
    fun getAllCharacters(page: Int) = remoteCharacterDataSource.getAllCharacters(page)
}