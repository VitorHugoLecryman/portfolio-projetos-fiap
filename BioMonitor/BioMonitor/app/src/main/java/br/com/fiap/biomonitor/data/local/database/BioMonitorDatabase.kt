package br.com.fiap.biomonitor.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.fiap.biomonitor.data.local.database.dao.BadgeDao
import br.com.fiap.biomonitor.data.local.database.dao.SightingDao
import br.com.fiap.biomonitor.data.local.database.dao.SpeciesCacheDao
import br.com.fiap.biomonitor.data.local.database.dao.UserDao
import br.com.fiap.biomonitor.data.local.database.entity.BadgeEntity
import br.com.fiap.biomonitor.data.local.database.entity.SightingEntity
import br.com.fiap.biomonitor.data.local.database.entity.SpeciesCacheEntity
import br.com.fiap.biomonitor.data.local.database.entity.UserBadgeEntity
import br.com.fiap.biomonitor.data.local.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SightingEntity::class,
        BadgeEntity::class,
        UserBadgeEntity::class,
        SpeciesCacheEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class BioMonitorDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sightingDao(): SightingDao
    abstract fun badgeDao(): BadgeDao
    abstract fun speciesCacheDao(): SpeciesCacheDao
}
