package br.com.fiap.biomonitor.domain.usecase.gamification

import br.com.fiap.biomonitor.domain.model.PointType
import br.com.fiap.biomonitor.domain.repository.UserRepository
import javax.inject.Inject

class AwardPointsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Long, pointType: PointType): Result<Unit> {
        val user = userRepository.findById(userId) ?: return Result.failure(Exception("Usuário não encontrado"))
        val newPoints = user.points + pointType.points
        return userRepository.updatePoints(userId, newPoints)
    }
}
