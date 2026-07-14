package br.com.fiap.biomonitor.domain.model

enum class PointType(val points: Int) {
    COMMON_SIGHTING(10),
    NEW_SPECIES(25),
    RARE_SPECIES(50),
    CHALLENGE_COMPLETED(100)
}
