package br.com.fiap.biomonitor.di

import android.content.Context
import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionPreferences(@ApplicationContext context: Context): SessionPreferences {
        return SessionPreferences(context)
    }
}
