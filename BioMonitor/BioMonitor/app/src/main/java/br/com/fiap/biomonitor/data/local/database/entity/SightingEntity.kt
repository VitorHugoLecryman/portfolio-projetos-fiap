package br.com.fiap.biomonitor.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sightings",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["userId"]), Index(value = ["syncStatus"])]
)
data class SightingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val photoPath: String,
    val category: String,
    val speciesName: String,
    val scientificName: String? = null,
    val latitude: Double,
    val longitude: Double,
    val dateTime: Long,
    val habitat: String? = null,
    val observations: String = "",
    val syncStatus: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
)
