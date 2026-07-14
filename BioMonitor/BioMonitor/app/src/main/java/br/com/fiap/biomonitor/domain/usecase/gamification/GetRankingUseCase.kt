package br.com.fiap.biomonitor.domain.usecase.gamification

import br.com.fiap.biomonitor.domain.model.RankingEntry
import br.com.fiap.biomonitor.domain.repository.UserRepository
import javax.inject.Inject

class GetRankingUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(limit: Int = 10, city: String? = null): List<RankingEntry> {
        val users = if (city != null) {
            userRepository.getTopUsersByCity(city, limit)
        } else {
            userRepository.getTopUsers(limit)
        }
        return users.mapIndexed { index, user ->
            RankingEntry(
                position = index + 1,
                user = user,
                points = user.points
            )
        }
    }
}
