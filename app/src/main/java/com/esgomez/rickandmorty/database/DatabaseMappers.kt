package com.esgomez.rickandmorty.database

import com.esgomez.rickandmorty.api.CharacterServer
import com.esgomez.rickandmorty.api.LocationServer
import com.esgomez.rickandmorty.api.OriginServer

fun CharacterEntity.toCharacterServer() = CharacterServer(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginServer(),
    location.toLocationServer(),
    episodeList
)

fun OriginEntity.toOriginServer() = OriginServer(
    originName,
    originUrl
)

fun LocationEntity.toLocationServer() = LocationServer(
    locationName,
    locationUrl
)
