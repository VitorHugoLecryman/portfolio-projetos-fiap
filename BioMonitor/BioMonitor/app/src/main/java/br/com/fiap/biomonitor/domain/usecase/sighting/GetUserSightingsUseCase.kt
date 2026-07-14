package br.com.fiap.biomonitor.domain.usecase.sighting

import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.repository.SightingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSightingsUseCase @Inject constructor(
    private val sightingRepository: SightingRepository
) {
    operator fun invoke(userId: Long): Flow<List<Sighting>> {
        return sightingRepository.getSightingsByUser(userId)
    }
}
