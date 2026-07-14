package br.com.fiap.biomonitor.presentation.screens.sighting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.usecase.sighting.DeleteSightingUseCase
import br.com.fiap.biomonitor.domain.usecase.sighting.GetUserSightingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySightingsViewModel @Inject constructor(
    private val getUserSightingsUseCase: GetUserSightingsUseCase,
    private val deleteSightingUseCase: DeleteSightingUseCase,
    private val sessionPreferences: SessionPreferences
) : ViewModel() {


    data class MySightingsState(
        val sightings: List<Sighting> = emptyList(),
        val filteredSightings: List<Sighting> = emptyList(),
        val searchQuery: String = "",
        val selectedCategory: Category? = null,
        val isLoading: Boolean = true,
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val sightingToDelete: Sighting? = null,
        val showDeleteDialog: Boolean = false,
        val deleteSuccess: Boolean = false
    )

    private val _state = MutableStateFlow(MySightingsState())
    val state: StateFlow<MySightingsState> = _state.asStateFlow()

    init {
        loadSightings()
    }


    private fun loadSightings() {
        val userId = sessionPreferences.getSession()?.userId ?: return

        viewModelScope.launch {
            getUserSightingsUseCase(userId).collect { sightings ->
                _state.update { currentState ->
                    currentState.copy(
                        sightings = sightings,
                        filteredSightings = filterSightings(
                            sightings,
                            currentState.searchQuery,
                            currentState.selectedCategory
                        ),
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        }
    }


    fun refresh() {
        _state.update { it.copy(isRefreshing = true) }
        loadSightings()
    }


    fun onSearchQueryChange(query: String) {
        _state.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredSightings = filterSightings(
                    currentState.sightings,
                    query,
                    currentState.selectedCategory
                )
            )
        }
    }


    fun setCategory(category: Category?) {
        _state.update { currentState ->
            currentState.copy(
                selectedCategory = category,
                filteredSightings = filterSightings(
                    currentState.sightings,
                    currentState.searchQuery,
                    category
                )
            )
        }
    }


    private fun filterSightings(
        sightings: List<Sighting>,
        searchQuery: String,
        category: Category?
    ): List<Sighting> {
        return sightings.filter { sighting ->
            val matchesSearch = searchQuery.isBlank() ||
                sighting.speciesName.contains(searchQuery, ignoreCase = true) ||
                sighting.scientificName?.contains(searchQuery, ignoreCase = true) == true

            val matchesCategory = category == null || sighting.category == category

            matchesSearch && matchesCategory
        }.sortedByDescending { it.dateTime }
    }


    fun showDeleteDialog(sighting: Sighting) {
        _state.update {
            it.copy(
                sightingToDelete = sighting,
                showDeleteDialog = true
            )
        }
    }


    fun hideDeleteDialog() {
        _state.update {
            it.copy(
                sightingToDelete = null,
                showDeleteDialog = false
            )
        }
    }


    fun confirmDelete() {
        val sighting = _state.value.sightingToDelete ?: return

        viewModelScope.launch {
            deleteSightingUseCase(sighting)
                .onSuccess {
                    _state.update {
                        it.copy(
                            showDeleteDialog = false,
                            sightingToDelete = null,
                            deleteSuccess = true
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            showDeleteDialog = false,
                            sightingToDelete = null,
                            error = exception.message
                        )
                    }
                }
        }
    }


    fun clearError() {
        _state.update { it.copy(error = null) }
    }


    fun resetDeleteSuccess() {
        _state.update { it.copy(deleteSuccess = false) }
    }
}
