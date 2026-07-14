package br.com.fiap.biomonitor.presentation.screens.sighting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.fiap.biomonitor.domain.model.Category
import br.com.fiap.biomonitor.domain.model.Sighting
import br.com.fiap.biomonitor.presentation.components.BioSearchField
import br.com.fiap.biomonitor.presentation.components.LoadingIndicator
import br.com.fiap.biomonitor.presentation.components.SightingCard
import br.com.fiap.biomonitor.util.ErrorMessages

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MySightingsScreen(
    onNavigateToSightingDetails: (Long) -> Unit,
    onNavigateToNewSighting: () -> Unit,
    viewModel: MySightingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showFilters by remember { mutableStateOf(false) }


    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }


    LaunchedEffect(state.deleteSuccess) {
        if (state.deleteSuccess) {
            snackbarHostState.showSnackbar(ErrorMessages.SIGHTING_DELETED)
            viewModel.resetDeleteSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Registros") },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtros"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToNewSighting,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Novo avistamento"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            BioSearchField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder = "Buscar por nome da espécie...",
                leadingIcon = Icons.Default.Search,
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpar busca"
                            )
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )


            AnimatedVisibility(
                visible = showFilters,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CategoryFilterRow(
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = viewModel::setCategory,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }


            when {
                state.isLoading -> {
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                state.filteredSightings.isEmpty() -> {
                    EmptyState(
                        hasFilters = state.searchQuery.isNotEmpty() || state.selectedCategory != null,
                        onClearFilters = {
                            viewModel.onSearchQueryChange("")
                            viewModel.setCategory(null)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = viewModel::refresh,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SightingsList(
                            sightings = state.filteredSightings,
                            onSightingClick = { onNavigateToSightingDetails(it.id) },
                            onDeleteSighting = viewModel::showDeleteDialog
                        )
                    }
                }
            }
        }


        if (state.showDeleteDialog && state.sightingToDelete != null) {
            DeleteConfirmationDialog(
                sighting = state.sightingToDelete!!,
                onConfirm = viewModel::confirmDelete,
                onDismiss = viewModel::hideDeleteDialog
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryFilterRow(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Filtrar por categoria",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Todos") }
            )


            Category.entries.forEach { category ->
                val categoryColor = Color(category.color)

                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        onCategorySelected(if (selectedCategory == category) null else category)
                    },
                    label = { Text(category.displayName) },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(categoryColor)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = categoryColor.copy(alpha = 0.2f)
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SightingsList(
    sightings: List<Sighting>,
    onSightingClick: (Sighting) -> Unit,
    onDeleteSighting: (Sighting) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = sightings,
            key = { it.id }
        ) { sighting ->
            SwipeToDeleteSightingCard(
                sighting = sighting,
                onClick = { onSightingClick(sighting) },
                onDelete = { onDeleteSighting(sighting) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteSightingCard(
    sighting: Sighting,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                false
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        SightingCard(
            sighting = sighting,
            onClick = onClick
        )
    }
}

@Composable
private fun EmptyState(
    hasFilters: Boolean,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🔍",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (hasFilters)
                "Nenhum registro encontrado"
            else
                "Você ainda não tem registros",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (hasFilters)
                "Tente ajustar os filtros de busca"
            else
                "Comece registrando seu primeiro avistamento de biodiversidade!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (hasFilters) {
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onClearFilters) {
                Text("Limpar filtros")
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    sighting: Sighting,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Excluir avistamento?") },
        text = {
            Text(
                "Tem certeza que deseja excluir o registro de \"${sighting.speciesName}\"? " +
                "Esta ação não pode ser desfeita."
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
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
