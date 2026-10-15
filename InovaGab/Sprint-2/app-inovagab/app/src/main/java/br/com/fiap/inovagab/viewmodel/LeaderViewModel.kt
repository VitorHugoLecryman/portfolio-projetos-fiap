package br.com.fiap.inovagab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.model.EstrategiaResumo
import br.com.fiap.inovagab.model.InsightsIa
import br.com.fiap.inovagab.model.Project
import br.com.fiap.inovagab.model.StrategicOrientation
import br.com.fiap.inovagab.repository.OrientationRepository
import br.com.fiap.inovagab.repository.ProjectRepository

class LeaderViewModel : ViewModel() {

    private val orientationRepository = OrientationRepository()
    private val projectRepository = ProjectRepository()

    private val _orientacoes = MutableLiveData<List<StrategicOrientation>>()
    val orientacoes: LiveData<List<StrategicOrientation>> = _orientacoes

    private val _projetos = MutableLiveData<List<Project>>()
    val projetos: LiveData<List<Project>> = _projetos

    private val _porEstrategia = MutableLiveData<List<EstrategiaResumo>>()
    val porEstrategia: LiveData<List<EstrategiaResumo>> = _porEstrategia

    private val _insightsIa = MutableLiveData<InsightsIa?>()
    val insightsIa: LiveData<InsightsIa?> = _insightsIa

    private val _mensagem = MutableLiveData<String?>()
    val mensagem: LiveData<String?> = _mensagem

    private val _carregando = MutableLiveData<Boolean>()
    val carregando: LiveData<Boolean> = _carregando

    private val _totalProjetos = MutableLiveData<Int>()
    val totalProjetos: LiveData<Int> = _totalProjetos

    private val _projetosAtivos = MutableLiveData<Int>()
    val projetosAtivos: LiveData<Int> = _projetosAtivos

    private val _projetosConcluidos = MutableLiveData<Int>()
    val projetosConcluidos: LiveData<Int> = _projetosConcluidos

    private val _investimentoTotal = MutableLiveData<Double>()
    val investimentoTotal: LiveData<Double> = _investimentoTotal

    private val _roiGeral = MutableLiveData<Double>()
    val roiGeral: LiveData<Double> = _roiGeral

    private val _retornoTotal = MutableLiveData<Double>()
    val retornoTotal: LiveData<Double> = _retornoTotal

    private val _lucroTotal = MutableLiveData<Double>()
    val lucroTotal: LiveData<Double> = _lucroTotal

    private val _ganhoMedioProdutividade = MutableLiveData<Double>()
    val ganhoMedioProdutividade: LiveData<Double> = _ganhoMedioProdutividade

    private val _distribuicaoPorEtapa = MutableLiveData<Map<String, Int>>()
    val distribuicaoPorEtapa: LiveData<Map<String, Int>> = _distribuicaoPorEtapa

    private val _ideiasPorStatus = MutableLiveData<Map<String, Int>>()
    val ideiasPorStatus: LiveData<Map<String, Int>> = _ideiasPorStatus

    fun criarOrientacao(
        titulo: String,
        descricao: String,
        categoria: String,
        campanha: String,
        autorId: String,
        autorNome: String
    ) {
        if (titulo.isBlank() || descricao.isBlank()) {
            _mensagem.value = "Preencha título e descrição"
            return
        }
        if (categoria.isBlank()) {
            _mensagem.value = "Informe a categoria"
            return
        }
        if (titulo.length > 150) {
            _mensagem.value = "Título deve ter no máximo 150 caracteres"
            return
        }
        if (descricao.length > 1000) {
            _mensagem.value = "Descrição deve ter no máximo 1000 caracteres"
            return
        }
        _carregando.value = true
        val orientacao = StrategicOrientation(
            titulo = titulo,
            descricao = descricao,
            categoria = categoria,
            campanha = campanha,
            autorId = autorId,
            autorNome = autorNome
        )
        orientationRepository.criarOrientacao(
            orientation = orientacao,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Orientação criada com sucesso"
                carregarOrientacoes()
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun atualizarOrientacao(id: String, titulo: String, descricao: String) {
        atualizarOrientacao(id, titulo, descricao, "", "")
    }

    fun atualizarOrientacao(
        id: String,
        titulo: String,
        descricao: String,
        categoria: String,
        campanha: String
    ) {
        if (titulo.isBlank() || descricao.isBlank()) {
            _mensagem.value = "Preencha título e descrição"
            return
        }
        _carregando.value = true
        orientationRepository.atualizarOrientacao(
            id = id,
            titulo = titulo,
            descricao = descricao,
            categoria = categoria,
            campanha = campanha,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Orientação atualizada"
                carregarOrientacoes()
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun excluirOrientacao(id: String) {
        _carregando.value = true
        orientationRepository.excluirOrientacao(
            id = id,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Orientação excluída"
                carregarOrientacoes()
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun carregarOrientacoes() {
        orientationRepository.listarOrientacoes(
            onSuccess = { _orientacoes.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun carregarHistoricoOrientacoes(categoria: String?) {
        orientationRepository.listarHistorico(
            categoria = categoria,
            onSuccess = { _orientacoes.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun carregarProjetos() {
        projectRepository.carregarResumo(
            onSuccess = { resumo ->
                _totalProjetos.value = resumo.totalProjetos
                _projetosAtivos.value = resumo.projetosAtivos
                _projetosConcluidos.value = resumo.projetosConcluidos
                _investimentoTotal.value = resumo.investimentoTotal
                _retornoTotal.value = resumo.retornoTotal
                _lucroTotal.value = resumo.lucroTotal
                _roiGeral.value = resumo.roiGeral
                _ganhoMedioProdutividade.value = resumo.ganhoMedioProdutividade
                _distribuicaoPorEtapa.value = resumo.distribuicaoPorEtapa
                _ideiasPorStatus.value = resumo.ideiasPorStatus
            },
            onError = { _mensagem.value = it }
        )

        projectRepository.listarProjetos(
            onSuccess = { _projetos.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun carregarPorEstrategia() {
        projectRepository.carregarPorEstrategia(
            onSuccess = { _porEstrategia.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun gerarInsightsIa() {
        _carregando.value = true
        projectRepository.carregarInsightsIa(
            onSuccess = { insights ->
                _carregando.value = false
                _insightsIa.value = insights
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun limparInsightsIa() {
        _insightsIa.value = null
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}
