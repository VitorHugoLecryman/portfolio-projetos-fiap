package br.com.fiap.biomonitor.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import br.com.fiap.biomonitor.domain.model.User
import br.com.fiap.biomonitor.domain.usecase.auth.RegisterUseCase
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val sessionPreferences: SessionPreferences
) : ViewModel() {


    data class RegisterState(
        val name: String = "",
        val email: String = "",
        val city: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val termsAccepted: Boolean = false,
        val nameError: String? = null,
        val emailError: String? = null,
        val cityError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null,
        val termsError: String? = null,
        val isLoading: Boolean = false,
        val registerError: String? = null,
        val isPasswordVisible: Boolean = false,
        val isConfirmPasswordVisible: Boolean = false,
        val registerSuccess: Boolean = false,
        val passwordStrength: PasswordStrength = PasswordStrength.NONE
    )


    enum class PasswordStrength {
        NONE,
        WEAK,
        MEDIUM,
        STRONG
    }

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name, nameError = null, registerError = null) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null, registerError = null) }
    }

    fun onCityChange(city: String) {
        _state.update { it.copy(city = city, cityError = null, registerError = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordError = null,
                registerError = null,
                passwordStrength = calculatePasswordStrength(password)
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update {
            it.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = null,
                registerError = null
            )
        }
    }

    fun onTermsAcceptedChange(accepted: Boolean) {
        _state.update { it.copy(termsAccepted = accepted, termsError = null, registerError = null) }
    }

    fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun clearRegisterError() {
        _state.update { it.copy(registerError = null) }
    }


    private fun calculatePasswordStrength(password: String): PasswordStrength {
        if (password.isEmpty()) return PasswordStrength.NONE

        var score = 0


        if (password.length >= 6) score++
        if (password.length >= 8) score++
        if (password.length >= 12) score++


        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++

        return when {
            score <= 2 -> PasswordStrength.WEAK
            score <= 4 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.STRONG
        }
    }


    private fun validateInputs(): Boolean {
        val currentState = _state.value

        val nameError = ValidationHelper.validateName(currentState.name)
        val emailError = ValidationHelper.validateEmail(currentState.email)
        val cityError = ValidationHelper.validateCity(currentState.city)
        val passwordError = ValidationHelper.validatePassword(currentState.password, requireAlphanumeric = true)
        val confirmPasswordError = ValidationHelper.validatePasswordMatch(
            currentState.password,
            currentState.confirmPassword
        )
        val termsError = ValidationHelper.validateTermsAccepted(currentState.termsAccepted)

        _state.update {
            it.copy(
                nameError = nameError,
                emailError = emailError,
                cityError = cityError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                termsError = termsError
            )
        }

        return nameError == null &&
               emailError == null &&
               cityError == null &&
               passwordError == null &&
               confirmPasswordError == null &&
               termsError == null
    }


    fun register() {
        if (!validateInputs()) return

        val currentState = _state.value

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, registerError = null) }

            val user = User(
                name = currentState.name.trim(),
                email = currentState.email.trim().lowercase(),
                password = currentState.password,
                city = currentState.city.trim()
            )

            registerUseCase(user)
                .onSuccess { userId ->

                    sessionPreferences.saveSession(userId, user.email)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            registerSuccess = true
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            registerError = exception.message ?: ErrorMessages.UNKNOWN_ERROR
                        )
                    }
                }
        }
    }


    fun resetRegisterSuccess() {
        _state.update { it.copy(registerSuccess = false) }
    }
}
