package br.com.fiap.inovagab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.inovagab.model.User
import br.com.fiap.inovagab.repository.AuthRepository

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _usuario = MutableLiveData<User?>()
    val usuario: LiveData<User?> = _usuario

    private val _erro = MutableLiveData<String?>()
    val erro: LiveData<String?> = _erro

    private val _carregando = MutableLiveData<Boolean>()
    val carregando: LiveData<Boolean> = _carregando

    fun login(email: String, senha: String) {
        if (email.isBlank() || senha.isBlank()) {
            _erro.value = "Preencha todos os campos"
            return
        }
        _carregando.value = true
        authRepository.login(
            email = email,
            senha = senha,
            onSuccess = { user ->
                _carregando.value = false
                _usuario.value = user
            },
            onError = { msg ->
                _carregando.value = false
                _erro.value = msg
            }
        )
    }

    fun verificarSessao() {
        val uid = authRepository.getUsuarioAtualId()
        if (uid.isNullOrBlank() || !authRepository.isLogado()) {
            return
        }
        _carregando.value = true
        authRepository.buscarUsuario(
            uid = uid,
            onSuccess = { user ->
                _carregando.value = false
                _usuario.value = user
            },
            onError = {
                _carregando.value = false
                authRepository.logout()
                _usuario.value = null
            }
        )
    }

    fun logout() {
        authRepository.logout()
        _usuario.value = null
    }

    fun limparErro() {
        _erro.value = null
    }
}
