package com.esgomez.rickandmorty.api

import com.esgomez.rickandmorty.database.CharacterEntity
import com.esgomez.rickandmorty.database.LocationEntity
import com.esgomez.rickandmorty.database.OriginEntity

fun CharacterResponseServer.toCharacterServerList(): List<CharacterServer> = results.map {
    it.run{
        CharacterServer(
            id,
            name,
            image,
            gender,
            species,
            status,
            origin,
            location,
            episodeList.map { episode -> "$episode/" }
        )
    }
}

fun CharacterServer.toCharacterEntity() = CharacterEntity(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginEntity(),
    location.toLocationEntity(),
    episodeList
)

fun OriginServer.toOriginEntity() = OriginEntity(
    name,
    url
)

fun LocationServer.toLocationEntity() = LocationEntity(
    name,
    url
)
