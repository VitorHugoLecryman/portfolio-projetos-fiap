package br.com.fiap.biomonitor.domain.usecase.auth

import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import br.com.fiap.biomonitor.domain.model.User
import br.com.fiap.biomonitor.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionPreferences: SessionPreferences
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return userRepository.login(email, password).onSuccess { user ->
            sessionPreferences.saveSession(user.id, user.email)
        }
    }
}
