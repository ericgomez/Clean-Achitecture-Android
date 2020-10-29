package com.esgomez.rickandmorty.api

import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.database.LocationEntity
import com.esgomez.rickandmorty.database.OriginEntity
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Location
import com.esgomez.rickandmorty.domain.Origin

fun CharacterResponseServer.toCharacterDomainList(): List<Character> = results.map {
    it.run{
        Character(
            id,
            name,
            image,
            gender,
            species,
            status,
            origin.toOriginDomain(),
            location.toLocationDomain(),
            episodeList.map { episode -> "$episode/" }
        )
    }
}

fun OriginServer.toOriginDomain() = Origin(
    name,
    url
)

fun LocationServer.toLocationDomain() = Location(
    name,
    url
)
