package br.com.fiap.biomonitor.domain.model

import java.time.LocalDateTime

data class Sighting(
    val id: Long = 0,
    val userId: Long,
    val photoPath: String,
    val category: Category,
    val speciesName: String,
    val scientificName: String? = null,
    val latitude: Double,
    val longitude: Double,
    val dateTime: LocalDateTime,
    val habitat: Habitat? = null,
    val observations: String = "",
    val syncStatus: SyncStatus = SyncStatus.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
