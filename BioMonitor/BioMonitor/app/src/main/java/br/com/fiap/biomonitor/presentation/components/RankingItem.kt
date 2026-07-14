package br.com.fiap.biomonitor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.fiap.biomonitor.domain.model.RankingEntry
import br.com.fiap.biomonitor.domain.model.User
import coil.compose.AsyncImage
import coil.request.ImageRequest

object RankingColors {
    val First = Color(0xFFFFD700)
    val Second = Color(0xFFC0C0C0)
    val Third = Color(0xFFCD7F32)
}

fun getPositionColor(position: Int): Color? {
    return when (position) {
        1 -> RankingColors.First
        2 -> RankingColors.Second
        3 -> RankingColors.Third
        else -> null
    }
}

@Composable
fun RankingItem(
    entry: RankingEntry,
    modifier: Modifier = Modifier,
    isCurrentUser: Boolean = false
) {
    val positionColor = getPositionColor(entry.position)
    val backgroundColor = when {
        isCurrentUser -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        positionColor != null -> positionColor.copy(alpha = 0.1f)
        else -> Color.Transparent
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PositionBadge(
                position = entry.position,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))


            UserAvatar(
                photoPath = entry.user.profilePhotoPath,
                name = entry.user.name,
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))


            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.user.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(você)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = entry.user.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatPoints(entry.points),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = positionColor ?: MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "pontos",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PositionBadge(
    position: Int,
    modifier: Modifier = Modifier
) {
    val positionColor = getPositionColor(position)

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                if (positionColor != null) {
                    Brush.radialGradient(
                        colors = listOf(
                            positionColor,
                            positionColor.copy(alpha = 0.7f)
                        )
                    )
                } else {
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (position <= 3) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Posição $position",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = position.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun UserAvatar(
    photoPath: String?,
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (!photoPath.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoPath)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto de $name",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        } else {
            Text(
                text = name.take(2).uppercase(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun RankingPodium(
    entries: List<RankingEntry>,
    modifier: Modifier = Modifier,
    currentUserId: Long? = null
) {
    if (entries.size < 3) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {

        PodiumItem(
            entry = entries[1],
            height = 100,
            isCurrentUser = entries[1].user.id == currentUserId
        )


        PodiumItem(
            entry = entries[0],
            height = 130,
            isCurrentUser = entries[0].user.id == currentUserId
        )


        PodiumItem(
            entry = entries[2],
            height = 80,
            isCurrentUser = entries[2].user.id == currentUserId
        )
    }
}

@Composable
private fun PodiumItem(
    entry: RankingEntry,
    height: Int,
    isCurrentUser: Boolean
) {
    val positionColor = getPositionColor(entry.position) ?: MaterialTheme.colorScheme.primary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = positionColor,
                    shape = CircleShape
                )
        ) {
            UserAvatar(
                photoPath = entry.user.profilePhotoPath,
                name = entry.user.name,
                modifier = Modifier.matchParentSize()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))


        Text(
            text = entry.user.name.split(" ").first(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isCurrentUser) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )


        Text(
            text = formatPoints(entry.points),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))


        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height.dp)
                .clip(MaterialTheme.shapes.small)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            positionColor,
                            positionColor.copy(alpha = 0.7f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = entry.position.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun RankingList(
    entries: List<RankingEntry>,
    modifier: Modifier = Modifier,
    currentUserId: Long? = null
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        if (entries.size >= 3) {
            item {
                RankingPodium(
                    entries = entries.take(3),
                    currentUserId = currentUserId,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }


        itemsIndexed(entries.drop(3)) { index, entry ->
            RankingItem(
                entry = entry,
                isCurrentUser = entry.user.id == currentUserId
            )
        }
    }
}

private fun formatPoints(points: Int): String {
    return String.format("%,d", points)
}

@Composable
fun CurrentUserRankingCard(
    entry: RankingEntry,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PositionBadge(
                position = entry.position,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sua posição",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = "#${entry.position} no ranking",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatPoints(entry.points),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "pontos",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}
