package br.com.fiap.biomonitor.presentation.screens.gamification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import br.com.fiap.biomonitor.domain.model.Badge
import br.com.fiap.biomonitor.domain.model.RankingEntry
import br.com.fiap.biomonitor.domain.model.User
import br.com.fiap.biomonitor.domain.repository.GamificationRepository
import br.com.fiap.biomonitor.domain.repository.UserRepository
import br.com.fiap.biomonitor.domain.usecase.gamification.GetRankingUseCase
import br.com.fiap.biomonitor.domain.usecase.gamification.GetUserBadgesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamificationViewModel @Inject constructor(
    private val getRankingUseCase: GetRankingUseCase,
    private val getUserBadgesUseCase: GetUserBadgesUseCase,
    private val gamificationRepository: GamificationRepository,
    private val userRepository: UserRepository,
    private val sessionPreferences: SessionPreferences
) : ViewModel() {


    enum class RankingPeriod {
        WEEKLY,
        MONTHLY,
        ALL_TIME
    }


    data class GamificationState(
        val currentUser: User? = null,
        val currentUserPosition: Int? = null,
        val rankings: List<RankingEntry> = emptyList(),
        val selectedPeriod: RankingPeriod = RankingPeriod.ALL_TIME,
        val allBadges: List<Badge> = emptyList(),
        val earnedBadges: Set<Long> = emptySet(),
        val isLoading: Boolean = true,
        val isLoadingRankings: Boolean = false,
        val error: String? = null,
        val selectedTab: Int = 0
    )

    private val _state = MutableStateFlow(GamificationState())
    val state: StateFlow<GamificationState> = _state.asStateFlow()

    init {
        loadData()
    }


    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val session = sessionPreferences.getSession()
                val currentUser = session?.let { userRepository.findById(it.userId) }


                val rankings = getRankingUseCase(limit = 10)


                val userPosition = currentUser?.let { user ->
                    rankings.indexOfFirst { it.user.id == user.id }.takeIf { it >= 0 }?.plus(1)
                }


                val allBadges = gamificationRepository.getAllBadges()
                val earnedBadges = currentUser?.let { user ->
                    getUserBadgesUseCase(user.id).map { it.id }.toSet()
                } ?: emptySet()

                _state.update {
                    it.copy(
                        currentUser = currentUser,
                        currentUserPosition = userPosition,
                        rankings = rankings,
                        allBadges = allBadges,
                        earnedBadges = earnedBadges,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar dados"
                    )
                }
            }
        }
    }


    fun setRankingPeriod(period: RankingPeriod) {
        if (period == _state.value.selectedPeriod) return

        _state.update { it.copy(selectedPeriod = period, isLoadingRankings = true) }

        viewModelScope.launch {
            try {
                val rankings = getRankingUseCase(limit = 10)

                val userPosition = _state.value.currentUser?.let { user ->
                    rankings.indexOfFirst { it.user.id == user.id }.takeIf { it >= 0 }?.plus(1)
                }

                _state.update {
                    it.copy(
                        rankings = rankings,
                        currentUserPosition = userPosition,
                        isLoadingRankings = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingRankings = false,
                        error = e.message
                    )
                }
            }
        }
    }


    fun setSelectedTab(tab: Int) {
        _state.update { it.copy(selectedTab = tab) }
    }


    fun refresh() {
        loadData()
    }


    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
