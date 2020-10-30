package com.esgomez.rickandmorty.data

import com.esgomez.rickandmorty.domain.Character
import io.reactivex.Single

interface RemoteCharacterDataSource {
    fun getAllCharacters(page: Int): Single<List<Character>>
}