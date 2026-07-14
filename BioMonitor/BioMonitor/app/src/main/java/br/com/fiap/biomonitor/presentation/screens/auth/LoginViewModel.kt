package br.com.fiap.biomonitor.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.domain.usecase.auth.LoginUseCase
import br.com.fiap.biomonitor.util.ErrorMessages
import br.com.fiap.biomonitor.util.ValidationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {


    data class LoginState(
        val email: String = "",
        val password: String = "",
        val emailError: String? = null,
        val passwordError: String? = null,
        val isLoading: Boolean = false,
        val loginError: String? = null,
        val isPasswordVisible: Boolean = false,
        val loginSuccess: Boolean = false
    )

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()


    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null, loginError = null) }
    }


    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null, loginError = null) }
    }


    fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }


    fun clearLoginError() {
        _state.update { it.copy(loginError = null) }
    }


    fun login() {
        val currentState = _state.value


        val emailError = ValidationHelper.validateEmail(currentState.email)
        val passwordError = ValidationHelper.validatePassword(currentState.password)

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }


        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, loginError = null) }

            loginUseCase(currentState.email, currentState.password)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loginError = exception.message ?: ErrorMessages.INVALID_CREDENTIALS
                        )
                    }
                }
        }
    }


    fun resetLoginSuccess() {
        _state.update { it.copy(loginSuccess = false) }
    }
}
