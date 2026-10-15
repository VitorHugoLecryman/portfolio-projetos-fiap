package br.com.fiap.inovagab.model

data class DashboardSummary(
    val totalProjetos: Int = 0,
    val projetosAtivos: Int = 0,
    val projetosConcluidos: Int = 0,
    val projetosCancelados: Int = 0,
    val investimentoTotal: Double = 0.0,
    val retornoTotal: Double = 0.0,
    val lucroTotal: Double = 0.0,
    val roiGeral: Double = 0.0,
    val ganhoMedioProdutividade: Double = 0.0,
    val distribuicaoPorEtapa: Map<String, Int> = emptyMap(),
    val ideiasPorStatus: Map<String, Int> = emptyMap()
)

data class EstrategiaResumo(
    val orientacaoId: String = "",
    val orientacaoTitulo: String = "",
    val totalProjetos: Int = 0,
    val projetosConcluidos: Int = 0,
    val investimento: Double = 0.0,
    val retorno: Double = 0.0,
    val lucro: Double = 0.0,
    val roi: Double = 0.0
)

data class InsightsIa(
    val analise: String = "",
    val sugestoes: List<String> = emptyList()
)

data class AvaliacaoIa(
    val ideiaId: String = "",
    val score: Int = 0,
    val justificativa: String = ""
)
