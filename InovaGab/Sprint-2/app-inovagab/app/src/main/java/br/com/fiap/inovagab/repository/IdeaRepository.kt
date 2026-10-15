package br.com.fiap.inovagab.repository

import br.com.fiap.inovagab.model.AvaliacaoIa
import br.com.fiap.inovagab.model.Idea
import br.com.fiap.inovagab.network.ApiClient
import br.com.fiap.inovagab.network.AprovacaoRequestDto
import br.com.fiap.inovagab.network.IdeiaRequestDto
import br.com.fiap.inovagab.network.PriorizacaoRequestDto
import br.com.fiap.inovagab.network.RejeicaoRequestDto
import br.com.fiap.inovagab.network.nivelParaApi
import br.com.fiap.inovagab.network.paraModel

class IdeaRepository {

    private val api = ApiClient.api

    fun enviarIdeia(
        idea: Idea,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.criarIdeia(
                IdeiaRequestDto(
                    titulo = idea.titulo,
                    descricao = idea.descricao,
                    categoria = idea.categoria,
                    orientacaoId = idea.orientacaoId
                )
            ),
            mensagemErro = "Erro ao enviar ideia",
            onSuccess = { dto -> onSuccess(dto?.id ?: "") },
            onError = onError
        )
    }

    fun atualizarIdeia(
        ideiaId: String,
        idea: Idea,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.atualizarIdeia(
                ideiaId,
                IdeiaRequestDto(
                    titulo = idea.titulo,
                    descricao = idea.descricao,
                    categoria = idea.categoria,
                    orientacaoId = idea.orientacaoId
                )
            ),
            mensagemErro = "Erro ao atualizar ideia",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun excluirIdeia(
        ideiaId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.excluirIdeia(ideiaId),
            mensagemErro = "Erro ao excluir ideia",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun listarIdeiasDoOperador(
        autorId: String,
        onSuccess: (List<Idea>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.minhasIdeias(),
            mensagemErro = "Erro ao carregar ideias",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun listarIdeiasEnviadas(
        onSuccess: (List<Idea>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.listarIdeias("ENVIADA"),
            mensagemErro = "Erro ao carregar ideias",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun listarTodasIdeias(
        onSuccess: (List<Idea>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.listarIdeias(null),
            mensagemErro = "Erro ao carregar ideias",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun priorizarIdeia(
        ideiaId: String,
        impacto: String,
        esforco: String,
        prioridade: Int?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.priorizarIdeia(
                ideiaId,
                PriorizacaoRequestDto(nivelParaApi(impacto), nivelParaApi(esforco), prioridade)
            ),
            mensagemErro = "Erro ao priorizar ideia",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun aprovarIdeia(
        ideiaId: String,
        impacto: String,
        esforco: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.aprovarIdeia(
                ideiaId,
                AprovacaoRequestDto(nivelParaApi(impacto), nivelParaApi(esforco))
            ),
            mensagemErro = "Erro ao aprovar ideia",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun rejeitarIdeia(
        ideiaId: String,
        motivo: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.rejeitarIdeia(ideiaId, RejeicaoRequestDto(motivo)),
            mensagemErro = "Erro ao rejeitar ideia",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }

    fun avaliarComIa(
        ideiaId: String,
        onSuccess: (AvaliacaoIa) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.avaliarIdeiaComIa(ideiaId),
            mensagemErro = "Erro ao avaliar a ideia com IA",
            onSuccess = { dto ->
                if (dto == null) {
                    onError("A IA não retornou uma avaliação")
                    return@executar
                }
                onSuccess(AvaliacaoIa(dto.ideiaId, dto.score, dto.justificativa))
            },
            onError = onError
        )
    }
}
