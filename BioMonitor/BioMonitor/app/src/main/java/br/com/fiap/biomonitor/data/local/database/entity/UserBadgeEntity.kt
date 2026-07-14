package br.com.fiap.biomonitor.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_badges",
    primaryKeys = ["userId", "badgeId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BadgeEntity::class,
            parentColumns = ["id"],
            childColumns = ["badgeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserBadgeEntity(
    val userId: Long,
    val badgeId: Long,
    val earnedAt: Long = System.currentTimeMillis()
)
