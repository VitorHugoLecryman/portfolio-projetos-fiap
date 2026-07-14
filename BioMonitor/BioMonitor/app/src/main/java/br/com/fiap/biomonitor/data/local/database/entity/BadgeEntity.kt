package br.com.fiap.biomonitor.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val iconResName: String,
    val requirementType: String,
    val requirementValue: Int,
    val requirementCategory: String? = null
)
