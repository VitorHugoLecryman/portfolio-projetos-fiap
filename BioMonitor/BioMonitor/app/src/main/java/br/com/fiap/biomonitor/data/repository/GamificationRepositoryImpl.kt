package br.com.fiap.biomonitor.data.repository

import br.com.fiap.biomonitor.data.local.database.dao.BadgeDao
import br.com.fiap.biomonitor.data.local.database.dao.SightingDao
import br.com.fiap.biomonitor.data.local.database.entity.BadgeEntity
import br.com.fiap.biomonitor.data.local.database.entity.UserBadgeEntity
import br.com.fiap.biomonitor.domain.model.Badge
import br.com.fiap.biomonitor.domain.model.BadgeRequirement
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.repository.GamificationRepository
import javax.inject.Inject

class GamificationRepositoryImpl @Inject constructor(
    private val badgeDao: BadgeDao,
    private val sightingDao: SightingDao
) : GamificationRepository {

    override suspend fun getAllBadges(): List<Badge> {
        return badgeDao.getAllBadges().map { it.toDomain() }
    }

    override suspend fun getUserBadges(userId: Long): List<Badge> {
        return badgeDao.getUserBadges(userId).map { it.toDomain() }
    }

    override suspend fun awardBadge(userId: Long, badgeId: Long): Result<Unit> {
        return try {
            val userBadge = UserBadgeEntity(
                userId = userId,
                badgeId = badgeId,
                earnedAt = System.currentTimeMillis()
            )
            badgeDao.insertUserBadge(userBadge)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun hasUserBadge(userId: Long, badgeId: Long): Boolean {
        return badgeDao.hasUserBadge(userId, badgeId)
    }

    override suspend fun checkAndAwardBadges(userId: Long): List<Badge> {
        val allBadges = badgeDao.getAllBadges()
        val newlyEarnedBadges = mutableListOf<Badge>()

        for (badgeEntity in allBadges) {

            if (badgeDao.hasUserBadge(userId, badgeEntity.id)) {
                continue
            }

            val badge = badgeEntity.toDomain()
            val shouldAward = when (val requirement = badge.requirement) {
                is BadgeRequirement.SightingCount -> {
                    val count = sightingDao.countByUser(userId)
                    count >= requirement.count
                }
                is BadgeRequirement.UniqueSpeciesCount -> {
                    val count = if (requirement.category != null) {

                        sightingDao.countUniqueSpeciesByUser(userId)
                    } else {
                        sightingDao.countUniqueSpeciesByUser(userId)
                    }
                    count >= requirement.count
                }
                is BadgeRequirement.CitiesCount -> {
                    val count = sightingDao.countCitiesByUser(userId)
                    count >= requirement.count
                }
            }

            if (shouldAward) {
                awardBadge(userId, badgeEntity.id)
                newlyEarnedBadges.add(badge)
            }
        }

        return newlyEarnedBadges
    }


    private fun BadgeEntity.toDomain(): Badge {
        val requirement = when (requirementType) {
            REQUIREMENT_TYPE_SIGHTING_COUNT -> {
                BadgeRequirement.SightingCount(requirementValue)
            }
            REQUIREMENT_TYPE_UNIQUE_SPECIES -> {
                val category = requirementCategory?.let {
                    try { Category.valueOf(it) } catch (e: Exception) { null }
                }
                BadgeRequirement.UniqueSpeciesCount(requirementValue, category)
            }
            REQUIREMENT_TYPE_CITIES_COUNT -> {
                BadgeRequirement.CitiesCount(requirementValue)
            }
            else -> BadgeRequirement.SightingCount(requirementValue)
        }

        return Badge(
            id = id,
            name = name,
            description = description,
            iconResName = iconResName,
            requirement = requirement
        )
    }

    companion object {
        const val REQUIREMENT_TYPE_SIGHTING_COUNT = "SIGHTING_COUNT"
        const val REQUIREMENT_TYPE_UNIQUE_SPECIES = "UNIQUE_SPECIES"
        const val REQUIREMENT_TYPE_CITIES_COUNT = "CITIES_COUNT"
    }
}
