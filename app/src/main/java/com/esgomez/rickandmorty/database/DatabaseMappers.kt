package com.esgomez.rickandmorty.database

import com.esgomez.rickandmorty.api.CharacterServer
import com.esgomez.rickandmorty.api.LocationServer
import com.esgomez.rickandmorty.api.OriginServer
import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Location
import com.esgomez.rickandmorty.domain.Origin

fun List<CharacterEntity>.toCharacterDomainList() = map(CharacterEntity::toCharacterDomain)

fun CharacterEntity.toCharacterDomain() = Character(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginDomain(),
    location.toLocationDomain(),
    episodeList
)

fun OriginEntity.toOriginDomain() = Origin(
    originName,
    originUrl
)

fun LocationEntity.toLocationDomain() = Location(
    locationName,
    locationUrl
)

/**
 *
 */

fun Character.toCharacterEntity() = CharacterEntity(
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

fun Origin.toOriginEntity() = OriginEntity(
    name,
    url
)

fun Location.toLocationEntity() = LocationEntity(
    name,
    url
)
