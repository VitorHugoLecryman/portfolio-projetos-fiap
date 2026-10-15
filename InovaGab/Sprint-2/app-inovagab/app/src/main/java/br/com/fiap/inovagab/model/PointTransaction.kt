package br.com.fiap.inovagab.model

import java.util.Date

data class PointTransaction(
    val id: String = "",
    val usuarioId: String = "",
    val pontos: Int = 0,
    val motivo: String = "", // "Envio de Ideia", "Ideia Aprovada"
    val data: Date? = null,
    val ideiaId: String = ""
)
