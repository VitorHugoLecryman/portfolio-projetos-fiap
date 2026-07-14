package br.com.fiap.biomonitor.data.repository

import br.com.fiap.biomonitor.data.local.database.dao.SpeciesCacheDao
import br.com.fiap.biomonitor.data.local.database.entity.SpeciesCacheEntity
import br.com.fiap.biomonitor.data.remote.api.GbifApi
import br.com.fiap.biomonitor.data.remote.api.INaturalistApi
import br.com.fiap.biomonitor.data.remote.dto.TaxaResult
import br.com.fiap.biomonitor.domain.model.Species
import br.com.fiap.biomonitor.domain.repository.SpeciesRepository
import javax.inject.Inject

class SpeciesRepositoryImpl @Inject constructor(
    private val iNaturalistApi: INaturalistApi,
    private val gbifApi: GbifApi,
    private val speciesCacheDao: SpeciesCacheDao
) : SpeciesRepository {

    override suspend fun searchSpecies(query: String): Result<List<Species>> {
        return try {

            val cachedSpecies = speciesCacheDao.searchByName(query)
            if (cachedSpecies.isNotEmpty()) {
                return Result.success(cachedSpecies.map { it.toDomain() })
            }


            val response = iNaturalistApi.searchSpecies(query, perPage = 10)
            if (response.isSuccessful) {
                val speciesList = response.body()?.results?.map { it.toDomain() } ?: emptyList()

                speciesList.forEach { cacheSpecies(it) }
                Result.success(speciesList)
            } else {
                Result.failure(Exception("Erro ao buscar espécies: ${response.code()}"))
            }
        } catch (e: Exception) {

            val cachedSpecies = speciesCacheDao.searchByName(query)
            if (cachedSpecies.isNotEmpty()) {
                Result.success(cachedSpecies.map { it.toDomain() })
            } else {
                Result.failure(Exception("Sem conexão com a internet"))
            }
        }
    }

    override suspend fun getSpeciesDetails(id: Long): Result<Species> {
        return try {

            val cachedSpecies = speciesCacheDao.findById(id)
            if (cachedSpecies != null) {
                return Result.success(cachedSpecies.toDomain())
            }


            val response = iNaturalistApi.getSpeciesDetails(id)
            if (response.isSuccessful) {
                val result = response.body()?.results?.firstOrNull()
                if (result != null) {
                    val species = Species(
                        id = result.id,
                        name = result.name,
                        scientificName = result.name,
                        commonName = result.preferredCommonName,
                        iconUrl = result.defaultPhoto?.squareUrl,
                        conservationStatus = result.conservationStatus?.statusName
                    )
                    cacheSpecies(species)
                    Result.success(species)
                } else {
                    Result.failure(Exception("Espécie não encontrada"))
                }
            } else {
                Result.failure(Exception("Erro ao buscar detalhes: ${response.code()}"))
            }
        } catch (e: Exception) {

            val cachedSpecies = speciesCacheDao.findById(id)
            if (cachedSpecies != null) {
                Result.success(cachedSpecies.toDomain())
            } else {
                Result.failure(Exception("Sem conexão com a internet"))
            }
        }
    }

    override suspend fun getConservationStatus(speciesKey: Long): Result<String?> {
        return try {
            val response = gbifApi.getSpeciesDetails(speciesKey)
            if (response.isSuccessful) {
                val status = response.body()?.threatStatus
                Result.success(status)
            } else {
                Result.failure(Exception("Erro ao buscar status de conservação: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sem conexão com a internet"))
        }
    }

    override suspend fun cacheSpecies(species: Species) {
        speciesCacheDao.insert(species.toEntity())
    }

    override suspend fun getCachedSpecies(query: String): List<Species> {
        return speciesCacheDao.searchByName(query).map { it.toDomain() }
    }


    private fun TaxaResult.toDomain(): Species {
        return Species(
            id = id,
            name = name,
            scientificName = name,
            commonName = preferredCommonName,
            iconUrl = defaultPhoto?.squareUrl,
            conservationStatus = null
        )
    }


    private fun SpeciesCacheEntity.toDomain(): Species {
        return Species(
            id = id,
            name = name,
            scientificName = scientificName,
            commonName = commonName,
            iconUrl = iconUrl,
            conservationStatus = conservationStatus
        )
    }


    private fun Species.toEntity(): SpeciesCacheEntity {
        return SpeciesCacheEntity(
            id = id,
            name = name,
            scientificName = scientificName,
            commonName = commonName,
            iconUrl = iconUrl,
            conservationStatus = conservationStatus,
            cachedAt = System.currentTimeMillis()
        )
    }
}
