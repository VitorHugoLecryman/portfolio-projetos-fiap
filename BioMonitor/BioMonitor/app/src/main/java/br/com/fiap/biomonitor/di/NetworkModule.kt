package br.com.fiap.biomonitor.di

import br.com.fiap.biomonitor.data.remote.api.GbifApi
import br.com.fiap.biomonitor.data.remote.api.INaturalistApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val INATURALIST_BASE_URL = "https://api.inaturalist.org/v1/"
    private const val GBIF_BASE_URL = "https://api.gbif.org/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("iNaturalist")
    fun provideINaturalistRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(INATURALIST_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("gbif")
    fun provideGbifRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GBIF_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideINaturalistApi(@Named("iNaturalist") retrofit: Retrofit): INaturalistApi {
        return retrofit.create(INaturalistApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGbifApi(@Named("gbif") retrofit: Retrofit): GbifApi {
        return retrofit.create(GbifApi::class.java)
    }
}
