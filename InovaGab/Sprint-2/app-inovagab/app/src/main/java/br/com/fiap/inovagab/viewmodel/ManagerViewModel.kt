package br.com.fiap.inovagab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.model.AvaliacaoIa
import br.com.fiap.inovagab.model.Idea
import br.com.fiap.inovagab.model.Project
import br.com.fiap.inovagab.model.StrategicOrientation
import br.com.fiap.inovagab.repository.IdeaRepository
import br.com.fiap.inovagab.repository.OrientationRepository
import br.com.fiap.inovagab.repository.ProjectRepository

class ManagerViewModel : ViewModel() {

    private val ideaRepository = IdeaRepository()
    private val projectRepository = ProjectRepository()
    private val orientationRepository = OrientationRepository()

    private val _ideiasEnviadas = MutableLiveData<List<Idea>>()
    val ideiasEnviadas: LiveData<List<Idea>> = _ideiasEnviadas

    private val _todasIdeias = MutableLiveData<List<Idea>>()
    val todasIdeias: LiveData<List<Idea>> = _todasIdeias

    private val _projetos = MutableLiveData<List<Project>>()
    val projetos: LiveData<List<Project>> = _projetos

    private val _orientacoes = MutableLiveData<List<StrategicOrientation>>()
    val orientacoes: LiveData<List<StrategicOrientation>> = _orientacoes

    private val _avaliacaoIa = MutableLiveData<AvaliacaoIa?>()
    val avaliacaoIa: LiveData<AvaliacaoIa?> = _avaliacaoIa

    private val _mensagem = MutableLiveData<String?>()
    val mensagem: LiveData<String?> = _mensagem

    private val _carregando = MutableLiveData<Boolean>()
    val carregando: LiveData<Boolean> = _carregando

    fun carregarIdeiasEnviadas() {
        ideaRepository.listarIdeiasEnviadas(
            onSuccess = { _ideiasEnviadas.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun carregarTodasIdeias() {
        ideaRepository.listarTodasIdeias(
            onSuccess = { _todasIdeias.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun aprovarIdeia(ideiaId: String, autorId: String, impacto: String, esforco: String) {
        _carregando.value = true
        ideaRepository.aprovarIdeia(
            ideiaId = ideiaId,
            impacto = impacto,
            esforco = esforco,
            onSuccess = {
                _ideiasEnviadas.value = _ideiasEnviadas.value?.filter { it.id != ideiaId }
                _carregando.value = false
                _mensagem.value = "Ideia aprovada! Operador recebeu +50 pontos"
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun rejeitarIdeia(ideiaId: String, motivo: String) {
        if (motivo.isBlank()) {
            _mensagem.value = "Informe o motivo da rejeição"
            return
        }
        _carregando.value = true
        ideaRepository.rejeitarIdeia(
            ideiaId = ideiaId,
            motivo = motivo,
            onSuccess = {
                _ideiasEnviadas.value = _ideiasEnviadas.value?.filter { it.id != ideiaId }
                _carregando.value = false
                _mensagem.value = "Ideia rejeitada"
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun priorizarIdeia(ideiaId: String, impacto: String, esforco: String, prioridade: Int? = null) {
        _carregando.value = true
        ideaRepository.priorizarIdeia(
            ideiaId = ideiaId,
            impacto = impacto,
            esforco = esforco,
            prioridade = prioridade,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Prioridade atualizada"
                carregarIdeiasEnviadas()
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun avaliarIdeiaComIa(ideiaId: String) {
        _carregando.value = true
        ideaRepository.avaliarComIa(
            ideiaId = ideiaId,
            onSuccess = { avaliacao ->
                _carregando.value = false
                _avaliacaoIa.value = avaliacao
                _ideiasEnviadas.value = _ideiasEnviadas.value?.map { ideia ->
                    if (ideia.id == ideiaId) {
                        ideia.copy(scoreIa = avaliacao.score, justificativaIa = avaliacao.justificativa)
                    } else {
                        ideia
                    }
                }
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun limparAvaliacaoIa() {
        _avaliacaoIa.value = null
    }

    fun criarProjeto(project: Project) {
        if (project.titulo.isBlank() || project.descricao.isBlank()) {
            _mensagem.value = "Preencha título e descrição do projeto"
            return
        }
        _carregando.value = true
        projectRepository.criarProjeto(
            project = project,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Projeto criado com sucesso"
                carregarProjetos()
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun carregarProjetos() {
        projectRepository.listarProjetos(
            onSuccess = { _projetos.value = it },
            onError = { _mensagem.value = it }
        )
    }

    fun atualizarEtapaProjeto(projectId: String, novaEtapa: String) {
        _carregando.value = true
        projectRepository.alterarEtapa(
            projectId = projectId,
            novaEtapa = novaEtapa,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Etapa atualizada para: $novaEtapa"
                carregarProjetos()
            },
            onError = { msg ->
                _carregando.value = false
                _mensagem.value = msg
            }
        )
    }

    fun adicionarResultados(
        projectId: String,
        retornoFinanceiro: Double,
        ganhoProdutividade: Double,
        resultadosQualitativos: String
    ) {
        _carregando.value = true
        projectRepository.adicionarResultados(
            projectId = projectId,
            retornoFinanceiro = retornoFinanceiro,
            ganhoProdutividade = ganhoProdutividade,
            resultadosQualitativos = resultadosQualitativos,
            onSuccess = {
                _carregando.value = false
                _mensagem.value = "Resultados adicionados com sucesso"
                carregarProjetos()
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

    fun limparMensagem() {
        _mensagem.value = null
    }
}
