package br.com.fiap.biomonitor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaxaAutocompleteResponse(
    @SerializedName("results") val results: List<TaxaResult>
)

data class TaxaResult(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("preferred_common_name") val preferredCommonName: String?,
    @SerializedName("default_photo") val defaultPhoto: DefaultPhoto?,
    @SerializedName("iconic_taxon_name") val iconicTaxonName: String?
)

data class DefaultPhoto(
    @SerializedName("square_url") val squareUrl: String?,
    @SerializedName("medium_url") val mediumUrl: String?
)
