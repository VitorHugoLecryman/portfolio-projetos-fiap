package br.com.fiap.biomonitor.domain.model

data class Badge(
    val id: Long,
    val name: String,
    val description: String,
    val iconResName: String,
    val requirement: BadgeRequirement
)

sealed class BadgeRequirement {
    data class SightingCount(val count: Int) : BadgeRequirement()
    data class UniqueSpeciesCount(val count: Int, val category: Category?) : BadgeRequirement()
    data class CitiesCount(val count: Int) : BadgeRequirement()
}
