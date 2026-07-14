package br.com.fiap.biomonitor.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.fiap.biomonitor.domain.model.Badge
import br.com.fiap.biomonitor.domain.model.BadgeRequirement

object BadgeColors {
    val Bronze = Color(0xFFCD7F32)
    val Silver = Color(0xFFC0C0C0)
    val Gold = Color(0xFFFFD700)
    val Platinum = Color(0xFFE5E4E2)
    val Diamond = Color(0xFFB9F2FF)
}

fun getBadgeColor(badge: Badge): Color {
    return when (badge.requirement) {
        is BadgeRequirement.SightingCount -> {
            when {
                (badge.requirement as BadgeRequirement.SightingCount).count >= 500 -> BadgeColors.Diamond
                (badge.requirement as BadgeRequirement.SightingCount).count >= 100 -> BadgeColors.Platinum
                (badge.requirement as BadgeRequirement.SightingCount).count >= 50 -> BadgeColors.Gold
                (badge.requirement as BadgeRequirement.SightingCount).count >= 10 -> BadgeColors.Silver
                else -> BadgeColors.Bronze
            }
        }
        is BadgeRequirement.UniqueSpeciesCount -> {
            when {
                (badge.requirement as BadgeRequirement.UniqueSpeciesCount).count >= 50 -> BadgeColors.Gold
                (badge.requirement as BadgeRequirement.UniqueSpeciesCount).count >= 20 -> BadgeColors.Silver
                else -> BadgeColors.Bronze
            }
        }
        is BadgeRequirement.CitiesCount -> {
            when {
                (badge.requirement as BadgeRequirement.CitiesCount).count >= 10 -> BadgeColors.Gold
                (badge.requirement as BadgeRequirement.CitiesCount).count >= 5 -> BadgeColors.Silver
                else -> BadgeColors.Bronze
            }
        }
    }
}

@Composable
fun BadgeItem(
    badge: Badge,
    isEarned: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val badgeColor = getBadgeColor(badge)
    val alpha by animateFloatAsState(
        targetValue = if (isEarned) 1f else 0.4f,
        animationSpec = tween(300),
        label = "badge_alpha"
    )

    Card(
        modifier = modifier
            .width(100.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned) {
                badgeColor.copy(alpha = 0.15f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isEarned) {
                            Brush.radialGradient(
                                colors = listOf(
                                    badgeColor.copy(alpha = 0.3f),
                                    badgeColor.copy(alpha = 0.1f)
                                )
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            )
                        }
                    )
                    .border(
                        width = 2.dp,
                        color = if (isEarned) badgeColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isEarned) Icons.Default.EmojiEvents else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isEarned) badgeColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = badge.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isEarned) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isEarned) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BadgeCard(
    badge: Badge,
    isEarned: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val badgeColor = getBadgeColor(badge)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned) {
                badgeColor.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        if (isEarned) badgeColor.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        width = 2.dp,
                        color = if (isEarned) badgeColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isEarned) Icons.Default.EmojiEvents else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isEarned) badgeColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badge.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isEarned) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (isEarned) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Conquistado!",
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeGrid(
    badges: List<Pair<Badge, Boolean>>,
    modifier: Modifier = Modifier,
    onBadgeClick: ((Badge) -> Unit)? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(badges) { (badge, isEarned) ->
            BadgeItem(
                badge = badge,
                isEarned = isEarned,
                onClick = onBadgeClick?.let { { it(badge) } }
            )
        }
    }
}

@Composable
fun BadgeProgress(
    badge: Badge,
    currentProgress: Int,
    modifier: Modifier = Modifier
) {
    val badgeColor = getBadgeColor(badge)
    val targetValue = when (val req = badge.requirement) {
        is BadgeRequirement.SightingCount -> req.count
        is BadgeRequirement.UniqueSpeciesCount -> req.count
        is BadgeRequirement.CitiesCount -> req.count
    }
    val progress = (currentProgress.toFloat() / targetValue).coerceIn(0f, 1f)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = badge.name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$currentProgress / $targetValue",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            BioLinearProgress(
                progress = progress,
                color = badgeColor
            )
        }
    }
}
