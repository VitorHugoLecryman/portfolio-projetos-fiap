package br.com.fiap.biomonitor.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val password: String,
    val city: String,
    val profilePhotoPath: String? = null,
    val points: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
