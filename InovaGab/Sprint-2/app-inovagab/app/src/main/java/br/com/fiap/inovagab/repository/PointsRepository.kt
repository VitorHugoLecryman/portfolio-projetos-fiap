package br.com.fiap.inovagab.repository

import br.com.fiap.inovagab.model.PointTransaction
import br.com.fiap.inovagab.network.ApiClient
import br.com.fiap.inovagab.network.paraModel

class PointsRepository {

    private val api = ApiClient.api

    fun listarHistorico(
        usuarioId: String,
        onSuccess: (List<PointTransaction>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.historicoPontos(),
            mensagemErro = "Erro ao carregar histórico",
            onSuccess = { lista -> onSuccess(lista.orEmpty().map { it.paraModel() }) },
            onError = onError
        )
    }

    fun listarRanking(
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.ranking(10),
            mensagemErro = "Erro ao carregar ranking",
            onSuccess = { lista ->
                onSuccess(lista.orEmpty().map { item ->
                    mapOf(
                        "posicao" to item.posicao,
                        "nome" to item.nome,
                        "pontos" to item.pontos
                    )
                })
            },
            onError = onError
        )
    }
}
