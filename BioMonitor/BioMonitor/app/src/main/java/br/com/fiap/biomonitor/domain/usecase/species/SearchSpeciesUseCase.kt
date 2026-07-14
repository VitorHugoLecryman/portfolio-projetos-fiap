package br.com.fiap.biomonitor.domain.usecase.species

import br.com.fiap.biomonitor.domain.model.Species
import br.com.fiap.biomonitor.domain.repository.SpeciesRepository
import javax.inject.Inject

class SearchSpeciesUseCase @Inject constructor(
    private val speciesRepository: SpeciesRepository
) {
    suspend operator fun invoke(query: String): Result<List<Species>> {
        if (query.length < 3) {
            return Result.success(emptyList())
        }

        val cached = speciesRepository.getCachedSpecies(query)
        if (cached.isNotEmpty()) {
            return Result.success(cached)
        }

        return speciesRepository.searchSpecies(query)
    }
}
