package br.com.fiap.inovagab.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface InovaGabApi {

    @POST("api/auth/login")
    fun login(@Body corpo: LoginRequestDto): Call<LoginResponseDto>

    @GET("api/auth/me")
    fun me(): Call<UsuarioDto>

    @GET("api/orientacoes")
    fun listarOrientacoes(): Call<List<OrientacaoDto>>

    @GET("api/orientacoes/historico")
    fun historicoOrientacoes(@Query("categoria") categoria: String?): Call<List<OrientacaoDto>>

    @GET("api/orientacoes/{id}")
    fun buscarOrientacao(@Path("id") id: String): Call<OrientacaoDto>

    @POST("api/orientacoes")
    fun criarOrientacao(@Body corpo: OrientacaoRequestDto): Call<OrientacaoDto>

    @PUT("api/orientacoes/{id}")
    fun atualizarOrientacao(@Path("id") id: String, @Body corpo: OrientacaoRequestDto): Call<OrientacaoDto>

    @DELETE("api/orientacoes/{id}")
    fun desativarOrientacao(@Path("id") id: String): Call<Unit>

    @POST("api/ideias")
    fun criarIdeia(@Body corpo: IdeiaRequestDto): Call<IdeiaDto>

    @GET("api/ideias/minhas")
    fun minhasIdeias(): Call<List<IdeiaDto>>

    @GET("api/ideias")
    fun listarIdeias(@Query("status") status: String?): Call<List<IdeiaDto>>

    @GET("api/ideias/{id}")
    fun buscarIdeia(@Path("id") id: String): Call<IdeiaDto>

    @PUT("api/ideias/{id}")
    fun atualizarIdeia(@Path("id") id: String, @Body corpo: IdeiaRequestDto): Call<IdeiaDto>

    @DELETE("api/ideias/{id}")
    fun excluirIdeia(@Path("id") id: String): Call<Unit>

    @PATCH("api/ideias/{id}/priorizacao")
    fun priorizarIdeia(@Path("id") id: String, @Body corpo: PriorizacaoRequestDto): Call<IdeiaDto>

    @PATCH("api/ideias/{id}/aprovar")
    fun aprovarIdeia(@Path("id") id: String, @Body corpo: AprovacaoRequestDto): Call<IdeiaDto>

    @PATCH("api/ideias/{id}/rejeitar")
    fun rejeitarIdeia(@Path("id") id: String, @Body corpo: RejeicaoRequestDto): Call<IdeiaDto>

    @POST("api/ideias/{id}/avaliacao-ia")
    fun avaliarIdeiaComIa(@Path("id") id: String): Call<AvaliacaoIaDto>

    @GET("api/pontos/historico")
    fun historicoPontos(): Call<List<TransacaoDto>>

    @GET("api/pontos/ranking")
    fun ranking(@Query("limite") limite: Int): Call<List<RankingItemDto>>

    @GET("api/projetos")
    fun listarProjetos(): Call<List<ProjetoDto>>

    @GET("api/projetos/{id}")
    fun buscarProjeto(@Path("id") id: String): Call<ProjetoDto>

    @POST("api/projetos")
    fun criarProjeto(@Body corpo: ProjetoRequestDto): Call<ProjetoDto>

    @PUT("api/projetos/{id}")
    fun atualizarProjeto(@Path("id") id: String, @Body corpo: ProjetoRequestDto): Call<ProjetoDto>

    @DELETE("api/projetos/{id}")
    fun excluirProjeto(@Path("id") id: String): Call<Unit>

    @PATCH("api/projetos/{id}/etapa")
    fun alterarEtapaProjeto(@Path("id") id: String, @Body corpo: EtapaRequestDto): Call<ProjetoDto>

    @PATCH("api/projetos/{id}/resultados")
    fun registrarResultados(@Path("id") id: String, @Body corpo: ResultadosRequestDto): Call<ProjetoDto>

    @GET("api/dashboard/resumo")
    fun dashboardResumo(): Call<ResumoDto>

    @GET("api/dashboard/por-estrategia")
    fun dashboardPorEstrategia(): Call<List<EstrategiaDto>>

    @GET("api/dashboard/por-projeto")
    fun dashboardPorProjeto(): Call<List<ProjetoResumoDto>>

    @POST("api/dashboard/insights-ia")
    fun insightsIa(): Call<InsightsDto>

    @GET("api/auditoria")
    fun auditoria(@Query("entidade") entidade: String?, @Query("limite") limite: Int): Call<List<AuditoriaDto>>
}
