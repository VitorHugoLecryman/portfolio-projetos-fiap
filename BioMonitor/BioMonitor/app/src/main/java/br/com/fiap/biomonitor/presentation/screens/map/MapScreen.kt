package br.com.fiap.biomonitor.presentation.screens.map

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Sighting
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.io.File
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MapScreen(
    onNavigateToNewSighting: () -> Unit,
    onNavigateToSightingDetails: (Long) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()


    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.cameraPosition, MapViewModel.DEFAULT_ZOOM)
    }


    val sheetState = rememberModalBottomSheetState()


    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }


    LaunchedEffect(state.cameraPosition) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLng(state.cameraPosition)
        )
    }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                SmallFloatingActionButton(
                    onClick = { viewModel.toggleFilters() },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtros"
                    )
                }


                SmallFloatingActionButton(
                    onClick = { viewModel.centerOnUserLocation() },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Minha localização"
                    )
                }


                FloatingActionButton(
                    onClick = onNavigateToNewSighting,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Novo avistamento"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionState.status.isGranted
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                ),
                onMapClick = { viewModel.selectSighting(null) }
            ) {

                state.filteredSightings.forEach { sighting ->
                    SightingMarker(
                        sighting = sighting,
                        onClick = {
                            viewModel.selectSighting(sighting)
                            scope.launch { sheetState.show() }
                        }
                    )
                }
            }


            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            AnimatedVisibility(
                visible = state.showFilters,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it },
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                FilterPanel(
                    activeFilters = state.activeFilters,
                    onToggleFilter = viewModel::toggleCategoryFilter,
                    onSelectAll = viewModel::selectAllFilters,
                    onClearAll = viewModel::clearAllFilters,
                    onClose = viewModel::hideFilters
                )
            }


            SightingCountBadge(
                count = state.filteredSightings.size,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
        }


        if (state.selectedSighting != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.selectSighting(null) },
                sheetState = sheetState
            ) {
                SightingPreviewCard(
                    sighting = state.selectedSighting!!,
                    onViewDetails = {
                        scope.launch { sheetState.hide() }
                        onNavigateToSightingDetails(state.selectedSighting!!.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun SightingMarker(
    sighting: Sighting,
    onClick: () -> Unit
) {
    val markerColor = getCategoryHue(sighting.category)

    Marker(
        state = MarkerState(position = LatLng(sighting.latitude, sighting.longitude)),
        title = sighting.speciesName,
        snippet = sighting.category.displayName,
        icon = BitmapDescriptorFactory.defaultMarker(markerColor),
        onClick = {
            onClick()
            true
        }
    )
}

private fun getCategoryHue(category: Category): Float {
    return when (category) {
        Category.FLORA -> BitmapDescriptorFactory.HUE_GREEN
        Category.AVES -> BitmapDescriptorFactory.HUE_AZURE
        Category.MAMIFEROS -> BitmapDescriptorFactory.HUE_ORANGE
        Category.REPTEIS_ANFIBIOS -> BitmapDescriptorFactory.HUE_VIOLET
        Category.INSETOS -> BitmapDescriptorFactory.HUE_YELLOW
        Category.PEIXES -> BitmapDescriptorFactory.HUE_CYAN
        Category.FUNGOS -> BitmapDescriptorFactory.HUE_ROSE
        Category.OUTROS -> BitmapDescriptorFactory.HUE_RED
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterPanel(
    activeFilters: Set<Category>,
    onToggleFilter: (Category) -> Unit,
    onSelectAll: () -> Unit,
    onClearAll: () -> Unit,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtrar por categoria",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Category.entries.forEach { category ->
                    CategoryFilterChip(
                        category = category,
                        isSelected = category in activeFilters,
                        onClick = { onToggleFilter(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onClearAll) {
                    Text("Limpar")
                }

                TextButton(onClick = onSelectAll) {
                    Text("Selecionar todos")
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterChip(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val categoryColor = Color(category.color)

    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(category.displayName) },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(categoryColor)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = categoryColor.copy(alpha = 0.2f),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun SightingCountBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = "$count avistamentos",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun SightingPreviewCard(
    sighting: Sighting,
    onViewDetails: () -> Unit
) {
    val context = LocalContext.current
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(sighting.photoPath))
                    .crossfade(true)
                    .build(),
                contentDescription = sighting.speciesName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = sighting.speciesName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                sighting.scientificName?.let { scientificName ->
                    Text(
                        text = scientificName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(sighting.category.color))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sighting.category.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = sighting.dateTime.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onViewDetails,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Ver detalhes")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
