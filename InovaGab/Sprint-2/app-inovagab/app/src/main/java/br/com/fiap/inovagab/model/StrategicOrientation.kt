package br.com.fiap.inovagab.model

import java.util.Date

data class StrategicOrientation(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val campanha: String = "",
    val autorId: String = "",
    val autorNome: String = "",
    val dataCriacao: Date? = null,
    val ativo: Boolean = true
)
