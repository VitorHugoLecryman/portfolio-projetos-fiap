package br.com.fiap.biomonitor.domain.model

data class Species(
    val id: Long,
    val name: String,
    val scientificName: String?,
    val commonName: String?,
    val iconUrl: String?,
    val conservationStatus: String? = null
)
