package com.esgomez.rickandmorty.requestmanager

import com.esgomez.rickandmorty.domain.Character
import com.esgomez.rickandmorty.domain.Episode
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

//Paso 8: Crear mapper para cambiar de episodio del servidor a episodio de dominio
fun EpisodeServer.toEpisodeDomain() = Episode(
    id, name
)