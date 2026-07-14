package br.com.fiap.biomonitor.presentation.screens.sighting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.repository.SightingRepository
import br.com.fiap.biomonitor.domain.usecase.sighting.DeleteSightingUseCase
import br.com.fiap.biomonitor.domain.usecase.species.SearchSpeciesUseCase
import br.com.fiap.biomonitor.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SightingDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sightingRepository: SightingRepository,
    private val deleteSightingUseCase: DeleteSightingUseCase,
    private val searchSpeciesUseCase: SearchSpeciesUseCase
) : ViewModel() {

    private val sightingId: Long = savedStateHandle.get<Long>(Screen.SIGHTING_ID_ARG) ?: 0L

    data class SightingDetailsState(
        val sighting: Sighting? = null,
        val conservationStatus: String? = null,
        val isLoading: Boolean = true,
        val isDeleting: Boolean = false,
        val error: String? = null,
        val showDeleteDialog: Boolean = false,
        val isDeleted: Boolean = false,
        val showFullscreenPhoto: Boolean = false
    )

    private val _state = MutableStateFlow(SightingDetailsState())
    val state: StateFlow<SightingDetailsState> = _state.asStateFlow()

    init {
        loadSighting()
    }

    private fun loadSighting() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val sighting = sightingRepository.findById(sightingId)
                if (sighting != null) {
                    _state.update {
                        it.copy(
                            sighting = sighting,
                            isLoading = false
                        )
                    }
                    sighting.scientificName?.let { loadConservationStatus(it) }
                } else {
                    _state.update {
                        it.copy(isLoading = false, error = "Avistamento não encontrado")
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = e.message ?: "Erro ao carregar avistamento")
                }
            }
        }
    }


    private fun loadConservationStatus(scientificName: String) {
        viewModelScope.launch {

            searchSpeciesUseCase(scientificName)
                .onSuccess { speciesList ->
                    val firstSpecies = speciesList.firstOrNull()
                    if (firstSpecies != null) {
                        _state.update {
                            it.copy(conservationStatus = firstSpecies.conservationStatus)
                        }
                    }
                }
        }
    }

    fun showDeleteDialog() {
        _state.update { it.copy(showDeleteDialog = true) }
    }

    fun hideDeleteDialog() {
        _state.update { it.copy(showDeleteDialog = false) }
    }

    fun confirmDelete() {
        val sighting = _state.value.sighting ?: return

        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, showDeleteDialog = false) }

            deleteSightingUseCase(sighting)
                .onSuccess {
                    _state.update { it.copy(isDeleting = false, isDeleted = true) }
                }
                .onFailure { exception ->
                    _state.update { it.copy(isDeleting = false, error = exception.message) }
                }
        }
    }

    fun showFullscreenPhoto() {
        _state.update { it.copy(showFullscreenPhoto = true) }
    }

    fun hideFullscreenPhoto() {
        _state.update { it.copy(showFullscreenPhoto = false) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}