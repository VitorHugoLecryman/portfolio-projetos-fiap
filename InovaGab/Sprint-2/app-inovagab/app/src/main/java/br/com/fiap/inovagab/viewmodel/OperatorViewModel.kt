package br.com.fiap.inovagab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.model.Idea
import br.com.fiap.inovagab.model.PointTransaction
import br.com.fiap.inovagab.model.StrategicOrientation
import br.com.fiap.inovagab.repository.IdeaRepository
import br.com.fiap.inovagab.repository.OrientationRepository
import br.com.fiap.inovagab.repository.PointsRepository

class OperatorViewModel : ViewModel() {

    private val ideaRepository = IdeaRepository()
    private val pointsRepository = PointsRepository()
    private val orientationRepository = OrientationRepository()

    private val _minhasIdeias = MutableLiveData<List<Idea>>()
    val minhasIdeias: LiveData<List<Idea>> = _minhasIdeias

    private val _orientacoes = MutableLiveData<List<StrategicOrientation>>()
    val orientacoes: LiveData<List<StrategicOrientation>> = _orientacoes

    private val _historicoPontos = MutableLiveData<List<PointTransaction>>()
    val historicoPontos: LiveData<List<PointTransaction>> = _historicoPontos

    private val _ranking = MutableLiveData<List<Map<String, Any>>>()
    val ranking: LiveData<List<Map<String, Any>>> = _ranking

    private val _mensagem = MutableLiveData<String?>()
    val mensagem: LiveData<String?> = _mensagem

    private val _carregando = MutableLiveData<Boolean>()
    val carregando: LiveData<Boolean> = _carregando

    fun enviarIdeia(
        titulo: String,
        descricao: String,
        categoria: String,
        orientacaoId: String,
        autorId: String,
        autorNome: String
    ) {
        if (titulo.isBlank() || descricao.isBlank() || categoria.isBlank()) {
            _mensagem.value = "Preencha todos os campos"
            return
        }
        if (orientacaoId.isBlank()) {
            _mensagem.value = "Selecione a orientação estratégica"
            return
        }
        if (titulo.length > 100) {
            _mensagem.value = "Título deve ter no máximo 100 caracteres"
            return
        }
        if (descricao.length > 500) {
            _mensagem.value = "Descrição deve ter no máximo 500 caracteres"
            return
        }

        _carregando.value = true
        val idea = Idea(
            titulo = titulo,
            descricao = descricao,
            categoria = categoria,
            orientacaoId = orientacaoId,
            autorId = autorId,
            autorNome = autorNome
        )
        ideaRepository.enviarIdeia(
            idea = idea,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Ideia enviada com sucesso! +10 pontos"
                carregarMinhasIdeias(autorId)
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun excluirIdeia(ideiaId: String, autorId: String) {
        _carregando.value = true
        ideaRepository.excluirIdeia(
            ideiaId = ideiaId,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Ideia excluída"
                carregarMinhasIdeias(autorId)
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun carregarMinhasIdeias(autorId: String) {
        ideaRepository.listarIdeiasDoOperador(
            autorId = autorId,
            onSuccess = { _minhasIdeias.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun carregarOrientacoes() {
        orientationRepository.listarOrientacoes(
            onSuccess = { _orientacoes.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun carregarHistoricoPontos(usuarioId: String) {
        pointsRepository.listarHistorico(
            usuarioId = usuarioId,
            onSuccess = { _historicoPontos.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun carregarRanking() {
        pointsRepository.listarRanking(
            onSuccess = { _ranking.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}
