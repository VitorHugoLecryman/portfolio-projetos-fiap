package br.com.fiap.biomonitor.domain.usecase.auth

import br.com.fiap.biomonitor.domain.model.User
import br.com.fiap.biomonitor.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Long> {
        val existingUser = userRepository.findByEmail(user.email)
        if (existingUser != null) {
            return Result.failure(Exception("Este email já está cadastrado"))
        }
        return userRepository.register(user)
    }
}
