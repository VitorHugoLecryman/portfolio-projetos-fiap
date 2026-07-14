package br.com.fiap.biomonitor.domain.usecase.species

import br.com.fiap.biomonitor.domain.model.Species
import br.com.fiap.biomonitor.domain.repository.SpeciesRepository
import javax.inject.Inject

class GetSpeciesDetailsUseCase @Inject constructor(
    private val speciesRepository: SpeciesRepository
) {
    suspend operator fun invoke(id: Long): Result<Species> {
        return speciesRepository.getSpeciesDetails(id)
    }
}
