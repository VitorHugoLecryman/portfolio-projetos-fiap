package br.com.fiap.biomonitor.di

import android.content.Context
import androidx.room.Room
import br.com.fiap.biomonitor.data.local.database.BadgePrepopulateCallback
import br.com.fiap.biomonitor.data.local.database.BioMonitorDatabase
import br.com.fiap.biomonitor.data.local.database.dao.BadgeDao
import br.com.fiap.biomonitor.data.local.database.dao.SightingDao
import br.com.fiap.biomonitor.data.local.database.dao.SpeciesCacheDao
import br.com.fiap.biomonitor.data.local.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BioMonitorDatabase {
        return Room.databaseBuilder(
            context,
            BioMonitorDatabase::class.java,
            "biomonitor.db"
        )
            .addCallback(BadgePrepopulateCallback(CoroutineScope(SupervisorJob())))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(database: BioMonitorDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideSightingDao(database: BioMonitorDatabase): SightingDao {
        return database.sightingDao()
    }

    @Provides
    fun provideBadgeDao(database: BioMonitorDatabase): BadgeDao {
        return database.badgeDao()
    }

    @Provides
    fun provideSpeciesCacheDao(database: BioMonitorDatabase): SpeciesCacheDao {
        return database.speciesCacheDao()
    }
}
