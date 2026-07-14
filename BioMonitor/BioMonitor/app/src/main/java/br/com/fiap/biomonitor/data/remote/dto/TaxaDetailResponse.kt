package br.com.fiap.biomonitor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TaxaDetailResponse(
    @SerializedName("results") val results: List<TaxaDetailResult>
)

data class TaxaDetailResult(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("preferred_common_name") val preferredCommonName: String?,
    @SerializedName("wikipedia_summary") val wikipediaSummary: String?,
    @SerializedName("conservation_status") val conservationStatus: ConservationStatusDto?,
    @SerializedName("default_photo") val defaultPhoto: DefaultPhoto?
)

data class ConservationStatusDto(
    @SerializedName("status") val status: String?,
    @SerializedName("status_name") val statusName: String?
)
