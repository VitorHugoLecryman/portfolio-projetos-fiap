package br.com.fiap.inovagab.repository

import br.com.fiap.inovagab.model.User
import br.com.fiap.inovagab.network.ApiClient
import br.com.fiap.inovagab.network.LoginRequestDto
import br.com.fiap.inovagab.network.paraModel

class AuthRepository {

    private val api = ApiClient.api
    private val sessao = ApiClient.sessionManager

    fun login(
        email: String,
        senha: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.login(LoginRequestDto(email, senha)),
            mensagemErro = "E-mail ou senha inválidos",
            onSuccess = { resposta ->
                if (resposta == null) {
                    onError("Resposta inválida do servidor")
                    return@executar
                }
                val usuario = resposta.usuario.paraModel()
                sessao.salvarSessao(
                    token = resposta.token,
                    id = usuario.uid,
                    nome = usuario.nome,
                    email = usuario.email,
                    perfil = usuario.perfil,
                    pontos = usuario.pontos
                )
                onSuccess(usuario)
            },
            onError = onError
        )
    }

    fun buscarUsuario(
        uid: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        ApiClient.executar(
            chamada = api.me(),
            mensagemErro = "Erro ao buscar dados do usuário",
            onSuccess = { dto ->
                if (dto == null) {
                    onError("Usuário não encontrado no sistema")
                    return@executar
                }
                val usuario = dto.paraModel()
                sessao.atualizarPontos(usuario.pontos)
                onSuccess(usuario)
            },
            onError = onError
        )
    }

    fun getUsuarioAtualId(): String? = sessao.getUsuarioId()

    fun logout() {
        sessao.limpar()
    }

    fun isLogado(): Boolean = sessao.estaLogado()
}
