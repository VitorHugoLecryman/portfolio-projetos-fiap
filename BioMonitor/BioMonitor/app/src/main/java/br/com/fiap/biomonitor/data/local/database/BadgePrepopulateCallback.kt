package br.com.fiap.biomonitor.data.local.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.fiap.biomonitor.data.local.database.entity.BadgeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BadgePrepopulateCallback(
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch(Dispatchers.IO) {
            prepopulateBadges(db)
        }
    }

    private fun prepopulateBadges(db: SupportSQLiteDatabase) {
        val badges = getInitialBadges()
        badges.forEach { badge ->
            db.execSQL(
                """
                INSERT INTO badges (id, name, description, iconResName, requirementType, requirementValue, requirementCategory)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                arrayOf(
                    badge.id,
                    badge.name,
                    badge.description,
                    badge.iconResName,
                    badge.requirementType,
                    badge.requirementValue,
                    badge.requirementCategory
                )
            )
        }
    }

    companion object {

        fun getInitialBadges(): List<BadgeEntity> = listOf(

            BadgeEntity(
                id = 1,
                name = "Primeiro Passo",
                description = "Registre seu primeiro avistamento",
                iconResName = "ic_badge_first",
                requirementType = "SIGHTING_COUNT",
                requirementValue = 1,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 2,
                name = "Observador Iniciante",
                description = "Registre 10 avistamentos",
                iconResName = "ic_badge_beginner",
                requirementType = "SIGHTING_COUNT",
                requirementValue = 10,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 3,
                name = "Observador Dedicado",
                description = "Registre 50 avistamentos",
                iconResName = "ic_badge_dedicated",
                requirementType = "SIGHTING_COUNT",
                requirementValue = 50,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 4,
                name = "Mestre Observador",
                description = "Registre 100 avistamentos",
                iconResName = "ic_badge_master",
                requirementType = "SIGHTING_COUNT",
                requirementValue = 100,
                requirementCategory = null
            ),


            BadgeEntity(
                id = 5,
                name = "Descobridor",
                description = "Registre 5 espécies diferentes",
                iconResName = "ic_badge_discoverer",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 5,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 6,
                name = "Explorador",
                description = "Registre 20 espécies diferentes",
                iconResName = "ic_badge_explorer",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 20,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 7,
                name = "Naturalista",
                description = "Registre 50 espécies diferentes",
                iconResName = "ic_badge_naturalist",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 50,
                requirementCategory = null
            ),


            BadgeEntity(
                id = 8,
                name = "Amigo das Plantas",
                description = "Registre 10 espécies de flora",
                iconResName = "ic_badge_flora",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 10,
                requirementCategory = "FLORA"
            ),
            BadgeEntity(
                id = 9,
                name = "Observador de Aves",
                description = "Registre 10 espécies de aves",
                iconResName = "ic_badge_birds",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 10,
                requirementCategory = "AVES"
            ),
            BadgeEntity(
                id = 10,
                name = "Rastreador de Mamíferos",
                description = "Registre 10 espécies de mamíferos",
                iconResName = "ic_badge_mammals",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 10,
                requirementCategory = "MAMIFEROS"
            ),
            BadgeEntity(
                id = 11,
                name = "Herpetólogo",
                description = "Registre 10 espécies de répteis/anfíbios",
                iconResName = "ic_badge_reptiles",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 10,
                requirementCategory = "REPTEIS_ANFIBIOS"
            ),
            BadgeEntity(
                id = 12,
                name = "Entomólogo",
                description = "Registre 10 espécies de insetos",
                iconResName = "ic_badge_insects",
                requirementType = "UNIQUE_SPECIES",
                requirementValue = 10,
                requirementCategory = "INSETOS"
            ),


            BadgeEntity(
                id = 13,
                name = "Colecionador de Pontos",
                description = "Acumule 100 pontos",
                iconResName = "ic_badge_points_100",
                requirementType = "POINTS",
                requirementValue = 100,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 14,
                name = "Pontuador Bronze",
                description = "Acumule 500 pontos",
                iconResName = "ic_badge_points_500",
                requirementType = "POINTS",
                requirementValue = 500,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 15,
                name = "Pontuador Prata",
                description = "Acumule 1000 pontos",
                iconResName = "ic_badge_points_1000",
                requirementType = "POINTS",
                requirementValue = 1000,
                requirementCategory = null
            ),
            BadgeEntity(
                id = 16,
                name = "Pontuador Ouro",
                description = "Acumule 5000 pontos",
                iconResName = "ic_badge_points_5000",
                requirementType = "POINTS",
                requirementValue = 5000,
                requirementCategory = null
            )
        )
    }
}
