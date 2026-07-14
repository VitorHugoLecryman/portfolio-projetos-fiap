package br.com.fiap.biomonitor.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import br.com.fiap.biomonitor.domain.model.Badge
import br.com.fiap.biomonitor.domain.model.User
import br.com.fiap.biomonitor.domain.repository.GamificationRepository
import br.com.fiap.biomonitor.domain.repository.SightingRepository
import br.com.fiap.biomonitor.domain.repository.UserRepository
import br.com.fiap.biomonitor.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sightingRepository: SightingRepository,
    private val gamificationRepository: GamificationRepository,
    private val sessionPreferences: SessionPreferences,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {


    data class UserStats(
        val totalSightings: Int = 0,
        val uniqueSpecies: Int = 0,
        val totalPoints: Int = 0
    )


    data class ProfileState(
        val user: User? = null,
        val stats: UserStats = UserStats(),
        val badges: List<Badge> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null,
        val showLogoutDialog: Boolean = false,
        val showDeleteAccountDialog: Boolean = false,
        val isLoggingOut: Boolean = false,
        val isDeletingAccount: Boolean = false,
        val logoutSuccess: Boolean = false,
        val deleteSuccess: Boolean = false
    )

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadProfile()
    }


    private fun loadProfile() {
        val session = sessionPreferences.getSession() ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {

                val user = userRepository.findById(session.userId)

                if (user != null) {

                    val totalSightings = sightingRepository.countByUser(user.id)
                    val uniqueSpecies = sightingRepository.countUniqueSpeciesByUser(user.id)


                    val badges = gamificationRepository.getUserBadges(user.id)

                    _state.update {
                        it.copy(
                            user = user,
                            stats = UserStats(
                                totalSightings = totalSightings,
                                uniqueSpecies = uniqueSpecies,
                                totalPoints = user.points
                            ),
                            badges = badges,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Usuário não encontrado"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar perfil"
                    )
                }
            }
        }
    }


    fun refresh() {
        loadProfile()
    }


    fun showLogoutDialog() {
        _state.update { it.copy(showLogoutDialog = true) }
    }


    fun hideLogoutDialog() {
        _state.update { it.copy(showLogoutDialog = false) }
    }


    fun confirmLogout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingOut = true, showLogoutDialog = false) }

            logoutUseCase()

            _state.update {
                it.copy(
                    isLoggingOut = false,
                    logoutSuccess = true
                )
            }
        }
    }


    fun showDeleteAccountDialog() {
        _state.update { it.copy(showDeleteAccountDialog = true) }
    }


    fun hideDeleteAccountDialog() {
        _state.update { it.copy(showDeleteAccountDialog = false) }
    }


    fun confirmDeleteAccount() {
        val user = _state.value.user ?: return

        viewModelScope.launch {
            _state.update { it.copy(isDeletingAccount = true, showDeleteAccountDialog = false) }

            userRepository.delete(user)
                .onSuccess {
                    sessionPreferences.clearSession()
                    _state.update {
                        it.copy(
                            isDeletingAccount = false,
                            deleteSuccess = true
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isDeletingAccount = false,
                            error = exception.message
                        )
                    }
                }
        }
    }


    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
