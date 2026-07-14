package br.com.fiap.biomonitor.presentation.screens.sighting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.model.SyncStatus
import br.com.fiap.biomonitor.presentation.components.LoadingIndicator
import br.com.fiap.biomonitor.presentation.components.SyncStatusIndicator
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.io.File
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SightingDetailsScreen(
    sightingId: Long,
    onNavigateBack: () -> Unit,
    onSightingDeleted: () -> Unit,
    viewModel: SightingDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) {
            onSightingDeleted()
        }
    }


    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Avistamento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartilhar"
                        )
                    }
                    IconButton(onClick = viewModel::showDeleteDialog) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Excluir",
                            tint = MaterialTheme.colorScheme.error
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
        when {
            state.isLoading || state.isDeleting -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            state.sighting == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Avistamento não encontrado",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                SightingDetailsContent(
                    sighting = state.sighting!!,
                    conservationStatus = state.conservationStatus,
                    onPhotoClick = viewModel::showFullscreenPhoto,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }


        if (state.showFullscreenPhoto && state.sighting != null) {
            FullscreenPhotoDialog(
                photoPath = state.sighting!!.photoPath,
                onDismiss = viewModel::hideFullscreenPhoto
            )
        }


        if (state.showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirm = viewModel::confirmDelete,
                onDismiss = viewModel::hideDeleteDialog
            )
        }
    }
}

@Composable
private fun SightingDetailsContent(
    sighting: Sighting,
    conservationStatus: String?,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm") }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {

        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(File(sighting.photoPath))
                .crossfade(true)
                .build(),
            contentDescription = sighting.speciesName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .clickable(onClick = onPhotoClick)
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sighting.speciesName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    sighting.scientificName?.let { scientificName ->
                        Text(
                            text = scientificName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                CategoryBadge(
                    category = sighting.category.displayName,
                    color = Color(sighting.category.color)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            conservationStatus?.let { status ->
                ConservationStatusCard(status = status)
                Spacer(modifier = Modifier.height(16.dp))
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoCard(
                    icon = Icons.Default.CalendarMonth,
                    label = "Data",
                    value = sighting.dateTime.format(dateFormatter),
                    modifier = Modifier.weight(1f)
                )

                InfoCard(
                    icon = Icons.Default.Sync,
                    label = "Status",
                    value = getSyncStatusText(sighting.syncStatus),
                    modifier = Modifier.weight(1f),
                    trailing = {
                        SyncStatusIndicator(syncStatus = sighting.syncStatus)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            sighting.habitat?.let { habitat ->
                InfoCard(
                    icon = Icons.Default.Forest,
                    label = "Habitat",
                    value = habitat.displayName
                )
                Spacer(modifier = Modifier.height(12.dp))
            }


            LocationCard(
                latitude = sighting.latitude,
                longitude = sighting.longitude
            )


            if (sighting.observations.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Observações",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = sighting.observations,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CategoryBadge(
    category: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ConservationStatusCard(status: String) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "CR", "CRITICALLY ENDANGERED" -> Pair(Color(0xFFFFCDD2), Color(0xFFC62828))
        "EN", "ENDANGERED" -> Pair(Color(0xFFFFE0B2), Color(0xFFEF6C00))
        "VU", "VULNERABLE" -> Pair(Color(0xFFFFF9C4), Color(0xFFF9A825))
        "NT", "NEAR THREATENED" -> Pair(Color(0xFFE1F5FE), Color(0xFF0277BD))
        "LC", "LEAST CONCERN" -> Pair(Color(0xFFC8E6C9), Color(0xFF2E7D32))
        else -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Status de Conservação:",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
private fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            trailing?.invoke()
        }
    }
}

@Composable
private fun LocationCard(
    latitude: Double,
    longitude: Double
) {
    val position = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        this.position = CameraPosition.fromLatLngZoom(position, 15f)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Localização",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "%.6f, %.6f".format(latitude, longitude),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = com.google.maps.android.compose.MapUiSettings(
                        zoomControlsEnabled = false,
                        scrollGesturesEnabled = false,
                        zoomGesturesEnabled = false,
                        tiltGesturesEnabled = false,
                        rotationGesturesEnabled = false
                    )
                ) {
                    Marker(
                        state = MarkerState(position = position)
                    )
                }
            }
        }
    }
}

@Composable
private fun FullscreenPhotoDialog(
    photoPath: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(photoPath))
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto em tela cheia",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Fechar",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir avistamento?") },
        text = {
            Text("Esta ação não pode ser desfeita. O avistamento será removido permanentemente.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Excluir",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun getSyncStatusText(status: SyncStatus): String {
    return when (status) {
        SyncStatus.SYNCED -> "Sincronizado"
        SyncStatus.PENDING -> "Pendente"
        SyncStatus.FAILED -> "Falha"
    }
}
