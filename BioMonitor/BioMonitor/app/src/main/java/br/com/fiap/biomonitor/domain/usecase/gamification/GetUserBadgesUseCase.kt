package br.com.fiap.biomonitor.domain.usecase.gamification

import br.com.fiap.biomonitor.domain.model.Badge
import br.com.fiap.biomonitor.domain.repository.GamificationRepository
import javax.inject.Inject

class GetUserBadgesUseCase @Inject constructor(
    private val gamificationRepository: GamificationRepository
) {
    suspend operator fun invoke(userId: Long): List<Badge> {
        return gamificationRepository.getUserBadges(userId)
    }
}
