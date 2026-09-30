package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.AppLanguage
import com.example.data.local.NoticeCategory
import com.example.data.local.NoticeEntity
import com.example.ui.components.AppTopBar
import com.example.ui.theme.*

@Composable
fun NoticeBoardScreen(
    notices: List<NoticeEntity>,
    language: AppLanguage,
    categoryFilter: NoticeCategory,
    onToggleLanguage: () -> Unit,
    onFilterChange: (NoticeCategory) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedNoticeForDetail by remember { mutableStateOf<NoticeEntity?>(null) }

    val filteredNotices = remember(notices, categoryFilter) {
        if (categoryFilter == NoticeCategory.ALL) {
            notices
        } else {
            notices.filter { it.category == categoryFilter.name }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (language == AppLanguage.MARATHI) "ग्रामपंचायत सूचना फलक" else "GP Notice Board",
                language = language,
                onToggleLanguage = onToggleLanguage
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Category Filter Bar
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(NoticeCategory.values()) { cat ->
                    FilterChip(
                        selected = categoryFilter == cat,
                        onClick = { onFilterChange(cat) },
                        label = {
                            Text(
                                text = if (language == AppLanguage.MARATHI) cat.titleMr else cat.titleEn,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("notice_category_${cat.name.lowercase()}")
                    )
                }
            }

            if (filteredNotices.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "या वर्गवारीत सध्या कोणतीही सूचना उपलब्ध नाही." else "No notices in this category.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredNotices, key = { it.id }) { notice ->
                        NoticeCard(
                            notice = notice,
                            language = language,
                            onClick = { selectedNoticeForDetail = notice },
                            onDownloadAttachment = {
                                onShowToast(
                                    if (language == AppLanguage.MARATHI)
                                        "${notice.attachmentTitle} डाउनलोड यशस्वी!"
                                    else
                                        "Downloaded ${notice.attachmentTitle}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (selectedNoticeForDetail != null) {
        NoticeDetailDialog(
            notice = selectedNoticeForDetail!!,
            language = language,
            onDismiss = { selectedNoticeForDetail = null },
            onDownload = {
                onShowToast(
                    if (language == AppLanguage.MARATHI)
                        "${selectedNoticeForDetail?.attachmentTitle} फाईल उघडत आहे..."
                    else
                        "Opening ${selectedNoticeForDetail?.attachmentTitle}..."
                )
            }
        )
    }
}

@Composable
fun NoticeCard(
    notice: NoticeEntity,
    language: AppLanguage,
    onClick: () -> Unit,
    onDownloadAttachment: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("notice_card_${notice.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (notice.isUrgent) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFD32F2F)
                        ) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "आपत्कालीन" else "URGENT",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = notice.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = notice.publishDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.MARATHI) notice.titleMr else notice.titleEn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (language == AppLanguage.MARATHI) notice.descriptionMr else notice.descriptionEn,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            if (notice.attachmentTitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onDownloadAttachment,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = notice.attachmentTitle,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun NoticeDetailDialog(
    notice: NoticeEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("notice_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = notice.publishDate,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (language == AppLanguage.MARATHI) notice.titleMr else notice.titleEn,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (language == AppLanguage.MARATHI) notice.descriptionMr else notice.descriptionEn,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )

                if (notice.attachmentTitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onDownload,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "परिपत्रक PDF डाउनलोड करा" else "Download Attachment PDF",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (language == AppLanguage.MARATHI) "बंद करा" else "Close")
                }
            }
        }
    }
}
