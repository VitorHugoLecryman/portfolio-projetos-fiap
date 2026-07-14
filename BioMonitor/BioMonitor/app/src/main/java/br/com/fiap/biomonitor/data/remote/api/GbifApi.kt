package br.com.fiap.biomonitor.data.remote.api

import br.com.fiap.biomonitor.data.remote.dto.SpeciesResponse
import br.com.fiap.biomonitor.data.remote.dto.SpeciesSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GbifApi {
    @GET("species/{key}")
    suspend fun getSpeciesDetails(
        @Path("key") key: Long
    ): Response<SpeciesResponse>

    @GET("species/search")
    suspend fun searchSpecies(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10
    ): Response<SpeciesSearchResponse>
}
