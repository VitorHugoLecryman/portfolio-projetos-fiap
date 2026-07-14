package br.com.fiap.biomonitor.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.domain.usecase.auth.CheckSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase
) : ViewModel() {


    sealed class SplashState {

        data object Loading : SplashState()


        data object NavigateToLogin : SplashState()


        data object NavigateToMap : SplashState()
    }

    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    init {
        checkSession()
    }


    private fun checkSession() {
        viewModelScope.launch {

            delay(SPLASH_DURATION_MS)

            val hasValidSession = checkSessionUseCase()
            _state.value = if (hasValidSession) {
                SplashState.NavigateToMap
            } else {
                SplashState.NavigateToLogin
            }
        }
    }

    companion object {
        private const val SPLASH_DURATION_MS = 2000L
    }
}
