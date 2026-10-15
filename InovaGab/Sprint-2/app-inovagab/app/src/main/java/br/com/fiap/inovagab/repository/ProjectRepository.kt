package br.com.fiap.inovagab.repository

import br.com.fiap.inovagab.model.DashboardSummary
import br.com.fiap.inovagab.model.EstrategiaResumo
import br.com.fiap.inovagab.model.InsightsIa
import br.com.fiap.inovagab.model.Project
import br.com.fiap.inovagab.network.ApiClient
import br.com.fiap.inovagab.network.EtapaRequestDto
import br.com.fiap.inovagab.network.ProjetoRequestDto
import br.com.fiap.inovagab.network.ResultadosRequestDto
import br.com.fiap.inovagab.network.etapaParaApi
import br.com.fiap.inovagab.network.paraModel

class ProjectRepository {

    private val api = ApiClient.api

    fun criarProjeto(
        project: Project,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.criarProjeto(
                ProjetoRequestDto(
                    titulo = project.titulo,
                    descricao = project.descricao,
                    ideiaId = project.ideiaId.ifBlank { null },
                    orientacaoId = project.orientacaoId.ifBlank { null },
                    orcamento = project.orcamento,
                    prazo = project.prazo.ifBlank { null },
                    status = project.status.ifBlank { null },
                    observacoes = project.observacoes.ifBlank { null }
                )
            ),
            mensagemErro = "Erro ao criar projeto",
            onSuccess = { dto -> onSuccess(dto?.id ?: "") },
            onError = onError
        )
    }

    fun listarProjetos(
        onSuccess: (List<Project>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.listarProjetos(),
            mensagemErro = "Erro ao carregar projetos",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun alterarEtapa(
        projectId: String,
        novaEtapa: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.alterarEtapaProjeto(projectId, EtapaRequestDto(etapaParaApi(novaEtapa))),
            mensagemErro = "Erro ao atualizar projeto",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun excluirProjeto(
        projectId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.excluirProjeto(projectId),
            mensagemErro = "Erro ao excluir projeto",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun adicionarResultados(
        projectId: String,
        retornoFinanceiro: Double,
        ganhoProdutividade: Double,
        resultadosQualitativos: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.registrarResultados(
                projectId,
                ResultadosRequestDto(
                    retornoFinanceiro = retornoFinanceiro,
                    ganhoProdutividade = ganhoProdutividade,
                    resultadosQualitativos = resultadosQualitativos.ifBlank { null }
                )
            ),
            mensagemErro = "Erro ao adicionar resultados",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun carregarResumo(
        onSuccess: (DashboardSummary) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.dashboardResumo(),
            mensagemErro = "Erro ao carregar o painel",
            onSuccess = { dto -> onSuccess(dto?.paraModel() ?: DashboardSummary()) },
            onError = onError
        )
    }

    fun carregarPorEstrategia(
        onSuccess: (List<EstrategiaResumo>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.dashboardPorEstrategia(),
            mensagemErro = "Erro ao carregar os indicadores por estratégia",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun carregarInsightsIa(
        onSuccess: (InsightsIa) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.insightsIa(),
            mensagemErro = "Erro ao gerar os insights de IA",
            onSuccess = { dto ->
                if (dto == null) {
                    onError("A IA não retornou uma análise")
                    return@executar
                }
                onSuccess(InsightsIa(dto.analise, dto.sugestoes))
            },
            onError = onError
        )
    }
}
