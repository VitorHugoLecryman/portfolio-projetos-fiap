package br.com.fiap.biomonitor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.fiap.biomonitor.data.local.database.entity.SpeciesCacheEntity

@Dao
interface SpeciesCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(species: SpeciesCacheEntity)

    @Query("SELECT * FROM species_cache WHERE id = :id")
    suspend fun findById(id: Long): SpeciesCacheEntity?

    @Query("SELECT * FROM species_cache WHERE name LIKE '%' || :query || '%' OR commonName LIKE '%' || :query || '%' OR scientificName LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<SpeciesCacheEntity>

    @Query("DELETE FROM species_cache WHERE cachedAt < :timestamp")
    suspend fun deleteOldCache(timestamp: Long)
}
