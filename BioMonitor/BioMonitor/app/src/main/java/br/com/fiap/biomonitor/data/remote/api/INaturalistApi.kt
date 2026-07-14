package br.com.fiap.biomonitor.data.remote.api

import br.com.fiap.biomonitor.data.remote.dto.TaxaAutocompleteResponse
import br.com.fiap.biomonitor.data.remote.dto.TaxaDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INaturalistApi {
    @GET("taxa/autocomplete")
    suspend fun searchSpecies(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 10
    ): Response<TaxaAutocompleteResponse>

    @GET("taxa/{id}")
    suspend fun getSpeciesDetails(
        @Path("id") id: Long
    ): Response<TaxaDetailResponse>
}
