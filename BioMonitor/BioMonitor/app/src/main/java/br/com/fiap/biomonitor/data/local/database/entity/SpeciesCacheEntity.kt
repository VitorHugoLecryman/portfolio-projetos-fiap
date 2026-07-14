package br.com.fiap.biomonitor.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "species_cache", indices = [Index(value = ["name"])])
data class SpeciesCacheEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val scientificName: String?,
    val commonName: String?,
    val iconUrl: String?,
    val conservationStatus: String?,
    val cachedAt: Long = System.currentTimeMillis()
)
