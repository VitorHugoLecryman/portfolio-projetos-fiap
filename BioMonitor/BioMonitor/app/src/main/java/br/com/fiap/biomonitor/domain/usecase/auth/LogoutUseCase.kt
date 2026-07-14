package br.com.fiap.biomonitor.domain.usecase.auth

import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionPreferences: SessionPreferences
) {
    operator fun invoke() {
        sessionPreferences.clearSession()
    }
}
