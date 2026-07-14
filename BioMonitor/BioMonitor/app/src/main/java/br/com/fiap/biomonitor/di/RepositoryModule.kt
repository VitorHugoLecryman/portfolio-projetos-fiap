package br.com.fiap.biomonitor.di

import br.com.fiap.biomonitor.data.repository.GamificationRepositoryImpl
import br.com.fiap.biomonitor.data.repository.SightingRepositoryImpl
import br.com.fiap.biomonitor.data.repository.SpeciesRepositoryImpl
import br.com.fiap.biomonitor.data.repository.UserRepositoryImpl
import br.com.fiap.biomonitor.domain.repository.GamificationRepository
import br.com.fiap.biomonitor.domain.repository.SightingRepository
import br.com.fiap.biomonitor.domain.repository.SpeciesRepository
import br.com.fiap.biomonitor.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindSightingRepository(impl: SightingRepositoryImpl): SightingRepository

    @Binds
    @Singleton
    abstract fun bindSpeciesRepository(impl: SpeciesRepositoryImpl): SpeciesRepository

    @Binds
    @Singleton
    abstract fun bindGamificationRepository(impl: GamificationRepositoryImpl): GamificationRepository
}
