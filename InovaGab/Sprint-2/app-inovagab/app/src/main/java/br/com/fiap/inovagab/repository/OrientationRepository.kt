package br.com.fiap.inovagab.repository

import br.com.fiap.inovagab.model.StrategicOrientation
import br.com.fiap.inovagab.network.ApiClient
import br.com.fiap.inovagab.network.OrientacaoRequestDto
import br.com.fiap.inovagab.network.paraModel

class OrientationRepository {

    private val api = ApiClient.api

    fun criarOrientacao(
        orientation: StrategicOrientation,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.criarOrientacao(
                OrientacaoRequestDto(
                    titulo = orientation.titulo,
                    descricao = orientation.descricao,
                    categoria = orientation.categoria,
                    campanha = orientation.campanha.ifBlank { null }
                )
            ),
            mensagemErro = "Erro ao criar orientação",
            onSuccess = { dto -> onSuccess(dto?.id ?: "") },
            onError = onError
        )
    }

    fun listarOrientacoes(
        onSuccess: (List<StrategicOrientation>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.listarOrientacoes(),
            mensagemErro = "Erro ao carregar orientações",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun listarHistorico(
        categoria: String?,
        onSuccess: (List<StrategicOrientation>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.historicoOrientacoes(categoria?.ifBlank { null }),
            mensagemErro = "Erro ao carregar histórico de orientações",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun atualizarOrientacao(
        id: String,
        titulo: String,
        descricao: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        atualizarOrientacao(id, titulo, descricao, "", "", onSuccess, onError)
    }

    fun atualizarOrientacao(
        id: String,
        titulo: String,
        descricao: String,
        categoria: String,
        campanha: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val enviar = { categoriaFinal: String ->
            ApiClient.executar(
                chamada = api.atualizarOrientacao(
                    id,
                    OrientacaoRequestDto(
                        titulo = titulo,
                        descricao = descricao,
                        categoria = categoriaFinal,
                        campanha = campanha.ifBlank { null }
                    )
                ),
                mensagemErro = "Erro ao atualizar orientação",
                onSuccess = { onSuccess() },
                onError = onError
            )
        }

        if (categoria.isNotBlank()) {
            enviar(categoria)
            return
        }

        ApiClient.executar(
            chamada = api.buscarOrientacao(id),
            mensagemErro = "Erro ao atualizar orientação",
            onSuccess = { atual -> enviar(atual?.categoria.orEmpty().ifBlank { "Geral" }) },
            onError = onError
        )
    }

    fun excluirOrientacao(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.desativarOrientacao(id),
            mensagemErro = "Erro ao excluir orientação",
            onSuccess = { onSuccess() },
            onError = onError
        )
    }
}
