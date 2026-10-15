package br.com.fiap.inovagab.network

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = criarPreferencias(context)

    private fun criarPreferencias(context: Context): SharedPreferences {
        return try {
            val chave = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            EncryptedSharedPreferences.create(
                ARQUIVO_SEGURO,
                chave,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (erro: Exception) {
            context.getSharedPreferences(ARQUIVO_SIMPLES, Context.MODE_PRIVATE)
        }
    }

    fun salvarSessao(token: String, id: String, nome: String, email: String, perfil: String, pontos: Int) {
        prefs.edit()
            .putString(CHAVE_TOKEN, token)
            .putString(CHAVE_ID, id)
            .putString(CHAVE_NOME, nome)
            .putString(CHAVE_EMAIL, email)
            .putString(CHAVE_PERFIL, perfil)
            .putInt(CHAVE_PONTOS, pontos)
            .apply()
    }

    fun getToken(): String? = prefs.getString(CHAVE_TOKEN, null)

    fun getUsuarioId(): String? = prefs.getString(CHAVE_ID, null)

    fun getNome(): String = prefs.getString(CHAVE_NOME, "") ?: ""

    fun getEmail(): String = prefs.getString(CHAVE_EMAIL, "") ?: ""

    fun getPerfil(): String = prefs.getString(CHAVE_PERFIL, "") ?: ""

    fun getPontos(): Int = prefs.getInt(CHAVE_PONTOS, 0)

    fun atualizarPontos(pontos: Int) {
        prefs.edit().putInt(CHAVE_PONTOS, pontos).apply()
    }

    fun estaLogado(): Boolean = !getToken().isNullOrBlank()

    fun limpar() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val ARQUIVO_SEGURO = "inovagab_sessao_segura"
        const val ARQUIVO_SIMPLES = "inovagab_sessao"
        const val CHAVE_TOKEN = "token"
        const val CHAVE_ID = "usuario_id"
        const val CHAVE_NOME = "usuario_nome"
        const val CHAVE_EMAIL = "usuario_email"
        const val CHAVE_PERFIL = "usuario_perfil"
        const val CHAVE_PONTOS = "usuario_pontos"
    }
}
