package br.com.fiap.biomonitor.domain.usecase.sighting

import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.repository.SightingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSightingsUseCase @Inject constructor(
    private val sightingRepository: SightingRepository
) {
    operator fun invoke(): Flow<List<Sighting>> {
        return sightingRepository.getAllSightings()
    }
}
