package br.com.fiap.biomonitor.domain.model

data class RankingEntry(
    val position: Int,
    val user: User,
    val points: Int
)
