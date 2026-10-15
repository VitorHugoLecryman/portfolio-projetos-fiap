package br.com.fiap.inovagab.model

import java.util.Date

data class Project(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val ideiaId: String = "",
    val orientacaoId: String = "",
    val orientacaoTitulo: String = "",
    val etapa: String = "Planejamento", // "Planejamento", "Em Andamento", "Concluído", "Cancelado"
    val status: String = "Ativo",
    val orcamento: Double = 0.0,
    val prazo: String = "",
    val gestorId: String = "",
    val gestorNome: String = "",
    val dataCriacao: Date? = null,
    val retornoFinanceiro: Double = 0.0,
    val ganhoProdutividade: Double = 0.0,
    val resultadosQualitativos: String = "",
    val observacoes: String = "",
    val lucro: Double = 0.0,
    val roi: Double = 0.0
)
