package br.com.fiap.inovagab.network

import android.content.Context
import br.com.fiap.inovagab.BuildConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.concurrent.TimeUnit

object ApiClient {

    private lateinit var sessao: SessionManager
    private lateinit var servico: InovaGabApi

    fun inicializar(context: Context) {
        if (::servico.isInitialized) {
            return
        }

        sessao = SessionManager(context.applicationContext)

        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDeserializer { elemento, _, _ ->
                val texto = elemento.asString
                try {
                    Date.from(Instant.parse(texto))
                } catch (erro: DateTimeParseException) {
                    null
                }
            })
            .create()

        val log = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
            else HttpLoggingInterceptor.Level.NONE
        }

        val cliente = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessao))
            .addInterceptor(log)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        servico = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(InovaGabApi::class.java)
    }

    val api: InovaGabApi
        get() = servico

    val sessionManager: SessionManager
        get() = sessao

    fun <T> executar(
        chamada: Call<T>,
        mensagemErro: String,
        onSuccess: (T?) -> Unit,
        onError: (String) -> Unit
    ) {
        chamada.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onError(extrairMensagem(response, mensagemErro))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError("Nao foi possivel falar com o servidor. Verifique se a API esta no ar.")
            }
        })
    }

    private fun <T> extrairMensagem(response: Response<T>, padrao: String): String {
        val corpo = try {
            response.errorBody()?.string()
        } catch (erro: Exception) {
            null
        }

        if (corpo.isNullOrBlank()) {
            return padrao
        }

        return try {
            val erro = GsonBuilder().create().fromJson(corpo, ErroDto::class.java)
            erro?.mensagem?.takeIf { it.isNotBlank() } ?: padrao
        } catch (erro: Exception) {
            padrao
        }
    }
}
