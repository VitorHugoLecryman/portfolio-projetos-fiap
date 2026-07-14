package br.com.fiap.biomonitor.domain.usecase.sighting

import br.com.fiap.biomonitor.domain.repository.SightingRepository
import javax.inject.Inject

class SyncSightingsUseCase @Inject constructor(
    private val sightingRepository: SightingRepository
) {
    suspend operator fun invoke(): Result<Int> {
        val pendingSightings = sightingRepository.getPendingSightings()
        var syncedCount = 0
        pendingSightings.forEach { sighting ->
            sightingRepository.syncSighting(sighting).onSuccess {
                syncedCount++
            }
        }
        return Result.success(syncedCount)
    }
}
