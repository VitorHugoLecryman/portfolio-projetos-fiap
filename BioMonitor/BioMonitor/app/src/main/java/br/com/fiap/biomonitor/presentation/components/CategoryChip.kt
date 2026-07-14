package br.com.fiap.biomonitor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Water
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.fiap.biomonitor.domain.model.Category

fun getCategoryIcon(category: Category): ImageVector {
    return when (category) {
        Category.FLORA -> Icons.Outlined.Eco
        Category.AVES -> Icons.Outlined.Pets
        Category.MAMIFEROS -> Icons.Default.Pets
        Category.REPTEIS_ANFIBIOS -> Icons.Outlined.Pets
        Category.INSETOS -> Icons.Outlined.BugReport
        Category.PEIXES -> Icons.Outlined.Water
        Category.FUNGOS -> Icons.Default.Grass
        Category.OUTROS -> Icons.Default.FilterList
    }
}

fun getCategoryColor(category: Category): Color {
    return Color(category.color)
}

@Composable
fun CategoryChip(
    category: Category,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val categoryColor = getCategoryColor(category)

    FilterChip(
        selected = selected,
        onClick = { onClick?.invoke() },
        label = { Text(category.displayName) },
        modifier = modifier,
        enabled = onClick != null,
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(categoryColor)
            )
        },
        trailingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                    modifier = Modifier.size(18.dp)
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = categoryColor.copy(alpha = 0.2f),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface,
            selectedLeadingIconColor = categoryColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = categoryColor.copy(alpha = 0.5f),
            selectedBorderColor = categoryColor
        )
    )
}

@Composable
fun CategoryChipWithIcon(
    category: Category,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val categoryColor = getCategoryColor(category)
    val icon = getCategoryIcon(category)

    FilterChip(
        selected = selected,
        onClick = { onClick?.invoke() },
        label = { Text(category.displayName) },
        modifier = modifier,
        enabled = onClick != null,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) categoryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = categoryColor.copy(alpha = 0.2f),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderColor = categoryColor
        )
    )
}

@Composable
fun CategoryBadge(
    category: Category,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(category)

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(categoryColor.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(categoryColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CategoryDot(
    category: Category,
    modifier: Modifier = Modifier,
    size: Int = 12
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(getCategoryColor(category))
    )
}

@Composable
fun CategoryFilterRow(
    categories: List<Category> = Category.entries,
    selectedCategories: Set<Category>,
    onCategoryToggle: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                selected = category in selectedCategories,
                onClick = { onCategoryToggle(category) }
            )
        }
    }
}

@Composable
fun CategorySelector(
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
    categories: List<Category> = Category.entries
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChipWithIcon(
                category = category,
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategorySuggestionChip(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(category)

    SuggestionChip(
        onClick = onClick,
        label = { Text(category.displayName) },
        modifier = modifier,
        icon = {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(categoryColor)
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = SuggestionChipDefaults.suggestionChipBorder(
            enabled = true,
            borderColor = categoryColor.copy(alpha = 0.5f)
        )
    )
}
