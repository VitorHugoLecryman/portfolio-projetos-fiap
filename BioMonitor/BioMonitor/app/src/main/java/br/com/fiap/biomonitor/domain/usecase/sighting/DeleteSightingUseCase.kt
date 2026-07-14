package br.com.fiap.biomonitor.domain.usecase.sighting

import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.repository.SightingRepository
import javax.inject.Inject

class DeleteSightingUseCase @Inject constructor(
    private val sightingRepository: SightingRepository
) {
    suspend operator fun invoke(sighting: Sighting): Result<Unit> {
        return sightingRepository.delete(sighting)
    }
}
