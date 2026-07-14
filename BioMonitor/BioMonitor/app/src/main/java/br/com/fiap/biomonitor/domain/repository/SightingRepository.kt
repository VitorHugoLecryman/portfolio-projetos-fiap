package br.com.fiap.biomonitor.domain.repository

import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Sighting
import kotlinx.coroutines.flow.Flow

interface SightingRepository {
    fun getSightingsByUser(userId: Long): Flow<List<Sighting>>
    fun getAllSightings(): Flow<List<Sighting>>
    suspend fun findById(id: Long): Sighting?
    suspend fun create(sighting: Sighting): Result<Long>
    suspend fun update(sighting: Sighting): Result<Unit>
    suspend fun delete(sighting: Sighting): Result<Unit>
    suspend fun getPendingSightings(): List<Sighting>
    suspend fun syncSighting(sighting: Sighting): Result<Unit>
    suspend fun countByUser(userId: Long): Int
    suspend fun countUniqueSpeciesByUser(userId: Long): Int
    suspend fun countCitiesByUser(userId: Long): Int
    fun getSightingsByCategory(userId: Long, category: Category): Flow<List<Sighting>>
}
