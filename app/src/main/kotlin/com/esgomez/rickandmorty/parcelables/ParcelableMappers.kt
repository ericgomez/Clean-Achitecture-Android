package com.esgomez.rickandmorty.parcelables

import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Location
import com.esgomez.rickandmorty.domain.Origin

fun Character.toCharacterParcelable() = CharacterParcelable(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginParcelable(),
    location.toLocationParcelable(),
    episodeList
)

fun Location.toLocationParcelable() = LocationParcelable(
    name,
    url
)

fun Origin.toOriginParcelable() = OriginParcelable(
    name,
    url
)

fun CharacterParcelable.toCharacterDomain() = Character(
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

fun LocationParcelable.toLocationDomain() = Location(
    name,
    url
)

fun OriginParcelable.toOriginDomain() = Origin(
    name,
    url
)