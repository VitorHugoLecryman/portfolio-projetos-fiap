package br.com.fiap.biomonitor.domain.repository

import br.com.fiap.biomonitor.domain.model.Species

interface SpeciesRepository {
    suspend fun searchSpecies(query: String): Result<List<Species>>
    suspend fun getSpeciesDetails(id: Long): Result<Species>
    suspend fun getConservationStatus(speciesKey: Long): Result<String?>
    suspend fun cacheSpecies(species: Species)
    suspend fun getCachedSpecies(query: String): List<Species>
}
