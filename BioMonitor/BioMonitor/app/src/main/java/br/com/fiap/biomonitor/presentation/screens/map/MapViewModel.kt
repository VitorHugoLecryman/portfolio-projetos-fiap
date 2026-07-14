package br.com.fiap.biomonitor.presentation.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.usecase.sighting.GetAllSightingsUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllSightingsUseCase: GetAllSightingsUseCase
) : ViewModel() {


    data class MapState(
        val sightings: List<Sighting> = emptyList(),
        val filteredSightings: List<Sighting> = emptyList(),
        val userLocation: LatLng? = null,
        val selectedSighting: Sighting? = null,
        val activeFilters: Set<Category> = Category.entries.toSet(),
        val isLoading: Boolean = true,
        val showFilters: Boolean = false,
        val cameraPosition: LatLng = DEFAULT_LOCATION
    )

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state.asStateFlow()

    init {
        loadSightings()
    }


    private fun loadSightings() {
        viewModelScope.launch {
            getAllSightingsUseCase().collect { sightings ->
                _state.update { currentState ->
                    currentState.copy(
                        sightings = sightings,
                        filteredSightings = filterSightings(sightings, currentState.activeFilters),
                        isLoading = false
                    )
                }
            }
        }
    }


    private fun filterSightings(sightings: List<Sighting>, activeFilters: Set<Category>): List<Sighting> {
        return sightings.filter { it.category in activeFilters }
    }


    fun toggleCategoryFilter(category: Category) {
        _state.update { currentState ->
            val newFilters = if (category in currentState.activeFilters) {
                currentState.activeFilters - category
            } else {
                currentState.activeFilters + category
            }
            currentState.copy(
                activeFilters = newFilters,
                filteredSightings = filterSightings(currentState.sightings, newFilters)
            )
        }
    }


    fun selectAllFilters() {
        _state.update { currentState ->
            val allFilters = Category.entries.toSet()
            currentState.copy(
                activeFilters = allFilters,
                filteredSightings = filterSightings(currentState.sightings, allFilters)
            )
        }
    }


    fun clearAllFilters() {
        _state.update { currentState ->
            currentState.copy(
                activeFilters = emptySet(),
                filteredSightings = emptyList()
            )
        }
    }


    fun selectSighting(sighting: Sighting?) {
        _state.update { it.copy(selectedSighting = sighting) }
    }


    fun updateUserLocation(location: LatLng) {
        _state.update { it.copy(userLocation = location) }
    }


    fun updateCameraPosition(position: LatLng) {
        _state.update { it.copy(cameraPosition = position) }
    }


    fun toggleFilters() {
        _state.update { it.copy(showFilters = !it.showFilters) }
    }


    fun hideFilters() {
        _state.update { it.copy(showFilters = false) }
    }


    fun centerOnUserLocation() {
        _state.value.userLocation?.let { location ->
            _state.update { it.copy(cameraPosition = location) }
        }
    }

    companion object {

        val DEFAULT_LOCATION = LatLng(-23.5505, -46.6333)
        const val DEFAULT_ZOOM = 12f
    }
}
