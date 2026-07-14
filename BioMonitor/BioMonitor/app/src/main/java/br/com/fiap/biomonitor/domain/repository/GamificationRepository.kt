package br.com.fiap.biomonitor.domain.repository

import br.com.fiap.biomonitor.domain.model.Badge

interface GamificationRepository {
    suspend fun getAllBadges(): List<Badge>
    suspend fun getUserBadges(userId: Long): List<Badge>
    suspend fun awardBadge(userId: Long, badgeId: Long): Result<Unit>
    suspend fun hasUserBadge(userId: Long, badgeId: Long): Boolean
    suspend fun checkAndAwardBadges(userId: Long): List<Badge>
}
