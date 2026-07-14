package br.com.fiap.biomonitor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.biomonitor.data.local.database.entity.SightingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SightingDao {
    @Query("SELECT * FROM sightings WHERE userId = :userId ORDER BY dateTime DESC")
    fun getSightingsByUser(userId: Long): Flow<List<SightingEntity>>

    @Query("SELECT * FROM sightings ORDER BY dateTime DESC")
    fun getAllSightings(): Flow<List<SightingEntity>>

    @Query("SELECT * FROM sightings WHERE id = :id")
    suspend fun findById(id: Long): SightingEntity?

    @Query("SELECT * FROM sightings WHERE syncStatus = :status")
    suspend fun getSightingsBySyncStatus(status: String): List<SightingEntity>

    @Query("SELECT COUNT(*) FROM sightings WHERE userId = :userId")
    suspend fun countByUser(userId: Long): Int

    @Query("SELECT COUNT(DISTINCT speciesName) FROM sightings WHERE userId = :userId")
    suspend fun countUniqueSpeciesByUser(userId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sighting: SightingEntity): Long

    @Update
    suspend fun update(sighting: SightingEntity)

    @Delete
    suspend fun delete(sighting: SightingEntity)

    @Query("SELECT * FROM sightings WHERE userId = :userId AND category = :category ORDER BY dateTime DESC")
    fun getSightingsByCategory(userId: Long, category: String): Flow<List<SightingEntity>>

    @Query("SELECT COUNT(DISTINCT city) FROM sightings s INNER JOIN users u ON s.userId = u.id WHERE s.userId = :userId")
    suspend fun countCitiesByUser(userId: Long): Int
}
