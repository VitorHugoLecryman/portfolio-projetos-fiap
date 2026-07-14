package br.com.fiap.biomonitor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.domain.model.SyncStatus
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.time.format.DateTimeFormatter

@Composable
fun SightingCard(
    sighting: Sighting,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            SightingThumbnail(
                photoPath = sighting.photoPath,
                category = sighting.category,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))


            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sighting.speciesName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CategoryDot(category = sighting.category)
                }


                if (!sighting.scientificName.isNullOrBlank()) {
                    Text(
                        text = sighting.scientificName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SightingInfoChip(
                        icon = Icons.Default.CalendarToday,
                        text = sighting.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))


                    SyncStatusIndicator(syncStatus = sighting.syncStatus)
                }


                SightingInfoChip(
                    icon = Icons.Default.LocationOn,
                    text = formatLocation(sighting.latitude, sighting.longitude),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SightingCardCompact(
    sighting: Sighting,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            SightingThumbnail(
                photoPath = sighting.photoPath,
                category = sighting.category,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CategoryDot(category = sighting.category, size = 8)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sighting.speciesName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = sighting.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            SyncStatusIndicator(syncStatus = sighting.syncStatus)
        }
    }
}

@Composable
fun SightingThumbnail(
    photoPath: String,
    category: Category,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(category)

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(categoryColor.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        if (photoPath.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoPath)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto do avistamento",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(MaterialTheme.shapes.medium)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Photo,
                contentDescription = "Sem foto",
                tint = categoryColor.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun SyncStatusIndicator(
    syncStatus: SyncStatus,
    modifier: Modifier = Modifier
) {
    val (icon, color, description) = when (syncStatus) {
        SyncStatus.SYNCED -> Triple(
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.primary,
            "Sincronizado"
        )
        SyncStatus.PENDING -> Triple(
            Icons.Default.CloudSync,
            MaterialTheme.colorScheme.tertiary,
            "Pendente"
        )
        SyncStatus.FAILED -> Triple(
            Icons.Default.Error,
            MaterialTheme.colorScheme.error,
            "Falha na sincronização"
        )
    }

    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = color,
        modifier = modifier.size(20.dp)
    )
}

@Composable
private fun SightingInfoChip(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun formatLocation(latitude: Double, longitude: Double): String {
    val latDir = if (latitude >= 0) "N" else "S"
    val lonDir = if (longitude >= 0) "E" else "W"
    return String.format(
        "%.4f°%s, %.4f°%s",
        kotlin.math.abs(latitude),
        latDir,
        kotlin.math.abs(longitude),
        lonDir
    )
}

@Composable
fun SightingMapPopup(
    sighting: Sighting,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryDot(category = sighting.category)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sighting.speciesName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = sighting.category.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = sighting.dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
