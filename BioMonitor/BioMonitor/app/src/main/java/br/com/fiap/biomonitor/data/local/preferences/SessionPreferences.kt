package br.com.fiap.biomonitor.data.local.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class SessionData(
    val userId: Long,
    val email: String,
    val loginTime: Long
)

@Singleton
class SessionPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(userId: Long, email: String) {
        prefs.edit()
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_EMAIL, email)
            .putLong(KEY_LOGIN_TIME, System.currentTimeMillis())
            .apply()
    }

    fun getSession(): SessionData? {
        val userId = prefs.getLong(KEY_USER_ID, -1)
        if (userId == -1L) return null
        return SessionData(
            userId = userId,
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            loginTime = prefs.getLong(KEY_LOGIN_TIME, 0)
        )
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isSessionValid(): Boolean {
        val session = getSession() ?: return false
        val elapsed = System.currentTimeMillis() - session.loginTime
        return elapsed < SESSION_TIMEOUT_MS
    }

    companion object {
        private const val PREFS_NAME = "biomonitor_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val KEY_LOGIN_TIME = "login_time"
        private const val SESSION_TIMEOUT_MS = 7 * 24 * 60 * 60 * 1000L
    }
}
