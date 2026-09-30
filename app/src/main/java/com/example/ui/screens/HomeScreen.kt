package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.*
import com.example.ui.components.LanguageToggleChip
import com.example.ui.components.StatusBadge
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenDestination

data class HomeModuleItem(
    val titleMr: String,
    val titleEn: String,
    val subtitleMr: String,
    val subtitleEn: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val destination: ScreenDestination,
    val badge: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    profile: UserProfile?,
    panchayatProfile: PanchayatProfile? = null,
    language: AppLanguage,
    waterSchedules: List<WaterScheduleEntity>,
    complaints: List<ComplaintEntity>,
    notices: List<NoticeEntity>,
    onToggleLanguage: () -> Unit,
    onNavigate: (ScreenDestination) -> Unit,
    onOpenNewComplaint: () -> Unit,
    onOpenComplaintDetail: (ComplaintEntity) -> Unit
) {
    val gpName = if (language == AppLanguage.MARATHI) {
        panchayatProfile?.nameMr?.ifBlank { profile?.grampanchayatNameMr } ?: (profile?.grampanchayatNameMr ?: "आदर्श ग्रामपंचायत पळसखेड दौलत")
    } else {
        panchayatProfile?.nameEn?.ifBlank { profile?.grampanchayatNameEn } ?: (profile?.grampanchayatNameEn ?: "Model Grampanchayat Palaskhed Daulat")
    }
    val taluka = if (language == AppLanguage.MARATHI) {
        panchayatProfile?.talukaMr?.ifBlank { profile?.talukaMr } ?: (profile?.talukaMr ?: "चिखली")
    } else {
        panchayatProfile?.talukaEn?.ifBlank { profile?.talukaEn } ?: (profile?.talukaEn ?: "Chikhli")
    }
    val district = if (language == AppLanguage.MARATHI) {
        panchayatProfile?.districtMr?.ifBlank { profile?.districtMr } ?: (profile?.districtMr ?: "बुलढाणा")
    } else {
        panchayatProfile?.districtEn?.ifBlank { profile?.districtEn } ?: (profile?.districtEn ?: "Buldhana")
    }

    val citizenWard = profile?.wardNumber ?: 3
    val myWardSchedule = waterSchedules.find { it.wardNumber == citizenWard }
    val urgentNotice = notices.find { it.isUrgent }

    val homeModules = listOf(
        HomeModuleItem(
            titleMr = "ग्रामपंचायत माहिती",
            titleEn = "GP Info & Directory",
            subtitleMr = "सरपंच, ग्रामसेवक, विकासकामे",
            subtitleEn = "Sarpanch, Staff, Projects",
            icon = Icons.Default.AccountBalance,
            iconBgColor = Color(0xFF0F3057),
            destination = ScreenDestination.GP_INFO
        ),
        HomeModuleItem(
            titleMr = "तक्रार पेटी",
            titleEn = "Complaint Box",
            subtitleMr = "नवीन तक्रार, स्थिती ट्रॅकिंग",
            subtitleEn = "File grievance & track",
            icon = Icons.Default.ReportProblem,
            iconBgColor = Color(0xFFD9531E),
            destination = ScreenDestination.COMPLAINTS,
            badge = "${complaints.count { it.status == "PENDING" || it.status == "IN_PROGRESS" }} active"
        ),
        HomeModuleItem(
            titleMr = "AI सहाय्यक",
            titleEn = "AI Assistant",
            subtitleMr = "ग्राममित्र २४x७ AI मदत",
            subtitleEn = "24x7 GramMitra AI help",
            icon = Icons.Default.Psychology,
            iconBgColor = Color(0xFF7B1FA2),
            destination = ScreenDestination.AI_ASSISTANT,
            badge = "Gemini AI"
        ),
        HomeModuleItem(
            titleMr = "सूचना फलक",
            titleEn = "Notice Board",
            subtitleMr = "ग्रामसभा, कर व परिपत्रके",
            subtitleEn = "Gramsabha & tenders",
            icon = Icons.Default.Campaign,
            iconBgColor = Color(0xFFE65100),
            destination = ScreenDestination.NOTICES,
            badge = "${notices.size} नवीन"
        ),
        HomeModuleItem(
            titleMr = "पाणीपुरवठा वेळापत्रक",
            titleEn = "Water Timetable",
            subtitleMr = "वॉर्डनुसार वेळ व टँकर बुकिंग",
            subtitleEn = "Schedules & Tanker Booking",
            icon = Icons.Default.WaterDrop,
            iconBgColor = Color(0xFF0288D1),
            destination = ScreenDestination.WATER_SERVICES
        ),
        HomeModuleItem(
            titleMr = "ऑनलाइन सेवा व कर",
            titleEn = "Online Services & Tax",
            subtitleMr = "दाखले, घरपट्टी-पाणीपट्टी भरणा",
            subtitleEn = "Certificates, Tax payments",
            icon = Icons.Default.ReceiptLong,
            iconBgColor = Color(0xFF2E7D32),
            destination = ScreenDestination.ONLINE_SERVICES,
            badge = "१०% सूट"
        ),
        HomeModuleItem(
            titleMr = "सूचना आणि अलर्ट",
            titleEn = "Notifications",
            subtitleMr = "तातडीचे संदेश व आठवणी",
            subtitleEn = "Instant emergency alerts",
            icon = Icons.Default.NotificationsActive,
            iconBgColor = Color(0xFFC2185B),
            destination = ScreenDestination.NOTIFICATIONS
        ),
        HomeModuleItem(
            titleMr = "माझी प्रोफाइल",
            titleEn = "Citizen Profile",
            subtitleMr = "माझे अर्ज, प्रभाग व भाषा",
            subtitleEn = "Requests, Ward & Settings",
            icon = Icons.Default.Person,
            iconBgColor = Color(0xFF455A64),
            destination = ScreenDestination.PROFILE
        )
    )

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gp_logo),
                            contentDescription = "Grampanchayat Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            Text(
                                text = gpName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (language == AppLanguage.MARATHI)
                                    "ता. $taluka, जि. $district"
                                else
                                    "Tal. $taluka, Dist. $district",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    LanguageToggleChip(
                        currentLanguage = language,
                        onToggle = onToggleLanguage
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOpenNewComplaint,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "तक्रार नोंदवा" else "New Grievance",
                        fontWeight = FontWeight.Bold
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("home_fab_new_complaint")
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding() + 12.dp,
                bottom = paddingValues.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("home_screen_lazy_column")
        ) {
            // 1. Citizen Welcome Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = GramNavy
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "स्वागत आहे," else "Welcome,",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = if (language == AppLanguage.MARATHI) (profile?.fullName ?: "नागरिक") else (profile?.fullNameEn ?: "Citizen"),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = GramSaffronLight
                            ) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "प्रभाग क्र. $citizenWard" else "Ward $citizenWard",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFFFFCC80),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (language == AppLanguage.MARATHI) (profile?.address ?: "") else (profile?.addressEn ?: ""),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // 2. Urgent Notice / Alert Banner
            if (urgentNotice != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF3E0)
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(Color(0xFFFF9800), Color(0xFFE65100)))
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigate(ScreenDestination.NOTICES) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFE65100),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Campaign,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFD32F2F)
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.MARATHI) "महत्त्वाची सूचना" else "URGENT",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = urgentNotice.publishDate,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF5D4037)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == AppLanguage.MARATHI) urgentNotice.titleMr else urgentNotice.titleEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E1C0C),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFFE65100)
                            )
                        }
                    }
                }
            }

            // 3. Ward Water Supply Status Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE1F5FE)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(ScreenDestination.WATER_SERVICES) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.MARATHI)
                                        "आजचा पाणीपुरवठा (प्रभाग $citizenWard)"
                                    else
                                        "Today's Water Status (Ward $citizenWard)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF01579B)
                                )
                            }
                            StatusBadge(
                                status = myWardSchedule?.status ?: "NORMAL",
                                language = language
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "सकाळची वेळ" else "Morning",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF546E7A)
                                )
                                Text(
                                    text = myWardSchedule?.morningTiming ?: "६:०० ते ७:३०",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0F3057)
                                )
                            }
                            Column {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "संध्याकाळची वेळ" else "Evening",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF546E7A)
                                )
                                Text(
                                    text = myWardSchedule?.eveningTiming ?: "५:०० ते ६:३०",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0F3057)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "स्थिती" else "Status",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF546E7A)
                                )
                                Text(
                                    text = if (language == AppLanguage.MARATHI) (myWardSchedule?.statusNoteMr ?: "सुरळीत") else (myWardSchedule?.statusNoteEn ?: "Normal"),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Section Title: All 8 Services & Features
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "ग्रामपंचायत मुख्य सेवा (८ मॉड्यूल्स)" else "Grampanchayat Main Services (8 Modules)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // 5. Grid of the 8 Main Modules (in rows of 2)
            items(homeModules.chunked(2)) { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (item in rowItems) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigate(item.destination) }
                                .testTag("home_module_${item.destination.name.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = item.iconBgColor,
                                        modifier = Modifier.size(42.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }

                                    if (item.badge != null) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = item.badge,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = if (language == AppLanguage.MARATHI) item.titleMr else item.titleEn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = if (language == AppLanguage.MARATHI) item.subtitleMr else item.subtitleEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // 6. Recent Grievances Preview
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "माझ्या तक्रारींची स्थिती" else "My Grievances Tracker",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { onNavigate(ScreenDestination.COMPLAINTS) }) {
                        Text(if (language == AppLanguage.MARATHI) "सर्व पहा" else "View All")
                    }
                }
            }

            if (complaints.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (language == AppLanguage.MARATHI) "कोणतीही तक्रार प्रलंबित नाही." else "No pending grievances.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(complaints.take(3)) { comp ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenComplaintDetail(comp) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = getCategoryIcon(comp.category),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = comp.id,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    StatusBadge(status = comp.status, language = language)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = comp.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = comp.locationDetail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
