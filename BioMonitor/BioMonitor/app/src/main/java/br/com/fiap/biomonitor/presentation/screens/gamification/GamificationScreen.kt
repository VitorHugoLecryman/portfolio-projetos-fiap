package br.com.fiap.biomonitor.presentation.screens.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.fiap.biomonitor.domain.model.Badge
import br.com.fiap.biomonitor.domain.model.RankingEntry
import br.com.fiap.biomonitor.presentation.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamificationScreen(
    viewModel: GamificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ranking") },
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
        ) {

            TabRow(
                selectedTabIndex = state.selectedTab
            ) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    text = { Text("Rankings") }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    text = { Text("Conquistas") }
                )
            }

            when {
                state.isLoading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                state.selectedTab == 0 -> {
                    RankingsTab(
                        rankings = state.rankings,
                        currentUserId = state.currentUser?.id,
                        currentUserPosition = state.currentUserPosition,
                        selectedPeriod = state.selectedPeriod,
                        isLoading = state.isLoadingRankings,
                        onPeriodChange = viewModel::setRankingPeriod
                    )
                }

                else -> {
                    BadgesTab(
                        badges = state.allBadges,
                        earnedBadges = state.earnedBadges
                    )
                }
            }
        }
    }
}

@Composable
private fun RankingsTab(
    rankings: List<RankingEntry>,
    currentUserId: Long?,
    currentUserPosition: Int?,
    selectedPeriod: GamificationViewModel.RankingPeriod,
    isLoading: Boolean,
    onPeriodChange: (GamificationViewModel.RankingPeriod) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GamificationViewModel.RankingPeriod.entries.forEach { period ->
                FilterChip(
                    selected = selectedPeriod == period,
                    onClick = { onPeriodChange(period) },
                    label = { Text(getPeriodLabel(period)) }
                )
            }
        }


        currentUserPosition?.let { position ->
            CurrentUserPositionCard(
                position = position,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }


        if (isLoading) {
            LoadingIndicator(modifier = Modifier.fillMaxSize())
        } else if (rankings.isEmpty()) {
            EmptyRankingsState(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(rankings) { index, entry ->
                    RankingCard(
                        position = index + 1,
                        entry = entry,
                        isCurrentUser = entry.user.id == currentUserId
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentUserPositionCard(
    position: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Sua posição: ",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${position}º lugar",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun RankingCard(
    position: Int,
    entry: RankingEntry,
    isCurrentUser: Boolean
) {
    val backgroundColor = when {
        isCurrentUser -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        position == 1 -> Color(0xFFFFD700).copy(alpha = 0.2f)
        position == 2 -> Color(0xFFC0C0C0).copy(alpha = 0.2f)
        position == 3 -> Color(0xFFCD7F32).copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surface
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

            PositionBadge(position = position)

            Spacer(modifier = Modifier.width(16.dp))


            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = entry.user.name.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = entry.user.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${entry.points}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "pontos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PositionBadge(position: Int) {
    val (backgroundColor, textColor) = when (position) {
        1 -> Pair(Color(0xFFFFD700), Color.Black)
        2 -> Pair(Color(0xFFC0C0C0), Color.Black)
        3 -> Pair(Color(0xFFCD7F32), Color.White)
        else -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = position.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun BadgesTab(
    badges: List<Badge>,
    earnedBadges: Set<Long>
) {
    if (badges.isEmpty()) {
        EmptyBadgesState(modifier = Modifier.fillMaxSize())
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(badges) { badge ->
                BadgeCard(
                    badge = badge,
                    isEarned = badge.id in earnedBadges
                )
            }
        }
    }
}

@Composable
private fun BadgeCard(
    badge: Badge,
    isEarned: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        if (isEarned)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isEarned) Icons.Default.EmojiEvents else Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (isEarned)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = badge.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = if (isEarned)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(4.dp))


            Text(
                text = badge.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = if (isEarned)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )


            if (isEarned) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✓ Conquistada",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EmptyRankingsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🏆",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhum ranking disponível",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Comece a registrar avistamentos para aparecer no ranking!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyBadgesState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎖️",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhuma conquista disponível",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun getPeriodLabel(period: GamificationViewModel.RankingPeriod): String {
    return when (period) {
        GamificationViewModel.RankingPeriod.WEEKLY -> "Semanal"
        GamificationViewModel.RankingPeriod.MONTHLY -> "Mensal"
        GamificationViewModel.RankingPeriod.ALL_TIME -> "Geral"
    }
}
