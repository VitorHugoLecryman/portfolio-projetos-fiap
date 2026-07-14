package br.com.fiap.biomonitor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SpeciesResponse(
    @SerializedName("key") val key: Long,
    @SerializedName("scientificName") val scientificName: String?,
    @SerializedName("canonicalName") val canonicalName: String?,
    @SerializedName("vernacularName") val vernacularName: String?,
    @SerializedName("threatStatus") val threatStatus: String?
)

data class SpeciesSearchResponse(
    @SerializedName("results") val results: List<SpeciesResponse>
)
