package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AppLanguage
import com.example.ui.theme.*

@Composable
fun LanguageToggleChip(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
            .testTag("language_toggle_chip")
            .height(36.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "Language",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = if (currentLanguage == AppLanguage.MARATHI) "मराठी | EN" else "EN | मराठी",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status.uppercase()) {
        "PENDING", "प्रलंबित" -> Triple(
            StatusPending.copy(alpha = 0.15f),
            StatusPending,
            if (language == AppLanguage.MARATHI) "प्रलंबित" else "Pending"
        )
        "IN_PROGRESS", "प्रगतीपथावर", "VERIFYING" -> Triple(
            StatusProgress.copy(alpha = 0.15f),
            StatusProgress,
            if (language == AppLanguage.MARATHI) "प्रगतीपथावर" else "In Progress"
        )
        "RESOLVED", "APPROVED", "CONFIRMED", "निवारण झाले" -> Triple(
            StatusResolved.copy(alpha = 0.15f),
            StatusResolved,
            if (language == AppLanguage.MARATHI) "निवारण झाले" else "Resolved"
        )
        "REJECTED", "नाकारले" -> Triple(
            StatusRejected.copy(alpha = 0.15f),
            StatusRejected,
            if (language == AppLanguage.MARATHI) "नाकारले" else "Rejected"
        )
        "MAINTENANCE" -> Triple(
            StatusPending.copy(alpha = 0.15f),
            StatusPending,
            if (language == AppLanguage.MARATHI) "देखभाल चालू" else "Maintenance"
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            status
        )
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun StarRatingBar(
    rating: Int,
    onRatingSelected: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    starSize: Int = 28
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            IconButton(
                onClick = { onRatingSelected?.invoke(i) },
                enabled = onRatingSelected != null,
                modifier = Modifier
                    .size(starSize.dp)
                    .testTag("star_rating_$i")
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "$i Stars",
                    tint = if (isSelected) Color(0xFFFFB300) else Color.Gray,
                    modifier = Modifier.size(starSize.dp)
                )
            }
        }
    }
}

@Composable
fun getCategoryIcon(categoryKey: String): ImageVector {
    return when (categoryKey.lowercase()) {
        "water" -> Icons.Default.WaterDrop
        "road" -> Icons.Default.AddRoad
        "streetlight" -> Icons.Default.Lightbulb
        "garbage" -> Icons.Default.DeleteSweep
        "drainage" -> Icons.Default.Waves
        "sanitation" -> Icons.Default.CleaningServices
        "amenities" -> Icons.Default.Apartment
        else -> Icons.Default.HelpOutline
    }
}

@Composable
fun AppTopBar(
    title: String,
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    showBackButton: Boolean = false,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (showBackButton && onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            LanguageToggleChip(
                currentLanguage = language,
                onToggle = onToggleLanguage
            )

            actions()
        }
    }
}
