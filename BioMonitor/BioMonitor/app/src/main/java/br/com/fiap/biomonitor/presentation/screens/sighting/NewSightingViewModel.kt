package br.com.fiap.biomonitor.presentation.screens.sighting

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.biomonitor.data.local.preferences.SessionPreferences
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Habitat
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.model.Species
import br.com.fiap.biomonitor.domain.model.SyncStatus
import br.com.fiap.biomonitor.domain.usecase.sighting.CreateSightingUseCase
import br.com.fiap.biomonitor.domain.usecase.species.SearchSpeciesUseCase
import br.com.fiap.biomonitor.util.ErrorMessages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NewSightingViewModel @Inject constructor(
    private val createSightingUseCase: CreateSightingUseCase,
    private val searchSpeciesUseCase: SearchSpeciesUseCase,
    private val sessionPreferences: SessionPreferences
) : ViewModel() {


    data class NewSightingState(
        val photoUri: Uri? = null,
        val photoPath: String? = null,
        val category: Category? = null,
        val speciesName: String = "",
        val scientificName: String? = null,
        val speciesSuggestions: List<Species> = emptyList(),
        val showSuggestions: Boolean = false,
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val hasLocation: Boolean = false,
        val dateTime: LocalDateTime = LocalDateTime.now(),
        val habitat: Habitat? = null,
        val observations: String = "",
        val isLoading: Boolean = false,
        val isSearchingSpecies: Boolean = false,
        val isSaved: Boolean = false,
        val errors: Map<String, String> = emptyMap(),
        val showCamera: Boolean = false,
        val showDatePicker: Boolean = false
    )

    private val _state = MutableStateFlow(NewSightingState())
    val state: StateFlow<NewSightingState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun setPhotoUri(uri: Uri?, path: String?) {
        _state.update {
            it.copy(
                photoUri = uri,
                photoPath = path,
                errors = it.errors - "photo"
            )
        }
    }

    fun setCategory(category: Category) {
        _state.update {
            it.copy(
                category = category,
                errors = it.errors - "category"
            )
        }
    }

    fun onSpeciesNameChange(name: String) {
        _state.update {
            it.copy(
                speciesName = name,
                scientificName = null,
                errors = it.errors - "species"
            )
        }


        searchJob?.cancel()
        if (name.length >= 3) {
            searchJob = viewModelScope.launch {
                delay(300)
                searchSpecies(name)
            }
        } else {
            _state.update { it.copy(speciesSuggestions = emptyList(), showSuggestions = false) }
        }
    }

    fun selectSpecies(species: Species) {
        _state.update {
            it.copy(
                speciesName = species.commonName ?: species.name,
                scientificName = species.scientificName,
                speciesSuggestions = emptyList(),
                showSuggestions = false,
                errors = it.errors - "species"
            )
        }
    }

    fun dismissSuggestions() {
        _state.update { it.copy(showSuggestions = false) }
    }

    private fun searchSpecies(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isSearchingSpecies = true) }

            searchSpeciesUseCase(query)
                .onSuccess { species ->
                    _state.update {
                        it.copy(
                            speciesSuggestions = species,
                            showSuggestions = species.isNotEmpty(),
                            isSearchingSpecies = false
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            speciesSuggestions = emptyList(),
                            showSuggestions = false,
                            isSearchingSpecies = false
                        )
                    }
                }
        }
    }

    fun setLocation(latitude: Double, longitude: Double) {
        _state.update {
            it.copy(
                latitude = latitude,
                longitude = longitude,
                hasLocation = true,
                errors = it.errors - "location"
            )
        }
    }

    fun setDateTime(dateTime: LocalDateTime) {
        _state.update { it.copy(dateTime = dateTime, showDatePicker = false) }
    }

    fun setHabitat(habitat: Habitat?) {
        _state.update { it.copy(habitat = habitat) }
    }

    fun onObservationsChange(observations: String) {
        _state.update { it.copy(observations = observations) }
    }

    fun showCamera() {
        _state.update { it.copy(showCamera = true) }
    }

    fun hideCamera() {
        _state.update { it.copy(showCamera = false) }
    }

    fun showDatePicker() {
        _state.update { it.copy(showDatePicker = true) }
    }

    fun hideDatePicker() {
        _state.update { it.copy(showDatePicker = false) }
    }


    private fun validateSighting(): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        val currentState = _state.value

        if (currentState.photoPath == null) {
            errors["photo"] = ErrorMessages.PHOTO_REQUIRED
        }

        if (currentState.category == null) {
            errors["category"] = ErrorMessages.CATEGORY_REQUIRED
        }

        if (currentState.speciesName.isBlank()) {
            errors["species"] = ErrorMessages.SPECIES_REQUIRED
        }

        if (!currentState.hasLocation) {
            errors["location"] = ErrorMessages.LOCATION_REQUIRED
        }

        return errors
    }


    fun saveSighting() {
        val errors = validateSighting()
        if (errors.isNotEmpty()) {
            _state.update { it.copy(errors = errors) }
            return
        }

        val currentState = _state.value
        val userId = sessionPreferences.getSession()?.userId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errors = emptyMap()) }

            val sighting = Sighting(
                userId = userId,
                photoPath = currentState.photoPath!!,
                category = currentState.category!!,
                speciesName = currentState.speciesName,
                scientificName = currentState.scientificName,
                latitude = currentState.latitude,
                longitude = currentState.longitude,
                dateTime = currentState.dateTime,
                habitat = currentState.habitat,
                observations = currentState.observations,
                syncStatus = SyncStatus.PENDING
            )

            createSightingUseCase(sighting)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSaved = true
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errors = mapOf("save" to (exception.message ?: ErrorMessages.UNKNOWN_ERROR))
                        )
                    }
                }
        }
    }


    fun resetSavedState() {
        _state.update { it.copy(isSaved = false) }
    }
}
