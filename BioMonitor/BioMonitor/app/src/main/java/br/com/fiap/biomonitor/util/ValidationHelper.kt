package br.com.fiap.biomonitor.util

object ValidationHelper {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    private val PASSWORD_ALPHANUMERIC_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$".toRegex()


    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> ErrorMessages.EMAIL_REQUIRED
            !email.matches(EMAIL_REGEX) -> ErrorMessages.INVALID_EMAIL
            else -> null
        }
    }


    fun validatePassword(password: String, requireAlphanumeric: Boolean = false): String? {
        return when {
            password.length < 6 -> ErrorMessages.INVALID_PASSWORD
            requireAlphanumeric && !password.matches(PASSWORD_ALPHANUMERIC_REGEX) ->
                ErrorMessages.INVALID_PASSWORD_STRENGTH
            else -> null
        }
    }


    fun validateName(name: String): String? {
        return if (name.length < 3) ErrorMessages.INVALID_NAME else null
    }


    fun validateCity(city: String): String? {
        return if (city.trim().isBlank()) ErrorMessages.INVALID_CITY else null
    }


    fun validatePasswordMatch(password: String, confirmation: String): String? {
        return if (password != confirmation) ErrorMessages.PASSWORDS_DONT_MATCH else null
    }


    fun validateTermsAccepted(accepted: Boolean): String? {
        return if (!accepted) ErrorMessages.TERMS_NOT_ACCEPTED else null
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errors: Map<String, String> = emptyMap()
)
