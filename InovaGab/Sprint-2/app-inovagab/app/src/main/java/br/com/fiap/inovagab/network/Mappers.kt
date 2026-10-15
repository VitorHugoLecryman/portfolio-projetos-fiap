package br.com.fiap.inovagab.network

import br.com.fiap.inovagab.model.DashboardSummary
import br.com.fiap.inovagab.model.EstrategiaResumo
import br.com.fiap.inovagab.model.Idea
import br.com.fiap.inovagab.model.PointTransaction
import br.com.fiap.inovagab.model.Project
import br.com.fiap.inovagab.model.StrategicOrientation
import br.com.fiap.inovagab.model.User

fun perfilParaApp(perfil: String?): String = when (perfil?.uppercase()) {
    "OPERADOR" -> "operador"
    "GESTOR" -> "gestor"
    "LIDER" -> "lider"
    else -> ""
}

fun statusParaApp(status: String?): String = when (status?.uppercase()) {
    "ENVIADA" -> "Enviada"
    "APROVADA" -> "Aprovada"
    "REJEITADA" -> "Rejeitada"
    else -> ""
}

fun statusParaApi(status: String?): String? = when (status?.lowercase()) {
    "enviada" -> "ENVIADA"
    "aprovada" -> "APROVADA"
    "rejeitada" -> "REJEITADA"
    else -> null
}

fun nivelParaApp(nivel: String?): String = when (nivel?.uppercase()) {
    "ALTO" -> "Alto"
    "MEDIO" -> "Médio"
    "BAIXO" -> "Baixo"
    else -> ""
}

fun nivelParaApi(nivel: String?): String = when (nivel?.lowercase()) {
    "alto" -> "ALTO"
    "médio", "medio" -> "MEDIO"
    else -> "BAIXO"
}

fun etapaParaApp(etapa: String?): String = when (etapa?.uppercase()) {
    "PLANEJAMENTO" -> "Planejamento"
    "EM_ANDAMENTO" -> "Em Andamento"
    "CONCLUIDO" -> "Concluído"
    "CANCELADO" -> "Cancelado"
    else -> ""
}

fun etapaParaApi(etapa: String?): String = when (etapa?.lowercase()) {
    "planejamento" -> "PLANEJAMENTO"
    "em andamento" -> "EM_ANDAMENTO"
    "concluído", "concluido" -> "CONCLUIDO"
    "cancelado" -> "CANCELADO"
    else -> "PLANEJAMENTO"
}

fun UsuarioDto.paraModel(): User = User(
    uid = id,
    nome = nome,
    email = email,
    perfil = perfilParaApp(perfil),
    pontos = pontos
)

fun OrientacaoDto.paraModel(): StrategicOrientation = StrategicOrientation(
    id = id,
    titulo = titulo,
    descricao = descricao,
    categoria = categoria,
    campanha = campanha ?: "",
    autorId = autorId,
    autorNome = autorNome,
    dataCriacao = dataCriacao,
    ativo = ativo
)

fun IdeiaDto.paraModel(): Idea = Idea(
    id = id,
    titulo = titulo,
    descricao = descricao,
    categoria = categoria,
    status = statusParaApp(status),
    autorId = autorId,
    autorNome = autorNome,
    dataEnvio = dataEnvio,
    impacto = nivelParaApp(impacto),
    esforco = nivelParaApp(esforco),
    motivoRejeicao = motivoRejeicao ?: "",
    orientacaoId = orientacaoId ?: "",
    orientacaoTitulo = orientacaoTitulo ?: "",
    prioridade = prioridade,
    scoreIa = scoreIa,
    justificativaIa = justificativaIa ?: ""
)

fun TransacaoDto.paraModel(): PointTransaction = PointTransaction(
    id = id,
    usuarioId = usuarioId,
    pontos = pontos,
    motivo = motivo,
    data = data,
    ideiaId = ideiaId ?: ""
)

fun ProjetoDto.paraModel(): Project = Project(
    id = id,
    titulo = titulo,
    descricao = descricao,
    ideiaId = ideiaId ?: "",
    orientacaoId = orientacaoId ?: "",
    orientacaoTitulo = orientacaoTitulo ?: "",
    etapa = etapaParaApp(etapa),
    status = status ?: "",
    orcamento = orcamento,
    prazo = prazo ?: "",
    gestorId = gestorId,
    gestorNome = gestorNome,
    dataCriacao = dataCriacao,
    retornoFinanceiro = retornoFinanceiro,
    ganhoProdutividade = ganhoProdutividade,
    resultadosQualitativos = resultadosQualitativos ?: "",
    observacoes = observacoes ?: "",
    lucro = lucro,
    roi = roi
)

fun ResumoDto.paraModel(): DashboardSummary = DashboardSummary(
    totalProjetos = totalProjetos.toInt(),
    projetosAtivos = projetosAtivos.toInt(),
    projetosConcluidos = projetosConcluidos.toInt(),
    projetosCancelados = projetosCancelados.toInt(),
    investimentoTotal = investimentoTotal,
    retornoTotal = retornoTotal,
    lucroTotal = lucroTotal,
    roiGeral = roiGeral,
    ganhoMedioProdutividade = ganhoMedioProdutividade,
    distribuicaoPorEtapa = distribuicaoPorEtapa.mapKeys { etapaParaApp(it.key) }
        .mapValues { it.value.toInt() },
    ideiasPorStatus = ideiasPorStatus.mapKeys { statusParaApp(it.key) }
        .mapValues { it.value.toInt() }
)

fun EstrategiaDto.paraModel(): EstrategiaResumo = EstrategiaResumo(
    orientacaoId = orientacaoId ?: "",
    orientacaoTitulo = orientacaoTitulo ?: "Sem orientação vinculada",
    totalProjetos = totalProjetos.toInt(),
    projetosConcluidos = projetosConcluidos.toInt(),
    investimento = investimento,
    retorno = retorno,
    lucro = lucro,
    roi = roi
)
