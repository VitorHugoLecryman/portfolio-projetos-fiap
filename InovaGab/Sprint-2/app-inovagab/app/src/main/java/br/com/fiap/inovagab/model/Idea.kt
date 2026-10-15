package br.com.fiap.inovagab.model

import java.util.Date

data class Idea(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val status: String = "Enviada", // "Enviada", "Aprovada", "Rejeitada"
    val autorId: String = "",
    val autorNome: String = "",
    val dataEnvio: Date? = null,
    val impacto: String = "", // "Alto", "Médio", "Baixo"
    val esforco: String = "", // "Alto", "Médio", "Baixo"
    val motivoRejeicao: String = "",
    val orientacaoId: String = "",
    val orientacaoTitulo: String = "",
    val prioridade: Int? = null,
    val scoreIa: Int? = null,
    val justificativaIa: String = ""
)
