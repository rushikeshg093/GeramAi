package com.example.ui.screens.admin

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.*
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

enum class AdminModule(
    val titleMr: String,
    val titleEn: String,
    val icon: ImageVector,
    val color: Color
) {
    AI_CALL_SYSTEM("🤖 AI कॉल सिस्टम", "AI Call System", Icons.Default.PhoneCallback, Color(0xFF00838F)),
    PANCHAYAT_PROFILE("ग्रामपंचायत माहिती", "Panchayat Profile", Icons.Default.AccountBalance, Color(0xFF1E88E5)),
    CITIZENS("नागरिक यादी", "Citizen Records", Icons.Default.People, Color(0xFF43A047)),
    WARDS("प्रभाग व्यवस्थापन", "Ward Management", Icons.Default.HolidayVillage, Color(0xFF8E24AA)),
    WATER_SUPPLY("पाणीपुरवठा", "Water Supply", Icons.Default.WaterDrop, Color(0xFF0288D1)),
    NOTICES("सूचना फलक", "Notice Board", Icons.Default.Campaign, Color(0xFFE65100)),
    GRIEVANCES("तक्रार निवारण", "Grievance Redressal", Icons.Default.ReportProblem, Color(0xFFD32F2F)),
    GP_DIRECTORY("कार्यालय व संपर्क", "GP Directory", Icons.Default.ContactPhone, Color(0xFF00897B)),
    STAFF("अधिकारी व कर्मचारी", "Staff & Officials", Icons.Default.Badge, Color(0xFF5E35B1)),
    PROJECTS("विकास प्रकल्प", "Projects & Works", Icons.Default.Engineering, Color(0xFFF57C00)),
    ONLINE_SERVICES("ऑनलाइन सेवा सूची", "Online Services", Icons.Default.Description, Color(0xFF3949AB)),
    NOTIFICATIONS("अलर्ट व मेसेज", "Broadcast Alerts", Icons.Default.NotificationsActive, Color(0xFFC2185B)),
    SETTINGS_SYNC("डेटा सिंक व सेटिंग्ज", "Sync & Settings", Icons.Default.Sync, Color(0xFF455A64))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    language: AppLanguage,
    adminUser: AdminUser?,
    panchayatProfile: PanchayatProfile,
    wards: List<WardEntity>,
    waterSchedules: List<WaterScheduleEntity>,
    notices: List<NoticeEntity>,
    complaints: List<ComplaintEntity>,
    officials: List<OfficialContactEntity>,
    projects: List<DevelopmentProjectEntity>,
    services: List<OnlineServiceItem>,
    notifications: List<NotificationItem>,
    citizens: List<UserProfile>,
    aiCampaigns: List<AiCallCampaign> = emptyList(),
    aiCallLogs: List<AiCallLog> = emptyList(),
    aiScheduledCalls: List<AiScheduledCall> = emptyList(),
    aiCallSettings: AiCallSettings = AiCallSettings(),
    isBatchCallingActive: Boolean = false,
    batchProgress: Pair<Int, Int> = Pair(0, 0),
    currentCallingCitizen: String = "",
    isVoicePlaying: Boolean = false,
    isLoading: Boolean,
    onToggleLanguage: () -> Unit,
    onLogout: () -> Unit,
    onSavePanchayatProfile: (PanchayatProfile) -> Unit,
    onSaveWard: (WardEntity) -> Unit,
    onDeleteWard: (String) -> Unit,
    onSaveWaterSchedule: (WaterScheduleEntity) -> Unit,
    onDeleteWaterSchedule: (Int) -> Unit,
    onSaveNotice: (NoticeEntity) -> Unit,
    onDeleteNotice: (String) -> Unit,
    onUpdateComplaint: (id: String, status: String, remarks: String, officer: String) -> Unit,
    onDeleteComplaint: (String) -> Unit,
    onSaveOfficial: (OfficialContactEntity) -> Unit,
    onDeleteOfficial: (String) -> Unit,
    onSaveProject: (DevelopmentProjectEntity) -> Unit,
    onDeleteProject: (String) -> Unit,
    onSaveService: (OnlineServiceItem) -> Unit,
    onDeleteService: (String) -> Unit,
    onSaveNotification: (NotificationItem) -> Unit,
    onDeleteNotification: (String) -> Unit,
    onSaveCitizen: (UserProfile) -> Unit,
    onSeedFirestore: () -> Unit,
    onPreviewVoice: (text: String, lang: String, voiceId: String) -> Unit = { _, _, _ -> },
    onStopPreviewVoice: () -> Unit = {},
    onStartCampaign: (title: String, msg: String, lang: String, voiceId: String, target: String) -> Unit = { _, _, _, _, _ -> },
    onScheduleCall: (title: String, msg: String, lang: String, voiceId: String, target: String, date: String, time: String, ts: Long) -> Unit = { _, _, _, _, _, _, _, _ -> },
    onCancelScheduledCall: (String) -> Unit = {},
    onSaveAiSettings: (AiCallSettings) -> Unit = {}
) {
    var selectedModule by remember { mutableStateOf<AdminModule?>(null) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "प्रशासकीय डॅशबोर्ड" else "Admin Dashboard",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = if (language == AppLanguage.MARATHI) panchayatProfile.nameMr else panchayatProfile.nameEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    if (selectedModule != null) {
                        IconButton(onClick = { selectedModule = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to modules")
                        }
                    } else {
                        IconButton(onClick = { showLogoutConfirm = true }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                        }
                    }
                },
                actions = {
                    TextButton(onClick = onToggleLanguage) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "English" else "मराठी",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            if (selectedModule == null) {
                // Main Admin Grid
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header card
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.2f),
                                    modifier = Modifier.size(54.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.AdminPanelSettings,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = adminUser?.name ?: "प्रशासक अधिकारी (Admin)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${adminUser?.designation ?: "ग्रामविकास अधिकारी"} • ${adminUser?.email ?: ""}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📍 कार्यक्षेत्र: ${adminUser?.gramPanchayatNameMr ?: panchayatProfile.nameMr} (ता. ${adminUser?.talukaId?.replaceFirstChar { it.uppercase() } ?: panchayatProfile.talukaMr}, जि. ${adminUser?.districtId?.replaceFirstChar { it.uppercase() } ?: panchayatProfile.districtMr})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color.White.copy(alpha = 0.25f)
                                        ) {
                                            Text(
                                                text = if (language == AppLanguage.MARATHI) "सक्रिय अधिकारी (Active Officer)" else "Active Officer",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        if (!adminUser?.adminId.isNullOrBlank()) {
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color.White.copy(alpha = 0.25f)
                                            ) {
                                                Text(
                                                    text = "ID: ${adminUser?.adminId}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Quick Stats Bar
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminStatChip(
                                title = if (language == AppLanguage.MARATHI) "तक्रारी" else "Complaints",
                                count = "${complaints.size}",
                                icon = Icons.Default.ReportProblem,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatChip(
                                title = if (language == AppLanguage.MARATHI) "प्रभाग" else "Wards",
                                count = "${wards.size}",
                                icon = Icons.Default.HolidayVillage,
                                color = Color(0xFF8E24AA),
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatChip(
                                title = if (language == AppLanguage.MARATHI) "सूचना" else "Notices",
                                count = "${notices.size}",
                                icon = Icons.Default.Campaign,
                                color = Color(0xFFE65100),
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatChip(
                                title = if (language == AppLanguage.MARATHI) "प्रकल्प" else "Projects",
                                count = "${projects.size}",
                                icon = Icons.Default.Engineering,
                                color = Color(0xFF00897B),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Section Title
                    item {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "प्रशासकीय विभाग (१३ मॉड्यूल्स)" else "Admin Modules (13 Modules)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Modules Grid
                    item {
                        val allModules = AdminModule.values()
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            for (chunk in allModules.toList().chunked(2)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    for (module in chunk) {
                                        AdminModuleCard(
                                            module = module,
                                            language = language,
                                            onClick = { selectedModule = module },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (chunk.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                // Specific Module View
                when (selectedModule!!) {
                    AdminModule.AI_CALL_SYSTEM -> {
                        AICallSystemScreen(
                            language = language,
                            panchayatProfile = panchayatProfile,
                            aiCampaigns = aiCampaigns,
                            aiCallLogs = aiCallLogs,
                            aiScheduledCalls = aiScheduledCalls,
                            aiCallSettings = aiCallSettings,
                            citizens = citizens,
                            isBatchCallingActive = isBatchCallingActive,
                            batchProgress = batchProgress,
                            currentCallingCitizen = currentCallingCitizen,
                            isVoicePlaying = isVoicePlaying,
                            onPreviewVoice = onPreviewVoice,
                            onStopPreviewVoice = onStopPreviewVoice,
                            onStartCampaign = onStartCampaign,
                            onScheduleCall = onScheduleCall,
                            onCancelScheduledCall = onCancelScheduledCall,
                            onSaveSettings = onSaveAiSettings,
                            onBack = { selectedModule = null }
                        )
                    }
                    AdminModule.PANCHAYAT_PROFILE -> {
                        AdminPanchayatProfileView(
                            profile = panchayatProfile,
                            language = language,
                            onSave = onSavePanchayatProfile
                        )
                    }
                    AdminModule.CITIZENS -> {
                        AdminCitizensView(
                            citizens = citizens,
                            wards = wards,
                            language = language,
                            onSaveCitizen = onSaveCitizen
                        )
                    }
                    AdminModule.WARDS -> {
                        AdminWardsView(
                            wards = wards,
                            language = language,
                            onSaveWard = onSaveWard,
                            onDeleteWard = onDeleteWard
                        )
                    }
                    AdminModule.WATER_SUPPLY -> {
                        AdminWaterSupplyView(
                            schedules = waterSchedules,
                            wards = wards,
                            language = language,
                            onSave = onSaveWaterSchedule,
                            onDelete = onDeleteWaterSchedule
                        )
                    }
                    AdminModule.NOTICES -> {
                        AdminNoticesView(
                            notices = notices,
                            language = language,
                            onSave = onSaveNotice,
                            onDelete = onDeleteNotice
                        )
                    }
                    AdminModule.GRIEVANCES -> {
                        AdminGrievancesView(
                            complaints = complaints,
                            language = language,
                            onUpdate = onUpdateComplaint,
                            onDelete = onDeleteComplaint
                        )
                    }
                    AdminModule.GP_DIRECTORY -> {
                        AdminGPDirectoryView(
                            panchayatProfile = panchayatProfile,
                            officials = officials,
                            language = language
                        )
                    }
                    AdminModule.STAFF -> {
                        AdminStaffView(
                            officials = officials,
                            language = language,
                            onSave = onSaveOfficial,
                            onDelete = onDeleteOfficial
                        )
                    }
                    AdminModule.PROJECTS -> {
                        AdminProjectsView(
                            projects = projects,
                            language = language,
                            onSave = onSaveProject,
                            onDelete = onDeleteProject
                        )
                    }
                    AdminModule.ONLINE_SERVICES -> {
                        AdminServicesView(
                            services = services,
                            language = language,
                            onSave = onSaveService,
                            onDelete = onDeleteService
                        )
                    }
                    AdminModule.NOTIFICATIONS -> {
                        AdminNotificationsView(
                            notifications = notifications,
                            language = language,
                            onSave = onSaveNotification,
                            onDelete = onDeleteNotification
                        )
                    }
                    AdminModule.SETTINGS_SYNC -> {
                        AdminSettingsSyncView(
                            language = language,
                            isLoading = isLoading,
                            onSeedFirestore = onSeedFirestore,
                            onLogout = onLogout
                        )
                    }
                }
            }
        }
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = {
                Text(if (language == AppLanguage.MARATHI) "प्रशासन कक्ष सोडायचे?" else "Sign Out Admin?")
            },
            text = {
                Text(
                    if (language == AppLanguage.MARATHI)
                        "आपण खात्रीपूर्वक प्रशासक डॅशबोर्डमधून बाहेर पडू इच्छिता का?"
                    else
                        "Are you sure you want to sign out from the administrative console?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirm = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (language == AppLanguage.MARATHI) "लॉग आउट करा" else "Sign Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                }
            }
        )
    }
}

@Composable
private fun AdminStatChip(
    title: String,
    count: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(count, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AdminModuleCard(
    module: AdminModule,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = module.color.copy(alpha = 0.15f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = null,
                        tint = module.color,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == AppLanguage.MARATHI) module.titleMr else module.titleEn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (language == AppLanguage.MARATHI) module.titleEn else module.titleMr,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ================= MODULE 1: PANCHAYAT PROFILE =================
@Composable
fun AdminPanchayatProfileView(
    profile: PanchayatProfile,
    language: AppLanguage,
    onSave: (PanchayatProfile) -> Unit
) {
    var nameMr by remember { mutableStateOf(profile.nameMr) }
    var nameEn by remember { mutableStateOf(profile.nameEn) }
    var talukaMr by remember { mutableStateOf(profile.talukaMr) }
    var talukaEn by remember { mutableStateOf(profile.talukaEn) }
    var districtMr by remember { mutableStateOf(profile.districtMr) }
    var districtEn by remember { mutableStateOf(profile.districtEn) }
    var pinCode by remember { mutableStateOf(profile.pinCode) }
    var addressMr by remember { mutableStateOf(profile.addressMr) }
    var addressEn by remember { mutableStateOf(profile.addressEn) }
    var phone by remember { mutableStateOf(profile.phone) }
    var email by remember { mutableStateOf(profile.email) }
    var website by remember { mutableStateOf(profile.website) }
    var officeHoursMr by remember { mutableStateOf(profile.officeHoursMr) }
    var officeHoursEn by remember { mutableStateOf(profile.officeHoursEn) }
    var totalPop by remember { mutableStateOf(profile.totalPopulation) }
    var totalHouses by remember { mutableStateOf(profile.totalHouseholds) }
    var totalWards by remember { mutableIntStateOf(profile.totalWards) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (language == AppLanguage.MARATHI) "🏛️ ग्रामपंचायत अधिकृत माहिती संपादन" else "🏛️ Edit Panchayat Profile",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nameMr,
                    onValueChange = { nameMr = it },
                    label = { Text("ग्रामपंचायतीचे नाव (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("Panchayat Name (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = talukaMr,
                        onValueChange = { talukaMr = it },
                        label = { Text("तालुका (मराठी)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = districtMr,
                        onValueChange = { districtMr = it },
                        label = { Text("जिल्हा (मराठी)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = talukaEn,
                        onValueChange = { talukaEn = it },
                        label = { Text("Taluka (English)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = districtEn,
                        onValueChange = { districtEn = it },
                        label = { Text("District (English)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = addressMr,
                    onValueChange = { addressMr = it },
                    label = { Text("कार्यालय पत्ता (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = addressEn,
                    onValueChange = { addressEn = it },
                    label = { Text("Office Address (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("फोन क्र. (Phone)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = { pinCode = it },
                        label = { Text("पिनकोड (Pin)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("अधिकृत ईमेल (Email)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    label = { Text("वेबसाइट (Website)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = officeHoursMr,
                    onValueChange = { officeHoursMr = it },
                    label = { Text("कार्यालयीन वेळ (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = totalPop,
                        onValueChange = { totalPop = it },
                        label = { Text("एकूण लोकसंख्या") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = totalHouses,
                        onValueChange = { totalHouses = it },
                        label = { Text("एकूण कुटुंबे") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                onSave(
                    profile.copy(
                        nameMr = nameMr,
                        nameEn = nameEn,
                        talukaMr = talukaMr,
                        talukaEn = talukaEn,
                        districtMr = districtMr,
                        districtEn = districtEn,
                        pinCode = pinCode,
                        addressMr = addressMr,
                        addressEn = addressEn,
                        phone = phone,
                        email = email,
                        website = website,
                        officeHoursMr = officeHoursMr,
                        officeHoursEn = officeHoursEn,
                        totalPopulation = totalPop,
                        totalHouseholds = totalHouses,
                        totalWards = totalWards
                    )
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.MARATHI) "माहिती Firestore मध्ये सेव्ह करा" else "Save to Firestore",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ================= MODULE 2: CITIZEN INFORMATION =================
@Composable
fun AdminCitizensView(
    citizens: List<UserProfile>,
    wards: List<WardEntity>,
    language: AppLanguage,
    onSaveCitizen: (UserProfile) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var editingCitizen by remember { mutableStateOf<UserProfile?>(null) }

    val filteredCitizens = remember(citizens, searchQuery) {
        if (searchQuery.isBlank()) citizens
        else citizens.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                    it.fullNameEn.contains(searchQuery, ignoreCase = true) ||
                    it.mobileNumber.contains(searchQuery) ||
                    "वॉर्ड ${it.wardNumber}".contains(searchQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "👥 नागरिक माहिती यादी (${filteredCitizens.size})" else "👥 Citizen Records (${filteredCitizens.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    editingCitizen = UserProfile(
                        id = "citizen_${System.currentTimeMillis()}",
                        fullName = "",
                        fullNameEn = "",
                        mobileNumber = "",
                        wardNumber = 1,
                        address = "",
                        addressEn = ""
                    )
                },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (language == AppLanguage.MARATHI) "+ नवीन जोडा" else "+ Add Citizen")
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (language == AppLanguage.MARATHI) "नाव किंवा मोबाईल शोधा..." else "Search name or mobile...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredCitizens) { citizen ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) citizen.fullName.ifBlank { citizen.fullNameEn } else citizen.fullNameEn.ifBlank { citizen.fullName },
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "📞 +91 ${citizen.mobileNumber} • प्रभाग क्र. ${citizen.wardNumber}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "पत्ता: ${citizen.address}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                maxLines = 1
                            )
                        }

                        IconButton(onClick = { editingCitizen = citizen }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }

    if (editingCitizen != null) {
        AdminCitizenEditDialog(
            citizen = editingCitizen!!,
            wards = wards,
            language = language,
            onDismiss = { editingCitizen = null },
            onSave = { updated ->
                onSaveCitizen(updated)
                editingCitizen = null
            }
        )
    }
}

@Composable
fun AdminCitizenEditDialog(
    citizen: UserProfile,
    wards: List<WardEntity>,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var nameMr by remember { mutableStateOf(citizen.fullName) }
    var nameEn by remember { mutableStateOf(citizen.fullNameEn) }
    var mobile by remember { mutableStateOf(citizen.mobileNumber) }
    var wardNum by remember { mutableIntStateOf(citizen.wardNumber) }
    var addressMr by remember { mutableStateOf(citizen.address) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "नागरिक माहिती संपादन" else "Edit Citizen Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = nameMr,
                    onValueChange = { nameMr = it },
                    label = { Text("पूर्ण नाव (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("Full Name (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("मोबाईल क्रमांक") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text(text = "प्रभाग निवडा (Ward):", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (w in 1..6) {
                        FilterChip(
                            selected = wardNum == w,
                            onClick = { wardNum = w },
                            label = { Text("$w") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = addressMr,
                    onValueChange = { addressMr = it },
                    label = { Text("पत्ता (Address)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                citizen.copy(
                                    fullName = nameMr,
                                    fullNameEn = nameEn,
                                    mobileNumber = mobile,
                                    wardNumber = wardNum,
                                    address = addressMr
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "सेव्ह करा" else "Save")
                    }
                }
            }
        }
    }
}

// ================= MODULE 3: WARD MANAGEMENT =================
@Composable
fun AdminWardsView(
    wards: List<WardEntity>,
    language: AppLanguage,
    onSaveWard: (WardEntity) -> Unit,
    onDeleteWard: (String) -> Unit
) {
    var editingWard by remember { mutableStateOf<WardEntity?>(null) }
    var deletingWardId by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "🏘️ प्रभाग व्यवस्थापन (${wards.size})" else "🏘️ Ward Management (${wards.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    val nextNum = (wards.maxOfOrNull { it.wardNumber } ?: 0) + 1
                    editingWard = WardEntity(
                        id = "ward_$nextNum",
                        wardNumber = nextNum,
                        nameMr = "प्रभाग $nextNum",
                        nameEn = "Ward $nextNum",
                        descriptionMr = "",
                        population = "१,०००",
                        representativeName = ""
                    )
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (language == AppLanguage.MARATHI) "+ प्रभाग जोडा" else "+ Add Ward")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(wards) { ward ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${ward.wardNumber}",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.MARATHI) ward.nameMr else ward.nameEn,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "प्रतिनिधी: ${ward.representativeName.ifBlank { "नियुक्त नाही" }} • लोकसंख्या: ${ward.population}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row {
                                IconButton(onClick = { editingWard = ward }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { deletingWardId = ward.id }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (editingWard != null) {
        AdminWardEditDialog(
            ward = editingWard!!,
            language = language,
            onDismiss = { editingWard = null },
            onSave = { updated ->
                onSaveWard(updated)
                editingWard = null
            }
        )
    }

    if (deletingWardId != null) {
        AlertDialog(
            onDismissRequest = { deletingWardId = null },
            title = { Text(if (language == AppLanguage.MARATHI) "प्रभाग हटवायचा?" else "Delete Ward?") },
            text = { Text(if (language == AppLanguage.MARATHI) "हा प्रभाग हटवल्यास संबंधित रेकॉर्ड नष्ट होतील." else "Are you sure you want to delete this ward record?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteWard(deletingWardId!!)
                        deletingWardId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (language == AppLanguage.MARATHI) "हटवा" else "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingWardId = null }) {
                    Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminWardEditDialog(
    ward: WardEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (WardEntity) -> Unit
) {
    var wardNum by remember { mutableIntStateOf(ward.wardNumber) }
    var nameMr by remember { mutableStateOf(ward.nameMr) }
    var nameEn by remember { mutableStateOf(ward.nameEn) }
    var pop by remember { mutableStateOf(ward.population) }
    var repName by remember { mutableStateOf(ward.representativeName) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "प्रभाग माहिती संपादन" else "Edit Ward Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = nameMr,
                    onValueChange = { nameMr = it },
                    label = { Text("प्रभागाचे नाव (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("Ward Name (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = repName,
                    onValueChange = { repName = it },
                    label = { Text("प्रभाग प्रतिनिधी नाव (Representative)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = pop,
                    onValueChange = { pop = it },
                    label = { Text("अंदाजे लोकसंख्या (Population)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                ward.copy(
                                    wardNumber = wardNum,
                                    nameMr = nameMr,
                                    nameEn = nameEn,
                                    population = pop,
                                    representativeName = repName
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "सेव्ह करा" else "Save")
                    }
                }
            }
        }
    }
}

// ================= MODULE 4: WATER SUPPLY =================
@Composable
fun AdminWaterSupplyView(
    schedules: List<WaterScheduleEntity>,
    wards: List<WardEntity>,
    language: AppLanguage,
    onSave: (WaterScheduleEntity) -> Unit,
    onDelete: (Int) -> Unit
) {
    var editingSchedule by remember { mutableStateOf<WaterScheduleEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "💧 पाणीपुरवठा वेळापत्रक (${schedules.size})" else "💧 Water Supply Schedule (${schedules.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    editingSchedule = WaterScheduleEntity(
                        wardNumber = 1,
                        wardNameMr = "प्रभाग १",
                        wardNameEn = "Ward 1",
                        morningTiming = "सकाळी ६:०० ते ७:३०",
                        eveningTiming = "संध्याकाळी ५:०० ते ६:३०",
                        daysMr = "दररोज",
                        daysEn = "Daily",
                        status = "NORMAL",
                        statusNoteMr = "पाणीपुरवठा सुरळीत",
                        statusNoteEn = "Supply Normal",
                        operatorName = "संतोष पाटील",
                        operatorContact = "+91 9422001122"
                    )
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (language == AppLanguage.MARATHI) "+ वेळापत्रक जोडा" else "+ Add Timetable")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(schedules) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) item.wardNameMr else item.wardNameEn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "🌅 ${item.morningTiming} | 🌆 ${item.eveningTiming}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "स्थिती: ${if (language == AppLanguage.MARATHI) item.statusNoteMr else item.statusNoteEn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (item.status == "NORMAL") Color(0xFF2E7D32) else Color(0xFFC62828)
                                )
                            }

                            Row {
                                IconButton(onClick = { editingSchedule = item }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { onDelete(item.wardNumber) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (editingSchedule != null) {
        AdminWaterScheduleDialog(
            schedule = editingSchedule!!,
            language = language,
            onDismiss = { editingSchedule = null },
            onSave = { updated ->
                onSave(updated)
                editingSchedule = null
            }
        )
    }
}

@Composable
fun AdminWaterScheduleDialog(
    schedule: WaterScheduleEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (WaterScheduleEntity) -> Unit
) {
    var wardNum by remember { mutableIntStateOf(schedule.wardNumber) }
    var nameMr by remember { mutableStateOf(schedule.wardNameMr) }
    var nameEn by remember { mutableStateOf(schedule.wardNameEn) }
    var morning by remember { mutableStateOf(schedule.morningTiming) }
    var evening by remember { mutableStateOf(schedule.eveningTiming) }
    var daysMr by remember { mutableStateOf(schedule.daysMr) }
    var status by remember { mutableStateOf(schedule.status) }
    var noteMr by remember { mutableStateOf(schedule.statusNoteMr) }
    var operatorName by remember { mutableStateOf(schedule.operatorName) }
    var operatorContact by remember { mutableStateOf(schedule.operatorContact) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "पाणीपुरवठा वेळापत्रक संपादन" else "Edit Water Supply Schedule",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text("प्रभाग क्र.:", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (w in 1..6) {
                        FilterChip(
                            selected = wardNum == w,
                            onClick = {
                                wardNum = w
                                nameMr = "प्रभाग $w"
                                nameEn = "Ward $w"
                            },
                            label = { Text("$w") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = morning,
                    onValueChange = { morning = it },
                    label = { Text("सकाळची वेळ (Morning Timing)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = evening,
                    onValueChange = { evening = it },
                    label = { Text("संध्याकाळची वेळ (Evening Timing)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = daysMr,
                    onValueChange = { daysMr = it },
                    label = { Text("वार / दिवस (Days)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text("पुरवठा स्थिती (Status):", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("NORMAL" to "सुरळीत", "DELAYED" to "उशीर", "MAINTENANCE" to "देखभाल").forEach { (st, label) ->
                        FilterChip(
                            selected = status == st,
                            onClick = { status = st },
                            label = { Text(label) }
                        )
                    }
                }

                OutlinedTextField(
                    value = noteMr,
                    onValueChange = { noteMr = it },
                    label = { Text("स्थिती टिप्पणी (Status Note)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = operatorName,
                        onValueChange = { operatorName = it },
                        label = { Text("ऑपरेटर नाव") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = operatorContact,
                        onValueChange = { operatorContact = it },
                        label = { Text("ऑपरेटर फोन") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                schedule.copy(
                                    wardNumber = wardNum,
                                    wardNameMr = nameMr,
                                    wardNameEn = nameEn,
                                    morningTiming = morning,
                                    eveningTiming = evening,
                                    daysMr = daysMr,
                                    status = status,
                                    statusNoteMr = noteMr,
                                    operatorName = operatorName,
                                    operatorContact = operatorContact
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "सेव्ह करा" else "Save")
                    }
                }
            }
        }
    }
}

// ================= MODULE 5: NOTICE MANAGEMENT =================
@Composable
fun AdminNoticesView(
    notices: List<NoticeEntity>,
    language: AppLanguage,
    onSave: (NoticeEntity) -> Unit,
    onDelete: (String) -> Unit
) {
    var editingNotice by remember { mutableStateOf<NoticeEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "📢 अधिकृत सूचना फलक (${notices.size})" else "📢 Official Notices (${notices.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    val id = "NOT-${System.currentTimeMillis()}"
                    editingNotice = NoticeEntity(
                        id = id,
                        titleMr = "",
                        titleEn = "",
                        descriptionMr = "",
                        descriptionEn = "",
                        category = "GRAMSABHA",
                        publishDate = "आज",
                        isUrgent = false
                    )
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (language == AppLanguage.MARATHI) "+ नवीन सूचना" else "+ New Notice")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(notices) { notice ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                if (notice.isUrgent) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MaterialTheme.colorScheme.errorContainer
                                    ) {
                                        Text(
                                            text = "🚨 तातडीची सूचना / Urgent",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                Text(
                                    text = if (language == AppLanguage.MARATHI) notice.titleMr else notice.titleEn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "तारीख: ${notice.publishDate} • प्रवर्ग: ${notice.category}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row {
                                IconButton(onClick = { editingNotice = notice }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { onDelete(notice.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (editingNotice != null) {
        AdminNoticeDialog(
            notice = editingNotice!!,
            language = language,
            onDismiss = { editingNotice = null },
            onSave = { updated ->
                onSave(updated)
                editingNotice = null
            }
        )
    }
}

@Composable
fun AdminNoticeDialog(
    notice: NoticeEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (NoticeEntity) -> Unit
) {
    var titleMr by remember { mutableStateOf(notice.titleMr) }
    var titleEn by remember { mutableStateOf(notice.titleEn) }
    var descMr by remember { mutableStateOf(notice.descriptionMr) }
    var descEn by remember { mutableStateOf(notice.descriptionEn) }
    var category by remember { mutableStateOf(notice.category) }
    var publishDate by remember { mutableStateOf(notice.publishDate) }
    var isUrgent by remember { mutableStateOf(notice.isUrgent) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "सूचना संपादन" else "Edit Notice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = titleMr,
                    onValueChange = { titleMr = it },
                    label = { Text("शीर्षक (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("Title (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = descMr,
                    onValueChange = { descMr = it },
                    label = { Text("तपशील (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 3
                )

                OutlinedTextField(
                    value = descEn,
                    onValueChange = { descEn = it },
                    label = { Text("Description (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 2
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("तातडीची सूचना आहे का? (Urgent):", style = MaterialTheme.typography.bodySmall)
                    Switch(checked = isUrgent, onCheckedChange = { isUrgent = it })
                }

                OutlinedTextField(
                    value = publishDate,
                    onValueChange = { publishDate = it },
                    label = { Text("प्रसिद्धी तारीख") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                notice.copy(
                                    titleMr = titleMr,
                                    titleEn = titleEn,
                                    descriptionMr = descMr,
                                    descriptionEn = descEn,
                                    category = category,
                                    publishDate = publishDate,
                                    isUrgent = isUrgent
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "प्रसिद्ध करा" else "Publish")
                    }
                }
            }
        }
    }
}

// ================= MODULE 6: GRIEVANCE REDRESSAL =================
@Composable
fun AdminGrievancesView(
    complaints: List<ComplaintEntity>,
    language: AppLanguage,
    onUpdate: (id: String, status: String, remarks: String, officer: String) -> Unit,
    onDelete: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var updatingComplaint by remember { mutableStateOf<ComplaintEntity?>(null) }

    val filtered = remember(complaints, selectedFilter) {
        if (selectedFilter == "ALL") complaints
        else complaints.filter { it.status == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (language == AppLanguage.MARATHI) "📋 नागरिक तक्रार निवारण कक्ष (${filtered.size})" else "📋 Grievance Redressal Console (${filtered.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("ALL" to "सर्व", "PENDING" to "प्रलंबित", "IN_PROGRESS" to "प्रगतीपथावर", "RESOLVED" to "निवारण").forEach { (f, label) ->
                FilterChip(
                    selected = selectedFilter == f,
                    onClick = { selectedFilter = f },
                    label = { Text(label) }
                )
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtered) { complaint ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = complaint.id,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            StatusBadge(status = complaint.status, language = language)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = complaint.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = complaint.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "📍 प्रभाग क्र. ${complaint.wardNumber} • ${complaint.locationDetail}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (complaint.officialRemarks.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "शासकीय शेरा: ${complaint.officialRemarks}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = { onDelete(complaint.id) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(if (language == AppLanguage.MARATHI) "हटवा" else "Delete")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { updatingComplaint = complaint },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(if (language == AppLanguage.MARATHI) "शेरा व स्थिती बदला" else "Update Status")
                            }
                        }
                    }
                }
            }
        }
    }

    if (updatingComplaint != null) {
        AdminComplaintUpdateDialog(
            complaint = updatingComplaint!!,
            language = language,
            onDismiss = { updatingComplaint = null },
            onSave = { status, remarks, officer ->
                onUpdate(updatingComplaint!!.id, status, remarks, officer)
                updatingComplaint = null
            }
        )
    }
}

@Composable
fun AdminComplaintUpdateDialog(
    complaint: ComplaintEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (status: String, remarks: String, officer: String) -> Unit
) {
    var status by remember { mutableStateOf(complaint.status) }
    var remarks by remember { mutableStateOf(complaint.officialRemarks) }
    var officer by remember { mutableStateOf(complaint.assignedOfficer) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "तक्रार निवारण शेरा नोंदवा" else "Update Grievance Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${complaint.id}: ${complaint.title}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text("तक्रार स्थिती बदला (Status):", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("PENDING" to "प्रलंबित", "IN_PROGRESS" to "प्रगतीपथावर", "RESOLVED" to "निवारण", "REJECTED" to "नाकारले").forEach { (st, label) ->
                        FilterChip(
                            selected = status == st,
                            onClick = { status = st },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = officer,
                    onValueChange = { officer = it },
                    label = { Text("नेमलेला अधिकारी / कर्मचारी") },
                    placeholder = { Text("उदा. वायरमन संतोष पाटील") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("शासकीय शेरा व निवारण तपशील") },
                    placeholder = { Text("उदा. तक्रारीची जागेवर तपासणी करून दुरुस्ती पूर्ण करण्यात आली.") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = { onSave(status, remarks, officer) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "अपडेट करा" else "Save Update")
                    }
                }
            }
        }
    }
}

// ================= MODULE 7: GP DIRECTORY =================
@Composable
fun AdminGPDirectoryView(
    panchayatProfile: PanchayatProfile,
    officials: List<OfficialContactEntity>,
    language: AppLanguage
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (language == AppLanguage.MARATHI) "🏢 ग्रामपंचायत कार्यालय व निर्देशिका" else "🏢 Grampanchayat Directory",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "🏛️ ${if (language == AppLanguage.MARATHI) panchayatProfile.nameMr else panchayatProfile.nameEn}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = "📍 ${panchayatProfile.addressMr}")
                Text(text = "📞 दूरध्वनी: ${panchayatProfile.phone}")
                Text(text = "✉️ ईमेल: ${panchayatProfile.email}")
                Text(text = "🌐 वेबसाइट: ${panchayatProfile.website}")
                Text(text = "🕒 कार्यालयीन वेळ: ${panchayatProfile.officeHoursMr}")
            }
        }

        Text(
            text = if (language == AppLanguage.MARATHI) "पदाधिकारी व कर्मचारी यादी" else "Officials Directory",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        for (official in officials) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Badge, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = if (language == AppLanguage.MARATHI) official.nameMr else official.nameEn, fontWeight = FontWeight.Bold)
                        Text(text = "${if (language == AppLanguage.MARATHI) official.designationMr else official.designationEn} • ${official.phoneNumber}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

// ================= MODULE 8: STAFF & OFFICIALS =================
@Composable
fun AdminStaffView(
    officials: List<OfficialContactEntity>,
    language: AppLanguage,
    onSave: (OfficialContactEntity) -> Unit,
    onDelete: (String) -> Unit
) {
    var editingOfficial by remember { mutableStateOf<OfficialContactEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "👔 अधिकारी व कर्मचारी व्यवस्थापन (${officials.size})" else "👔 Staff Management (${officials.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    editingOfficial = OfficialContactEntity(
                        id = "off_${System.currentTimeMillis()}",
                        nameMr = "",
                        nameEn = "",
                        designationMr = "",
                        designationEn = "",
                        phoneNumber = "+91 ",
                        wardOrDeptMr = "",
                        wardOrDeptEn = ""
                    )
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (language == AppLanguage.MARATHI) "+ नवीन जोडा" else "+ Add Staff")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(officials) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) item.nameMr else item.nameEn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${if (language == AppLanguage.MARATHI) item.designationMr else item.designationEn} • ${item.phoneNumber}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row {
                            IconButton(onClick = { editingOfficial = item }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { onDelete(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    if (editingOfficial != null) {
        AdminStaffDialog(
            official = editingOfficial!!,
            language = language,
            onDismiss = { editingOfficial = null },
            onSave = { updated ->
                onSave(updated)
                editingOfficial = null
            }
        )
    }
}

@Composable
fun AdminStaffDialog(
    official: OfficialContactEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (OfficialContactEntity) -> Unit
) {
    var nameMr by remember { mutableStateOf(official.nameMr) }
    var nameEn by remember { mutableStateOf(official.nameEn) }
    var desigMr by remember { mutableStateOf(official.designationMr) }
    var desigEn by remember { mutableStateOf(official.designationEn) }
    var phone by remember { mutableStateOf(official.phoneNumber) }
    var deptMr by remember { mutableStateOf(official.wardOrDeptMr) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "अधिकारी / कर्मचारी माहिती" else "Official Contact Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = nameMr,
                    onValueChange = { nameMr = it },
                    label = { Text("नाव (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("Name (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = desigMr,
                    onValueChange = { desigMr = it },
                    label = { Text("पद / हुद्दा (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = desigEn,
                    onValueChange = { desigEn = it },
                    label = { Text("Designation (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("मोबाईल / संपर्क फोन") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = deptMr,
                    onValueChange = { deptMr = it },
                    label = { Text("विभाग / प्रभाग (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                official.copy(
                                    nameMr = nameMr,
                                    nameEn = nameEn,
                                    designationMr = desigMr,
                                    designationEn = desigEn,
                                    phoneNumber = phone,
                                    wardOrDeptMr = deptMr
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "सेव्ह करा" else "Save")
                    }
                }
            }
        }
    }
}

// ================= MODULE 9: PROJECTS =================
@Composable
fun AdminProjectsView(
    projects: List<DevelopmentProjectEntity>,
    language: AppLanguage,
    onSave: (DevelopmentProjectEntity) -> Unit,
    onDelete: (String) -> Unit
) {
    var editingProject by remember { mutableStateOf<DevelopmentProjectEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "🏗️ विकासकामे व प्रकल्प (${projects.size})" else "🏗️ Development Projects (${projects.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    editingProject = DevelopmentProjectEntity(
                        id = "proj_${System.currentTimeMillis()}",
                        titleMr = "",
                        titleEn = "",
                        sanctionedBudget = "रु. १०,००,०००/-",
                        duration = "३ महिने",
                        status = "IN_PROGRESS"
                    )
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (language == AppLanguage.MARATHI) "+ नवीन प्रकल्प" else "+ Add Project")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(projects) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) item.titleMr else item.titleEn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "💰 मंजूर निधी: ${item.sanctionedBudget} • कालावधी: ${item.duration}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row {
                                IconButton(onClick = { editingProject = item }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { onDelete(item.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (editingProject != null) {
        AdminProjectDialog(
            project = editingProject!!,
            language = language,
            onDismiss = { editingProject = null },
            onSave = { updated ->
                onSave(updated)
                editingProject = null
            }
        )
    }
}

@Composable
fun AdminProjectDialog(
    project: DevelopmentProjectEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (DevelopmentProjectEntity) -> Unit
) {
    var titleMr by remember { mutableStateOf(project.titleMr) }
    var titleEn by remember { mutableStateOf(project.titleEn) }
    var budget by remember { mutableStateOf(project.sanctionedBudget) }
    var duration by remember { mutableStateOf(project.duration) }
    var status by remember { mutableStateOf(project.status) }
    var loc by remember { mutableStateOf(project.location) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "प्रकल्प तपशील संपादन" else "Edit Project Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = titleMr,
                    onValueChange = { titleMr = it },
                    label = { Text("प्रकल्प नाव (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("Project Title (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = budget,
                    onValueChange = { budget = it },
                    label = { Text("मंजूर निधी (Budget)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("कालावधी (Duration)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = loc,
                    onValueChange = { loc = it },
                    label = { Text("ठिकाण (Location)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Text("प्रकल्प स्थिती (Status):", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("PENDING" to "नियोजित", "IN_PROGRESS" to "प्रगतीपथावर", "RESOLVED" to "पूर्ण").forEach { (st, label) ->
                        FilterChip(
                            selected = status == st,
                            onClick = { status = st },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                project.copy(
                                    titleMr = titleMr,
                                    titleEn = titleEn,
                                    sanctionedBudget = budget,
                                    duration = duration,
                                    status = status,
                                    location = loc
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "सेव्ह करा" else "Save")
                    }
                }
            }
        }
    }
}

// ================= MODULE 10: ONLINE SERVICES =================
@Composable
fun AdminServicesView(
    services: List<OnlineServiceItem>,
    language: AppLanguage,
    onSave: (OnlineServiceItem) -> Unit,
    onDelete: (String) -> Unit
) {
    var editingService by remember { mutableStateOf<OnlineServiceItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.MARATHI) "📑 ऑनलाइन सेवा सूची (${services.size})" else "📑 Online Services (${services.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    editingService = OnlineServiceItem(
                        id = "srv_${System.currentTimeMillis()}",
                        key = "custom_${System.currentTimeMillis()}",
                        titleMr = "",
                        titleEn = "",
                        descriptionMr = "",
                        requiredDocsMr = "",
                        fee = 20,
                        processingDays = "३ दिवस"
                    )
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (language == AppLanguage.MARATHI) "+ नवीन सेवा" else "+ Add Service")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(services) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) item.titleMr else item.titleEn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "शुल्क: रु. ${item.fee}/- • कालावधी: ${item.processingDays}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row {
                            IconButton(onClick = { editingService = item }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { onDelete(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    if (editingService != null) {
        AdminServiceDialog(
            service = editingService!!,
            language = language,
            onDismiss = { editingService = null },
            onSave = { updated ->
                onSave(updated)
                editingService = null
            }
        )
    }
}

@Composable
fun AdminServiceDialog(
    service: OnlineServiceItem,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (OnlineServiceItem) -> Unit
) {
    var titleMr by remember { mutableStateOf(service.titleMr) }
    var titleEn by remember { mutableStateOf(service.titleEn) }
    var descMr by remember { mutableStateOf(service.descriptionMr) }
    var docsMr by remember { mutableStateOf(service.requiredDocsMr) }
    var fee by remember { mutableIntStateOf(service.fee) }
    var days by remember { mutableStateOf(service.processingDays) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "ऑनलाइन सेवा संपादन" else "Edit Online Service",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = titleMr,
                    onValueChange = { titleMr = it },
                    label = { Text("सेवेचे नाव (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("Service Title (English)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = descMr,
                    onValueChange = { descMr = it },
                    label = { Text("तपशील (Description)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = docsMr,
                    onValueChange = { docsMr = it },
                    label = { Text("आवश्यक कागदपत्रे (Required Documents)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = "$fee",
                        onValueChange = { fee = it.toIntOrNull() ?: 0 },
                        label = { Text("शासकीय शुल्क (रु.)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = days,
                        onValueChange = { days = it },
                        label = { Text("कालावधी (Days)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                service.copy(
                                    titleMr = titleMr,
                                    titleEn = titleEn,
                                    descriptionMr = descMr,
                                    requiredDocsMr = docsMr,
                                    fee = fee,
                                    processingDays = days
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (language == AppLanguage.MARATHI) "सेव्ह करा" else "Save")
                    }
                }
            }
        }
    }
}

// ================= MODULE 11: NOTIFICATIONS =================
@Composable
fun AdminNotificationsView(
    notifications: List<NotificationItem>,
    language: AppLanguage,
    onSave: (NotificationItem) -> Unit,
    onDelete: (String) -> Unit
) {
    var titleMr by remember { mutableStateOf("") }
    var titleEn by remember { mutableStateOf("") }
    var msgMr by remember { mutableStateOf("") }
    var msgEn by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (language == AppLanguage.MARATHI) "🔔 नवीन ब्रॉडकास्ट अलर्ट पाठवा" else "🔔 Broadcast Notification",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = titleMr,
                    onValueChange = { titleMr = it },
                    label = { Text("सूचना शीर्षक (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = msgMr,
                    onValueChange = { msgMr = it },
                    label = { Text("सूचना संदेश (मराठी)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("तातडीचा अलर्ट (Urgent Alert):", style = MaterialTheme.typography.bodySmall)
                    Switch(checked = isUrgent, onCheckedChange = { isUrgent = it })
                }

                Button(
                    onClick = {
                        if (titleMr.isNotBlank()) {
                            onSave(
                                NotificationItem(
                                    id = "notif_${System.currentTimeMillis()}",
                                    titleMr = titleMr,
                                    titleEn = titleEn.ifBlank { titleMr },
                                    messageMr = msgMr,
                                    messageEn = msgEn.ifBlank { msgMr },
                                    timestamp = "आत्ताच",
                                    isUrgent = isUrgent
                                )
                            )
                            titleMr = ""
                            titleEn = ""
                            msgMr = ""
                            msgEn = ""
                            isUrgent = false
                        }
                    },
                    enabled = titleMr.isNotBlank(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (language == AppLanguage.MARATHI) "सर्व नागरिकांना अलर्ट पाठवा" else "Broadcast Alert")
                }
            }
        }

        Text(
            text = if (language == AppLanguage.MARATHI) "प्रसिद्ध झालेले अलर्ट (${notifications.size})" else "Active Alerts (${notifications.size})",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(notifications) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = if (language == AppLanguage.MARATHI) item.titleMr else item.titleEn, fontWeight = FontWeight.Bold)
                            Text(text = if (language == AppLanguage.MARATHI) item.messageMr else item.messageEn, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { onDelete(item.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

// ================= MODULE 12: SETTINGS & SYNC =================
@Composable
fun AdminSettingsSyncView(
    language: AppLanguage,
    isLoading: Boolean,
    onSeedFirestore: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (language == AppLanguage.MARATHI) "⚙️ ॲप सेटिंग्ज व Firestore सिंक" else "⚙️ App Settings & Firestore Sync",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "क्लाउड डेटा इनिशियलायझेशन (Initial Seed)" else "Cloud Data Initialization (Initial Seed)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "जर Firestore डेटाबेस रिक्त असेल, तर हे बटण दाबून सर्व प्रारंभिक डेटा (ग्रामपंचायत प्रोफाइल, प्रभाग, पाणीपुरवठा वेळापत्रक, अधिकारी, प्रकल्प, सूचना) एका क्लिकमध्ये Firestore मध्ये अपलोड करता येईल."
                    else
                        "If the Firestore database is newly created or empty, click below to upload all initial data (Panchayat profile, wards, water supply, officials, projects, notices) to Firestore in one tap.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = onSeedFirestore,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (language == AppLanguage.MARATHI) "अपलोड होत आहे..." else "Uploading...")
                    } else {
                        Icon(Icons.Default.CloudUpload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (language == AppLanguage.MARATHI) "प्रारंभिक डेटा Firestore मध्ये सिंक करा" else "Seed Initial Data to Firestore")
                    }
                }
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (language == AppLanguage.MARATHI) "सुरक्षा व विशेषाधिकार" else "Security & Privileges",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "• Firestore Security Rules: सर्व सामान्य डेटा सार्वजनिक वाचनीय आहे, तर संपादन फक्त अधिकृत ॲडमिनद्वारे करता येते.\n• ऑफलाइन कॅशे: नेटवर्क उपलब्ध नसताना Room डेटाबेसद्वारे ॲप सुरळीत चालू राहते."
                    else
                        "• Firestore Security Rules: All public datasets are read-only for citizens and editable only by verified Admins.\n• Offline Support: Room database serves as instant local cache when offline.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (language == AppLanguage.MARATHI) "प्रशासन कक्षेतून बाहेर पडा (Logout)" else "Sign Out Admin Console")
        }
    }
}
