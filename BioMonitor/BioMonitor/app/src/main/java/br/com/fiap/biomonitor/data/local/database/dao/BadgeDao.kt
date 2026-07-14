package br.com.fiap.biomonitor.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.fiap.biomonitor.data.local.database.entity.BadgeEntity
import br.com.fiap.biomonitor.data.local.database.entity.UserBadgeEntity

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges")
    suspend fun getAllBadges(): List<BadgeEntity>

    @Query("SELECT b.* FROM badges b INNER JOIN user_badges ub ON b.id = ub.badgeId WHERE ub.userId = :userId")
    suspend fun getUserBadges(userId: Long): List<BadgeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserBadge(userBadge: UserBadgeEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM user_badges WHERE userId = :userId AND badgeId = :badgeId)")
    suspend fun hasUserBadge(userId: Long, badgeId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBadges(badges: List<BadgeEntity>)
}
