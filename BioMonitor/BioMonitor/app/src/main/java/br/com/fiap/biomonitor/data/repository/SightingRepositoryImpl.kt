package br.com.fiap.biomonitor.data.repository

import br.com.fiap.biomonitor.data.local.database.dao.SightingDao
import br.com.fiap.biomonitor.data.local.database.entity.SightingEntity
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Habitat
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.model.SyncStatus
import br.com.fiap.biomonitor.domain.repository.SightingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class SightingRepositoryImpl @Inject constructor(
    private val sightingDao: SightingDao
) : SightingRepository {

    override fun getSightingsByUser(userId: Long): Flow<List<Sighting>> {
        return sightingDao.getSightingsByUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllSightings(): Flow<List<Sighting>> {
        return sightingDao.getAllSightings().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun findById(id: Long): Sighting? {
        return sightingDao.findById(id)?.toDomain()
    }

    override suspend fun create(sighting: Sighting): Result<Long> {
        return try {
            val entity = sighting.toEntity().copy(syncStatus = SyncStatus.PENDING.name)
            val id = sightingDao.insert(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(sighting: Sighting): Result<Unit> {
        return try {
            sightingDao.update(sighting.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(sighting: Sighting): Result<Unit> {
        return try {
            sightingDao.delete(sighting.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPendingSightings(): List<Sighting> {
        return sightingDao.getSightingsBySyncStatus(SyncStatus.PENDING.name)
            .map { it.toDomain() }
    }

    override suspend fun syncSighting(sighting: Sighting): Result<Unit> {
        return try {
            val syncedSighting = sighting.copy(syncStatus = SyncStatus.SYNCED)
            sightingDao.update(syncedSighting.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            val failedSighting = sighting.copy(syncStatus = SyncStatus.FAILED)
            sightingDao.update(failedSighting.toEntity())
            Result.failure(e)
        }
    }

    override suspend fun countByUser(userId: Long): Int {
        return sightingDao.countByUser(userId)
    }

    override suspend fun countUniqueSpeciesByUser(userId: Long): Int {
        return sightingDao.countUniqueSpeciesByUser(userId)
    }

    override suspend fun countCitiesByUser(userId: Long): Int {
        return sightingDao.countCitiesByUser(userId)
    }

    override fun getSightingsByCategory(userId: Long, category: Category): Flow<List<Sighting>> {
        return sightingDao.getSightingsByCategory(userId, category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }


    private fun SightingEntity.toDomain(): Sighting {
        return Sighting(
            id = id,
            userId = userId,
            photoPath = photoPath,
            category = Category.valueOf(category),
            speciesName = speciesName,
            scientificName = scientificName,
            latitude = latitude,
            longitude = longitude,
            dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dateTime),
                ZoneId.systemDefault()
            ),
            habitat = habitat?.let { Habitat.valueOf(it) },
            observations = observations,
            syncStatus = SyncStatus.valueOf(syncStatus),
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(createdAt),
                ZoneId.systemDefault()
            )
        )
    }


    private fun Sighting.toEntity(): SightingEntity {
        return SightingEntity(
            id = id,
            userId = userId,
            photoPath = photoPath,
            category = category.name,
            speciesName = speciesName,
            scientificName = scientificName,
            latitude = latitude,
            longitude = longitude,
            dateTime = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            habitat = habitat?.name,
            observations = observations,
            syncStatus = syncStatus.name,
            createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
}
