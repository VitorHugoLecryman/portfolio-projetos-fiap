package br.com.fiap.inovagab.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requisicao = chain.request()
        val token = sessionManager.getToken()

        if (token.isNullOrBlank() || requisicao.url.encodedPath.endsWith("/auth/login")) {
            return chain.proceed(requisicao)
        }

        val autenticada = requisicao.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(autenticada)
    }
}
