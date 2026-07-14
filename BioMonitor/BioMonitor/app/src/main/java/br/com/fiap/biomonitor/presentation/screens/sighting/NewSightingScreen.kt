package br.com.fiap.biomonitor.presentation.screens.sighting

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Habitat
import br.com.fiap.biomonitor.presentation.components.BioButton
import br.com.fiap.biomonitor.presentation.components.BioTextArea
import br.com.fiap.biomonitor.presentation.components.BioTextField
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class, ExperimentalLayoutApi::class)
@Composable
fun NewSightingScreen(
    onNavigateBack: () -> Unit,
    onSightingSaved: () -> Unit,
    viewModel: NewSightingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }


    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)


    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var photoFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null && photoFile != null) {
            viewModel.setPhotoUri(photoUri, photoFile!!.absolutePath)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {

            val file = File(context.cacheDir, "sighting_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            viewModel.setPhotoUri(uri, file.absolutePath)
        }
    }


    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted && !state.hasLocation) {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        viewModel.setLocation(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {

            }
        }
    }


    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onSightingSaved()
            viewModel.resetSavedState()
        }
    }


    LaunchedEffect(state.errors) {
        state.errors["save"]?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Avistamento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .imePadding()
        ) {

            Text(
                text = "Foto *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            PhotoSection(
                photoUri = state.photoUri,
                error = state.errors["photo"],
                onCameraClick = {
                    if (cameraPermissionState.status.isGranted) {
                        val file = File(context.cacheDir, "sighting_${System.currentTimeMillis()}.jpg")
                        photoFile = file
                        photoUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )
                        cameraLauncher.launch(photoUri!!)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
                onGalleryClick = {
                    galleryLauncher.launch("image/*")
                },
                onRemovePhoto = {
                    viewModel.setPhotoUri(null, null)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Categoria *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            CategorySelector(
                selectedCategory = state.category,
                error = state.errors["category"],
                onCategorySelected = viewModel::setCategory
            )

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Nome da Espécie *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            SpeciesAutocomplete(
                value = state.speciesName,
                onValueChange = viewModel::onSpeciesNameChange,
                suggestions = state.speciesSuggestions,
                showSuggestions = state.showSuggestions,
                isSearching = state.isSearchingSpecies,
                onSuggestionSelected = viewModel::selectSpecies,
                onDismissSuggestions = viewModel::dismissSuggestions,
                error = state.errors["species"]
            )

            state.scientificName?.let { scientificName ->
                Text(
                    text = "Nome científico: $scientificName",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Localização *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LocationSection(
                hasLocation = state.hasLocation,
                latitude = state.latitude,
                longitude = state.longitude,
                error = state.errors["location"],
                onRequestLocation = {
                    if (!locationPermissionState.status.isGranted) {
                        locationPermissionState.launchPermissionRequest()
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Data e Hora",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            DateTimeSection(
                dateTime = state.dateTime,
                onShowDatePicker = viewModel::showDatePicker
            )


            if (state.showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = state.dateTime
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                )

                DatePickerDialog(
                    onDismissRequest = viewModel::hideDatePicker,
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val newDate = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime()
                                    viewModel.setDateTime(newDate)
                                }
                            }
                        ) {
                            Text("Confirmar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideDatePicker) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Habitat",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            HabitatSelector(
                selectedHabitat = state.habitat,
                onHabitatSelected = viewModel::setHabitat
            )

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Observações",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            BioTextArea(
                value = state.observations,
                onValueChange = viewModel::onObservationsChange,
                label = "Observações adicionais",
                placeholder = "Descreva detalhes do avistamento..."
            )

            Spacer(modifier = Modifier.height(32.dp))


            BioButton(
                text = "Salvar Avistamento",
                onClick = { viewModel.saveSighting() },
                isLoading = state.isLoading,
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PhotoSection(
    photoUri: Uri?,
    error: String?,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onRemovePhoto: () -> Unit
) {
    val context = LocalContext.current

    if (photoUri != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photoUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto do avistamento",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onRemovePhoto,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remover foto"
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PhotoOptionCard(
                icon = Icons.Default.CameraAlt,
                label = "Câmera",
                onClick = onCameraClick,
                modifier = Modifier.weight(1f)
            )

            PhotoOptionCard(
                icon = Icons.Default.PhotoLibrary,
                label = "Galeria",
                onClick = onGalleryClick,
                modifier = Modifier.weight(1f)
            )
        }
    }

    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun PhotoOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySelector(
    selectedCategory: Category?,
    error: String?,
    onCategorySelected: (Category) -> Unit
) {
    Column {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Category.entries.forEach { category ->
                val isSelected = category == selectedCategory
                val categoryColor = Color(category.color)

                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.displayName) },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(categoryColor)
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = categoryColor.copy(alpha = 0.2f)
                    )
                )
            }
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpeciesAutocomplete(
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<br.com.fiap.biomonitor.domain.model.Species>,
    showSuggestions: Boolean,
    isSearching: Boolean,
    onSuggestionSelected: (br.com.fiap.biomonitor.domain.model.Species) -> Unit,
    onDismissSuggestions: () -> Unit,
    error: String?
) {
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(showSuggestions) {
        expanded = showSuggestions
    }

    ExposedDropdownMenuBox(
        expanded = expanded && suggestions.isNotEmpty(),
        onExpandedChange = { }
    ) {
        BioTextField(
            value = value,
            onValueChange = onValueChange,
            label = "Nome da espécie",
            placeholder = "Digite para buscar...",
            isError = error != null,
            errorMessage = error,
            trailingIcon = {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded && suggestions.isNotEmpty(),
            onDismissRequest = {
                expanded = false
                onDismissSuggestions()
            }
        ) {
            suggestions.take(10).forEach { species ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = species.commonName ?: species.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            species.scientificName?.let { scientificName ->
                                Text(
                                    text = scientificName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }
                    },
                    onClick = {
                        onSuggestionSelected(species)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun LocationSection(
    hasLocation: Boolean,
    latitude: Double,
    longitude: Double,
    error: String?,
    onRequestLocation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (hasLocation)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onRequestLocation)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = if (hasLocation)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                if (hasLocation) {
                    Text(
                        text = "Localização obtida",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "%.6f, %.6f".format(latitude, longitude),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "Toque para obter localização",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (hasLocation) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun DateTimeSection(
    dateTime: LocalDateTime,
    onShowDatePicker: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onShowDatePicker),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = dateTime.format(formatter),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HabitatSelector(
    selectedHabitat: Habitat?,
    onHabitatSelected: (Habitat?) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Habitat.entries.forEach { habitat ->
            val isSelected = habitat == selectedHabitat

            FilterChip(
                selected = isSelected,
                onClick = {
                    onHabitatSelected(if (isSelected) null else habitat)
                },
                label = { Text(habitat.displayName) },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else null
            )
        }
    }
}
