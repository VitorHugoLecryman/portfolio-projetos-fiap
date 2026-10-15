package br.com.fiap.inovagab.network

import java.util.Date

data class LoginRequestDto(
    val email: String,
    val senha: String
)

data class LoginResponseDto(
    val token: String = "",
    val expiraEmMinutos: Long = 0,
    val usuario: UsuarioDto = UsuarioDto()
)

data class UsuarioDto(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val perfil: String = "",
    val pontos: Int = 0,
    val ativo: Boolean = true
)

data class OrientacaoRequestDto(
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val campanha: String?
)

data class OrientacaoDto(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val campanha: String? = null,
    val autorId: String = "",
    val autorNome: String = "",
    val dataCriacao: Date? = null,
    val dataAtualizacao: Date? = null,
    val ativo: Boolean = true
)

data class IdeiaRequestDto(
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val orientacaoId: String
)

data class PriorizacaoRequestDto(
    val impacto: String,
    val esforco: String,
    val prioridade: Int?
)

data class AprovacaoRequestDto(
    val impacto: String,
    val esforco: String
)

data class RejeicaoRequestDto(
    val motivo: String
)

data class IdeiaDto(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val status: String = "",
    val orientacaoId: String? = null,
    val orientacaoTitulo: String? = null,
    val autorId: String = "",
    val autorNome: String = "",
    val dataEnvio: Date? = null,
    val impacto: String? = null,
    val esforco: String? = null,
    val prioridade: Int? = null,
    val motivoRejeicao: String? = null,
    val avaliadorId: String? = null,
    val avaliadorNome: String? = null,
    val dataAvaliacao: Date? = null,
    val scoreIa: Int? = null,
    val justificativaIa: String? = null
)

data class AvaliacaoIaDto(
    val ideiaId: String = "",
    val score: Int = 0,
    val justificativa: String = "",
    val modelo: String? = null
)

data class TransacaoDto(
    val id: String = "",
    val usuarioId: String = "",
    val pontos: Int = 0,
    val motivo: String = "",
    val ideiaId: String? = null,
    val data: Date? = null
)

data class RankingItemDto(
    val posicao: Int = 0,
    val usuarioId: String = "",
    val nome: String = "",
    val pontos: Int = 0
)

data class ProjetoRequestDto(
    val titulo: String,
    val descricao: String,
    val ideiaId: String?,
    val orientacaoId: String?,
    val orcamento: Double,
    val prazo: String?,
    val status: String?,
    val observacoes: String?
)

data class EtapaRequestDto(
    val etapa: String
)

data class ResultadosRequestDto(
    val retornoFinanceiro: Double,
    val ganhoProdutividade: Double,
    val resultadosQualitativos: String?
)

data class ProjetoDto(
    val id: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val ideiaId: String? = null,
    val orientacaoId: String? = null,
    val orientacaoTitulo: String? = null,
    val etapa: String = "",
    val status: String? = null,
    val orcamento: Double = 0.0,
    val prazo: String? = null,
    val gestorId: String = "",
    val gestorNome: String = "",
    val dataCriacao: Date? = null,
    val retornoFinanceiro: Double = 0.0,
    val ganhoProdutividade: Double = 0.0,
    val resultadosQualitativos: String? = null,
    val observacoes: String? = null,
    val lucro: Double = 0.0,
    val roi: Double = 0.0
)

data class ResumoDto(
    val totalProjetos: Long = 0,
    val projetosAtivos: Long = 0,
    val projetosConcluidos: Long = 0,
    val projetosCancelados: Long = 0,
    val investimentoTotal: Double = 0.0,
    val retornoTotal: Double = 0.0,
    val lucroTotal: Double = 0.0,
    val roiGeral: Double = 0.0,
    val ganhoMedioProdutividade: Double = 0.0,
    val distribuicaoPorEtapa: Map<String, Long> = emptyMap(),
    val ideiasPorStatus: Map<String, Long> = emptyMap()
)

data class EstrategiaDto(
    val orientacaoId: String? = null,
    val orientacaoTitulo: String? = null,
    val totalProjetos: Long = 0,
    val projetosConcluidos: Long = 0,
    val investimento: Double = 0.0,
    val retorno: Double = 0.0,
    val lucro: Double = 0.0,
    val roi: Double = 0.0
)

data class ProjetoResumoDto(
    val id: String = "",
    val titulo: String = "",
    val etapa: String = "",
    val prazo: String? = null,
    val investimento: Double = 0.0,
    val retorno: Double = 0.0,
    val lucro: Double = 0.0,
    val roi: Double = 0.0
)

data class InsightsDto(
    val analise: String = "",
    val sugestoes: List<String> = emptyList(),
    val modelo: String? = null
)

data class AuditoriaDto(
    val id: String = "",
    val usuarioId: String = "",
    val usuarioNome: String = "",
    val perfil: String = "",
    val acao: String = "",
    val entidade: String = "",
    val entidadeId: String? = null,
    val data: Date? = null,
    val detalhes: String? = null
)

data class ErroDto(
    val timestamp: String? = null,
    val status: Int = 0,
    val erro: String? = null,
    val mensagem: String? = null
)
