package com.esgomez.rickandmorty.requestmanager

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_EPISODE
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_GENDER
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_ID
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_IMAGE
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_LOCATION
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_NAME
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_ORIGIN
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_RESULTS
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_SPECIES
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_STATUS
import com.esgomez.rickandmorty.requestmanager.APIConstants.KEY_URL
import kotlinx.android.parcel.Parcelize

data class CharacterResponseServer(
    @SerializedName(KEY_RESULTS) val results: List<CharacterServer>
)

@Parcelize
data class CharacterServer(
    @SerializedName(KEY_ID) val id: Int,
    @SerializedName(KEY_NAME) val name: String,
    @SerializedName(KEY_IMAGE) val image: String?,
    @SerializedName(KEY_GENDER) val gender: String,
    @SerializedName(KEY_SPECIES) val species: String,
    @SerializedName(KEY_STATUS) val status: String,
    @SerializedName(KEY_ORIGIN) val origin: OriginServer,
    @SerializedName(KEY_LOCATION) val location: LocationServer,
    @SerializedName(KEY_EPISODE) val episodeList: List<String>
): Parcelable

@Parcelize
data class LocationServer(
    @SerializedName(KEY_NAME) val name: String,
    @SerializedName(KEY_URL) val url: String
): Parcelable

@Parcelize
data class OriginServer(
    @SerializedName(KEY_NAME) val name: String,
    @SerializedName(KEY_URL) val url: String
): Parcelable

data class EpisodeServer(
    @SerializedName(KEY_ID) val id: Int,
    @SerializedName(KEY_NAME) val name: String
)
