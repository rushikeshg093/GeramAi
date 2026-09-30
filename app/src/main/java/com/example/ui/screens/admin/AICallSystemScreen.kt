package com.example.ui.screens.admin

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.*
import java.text.SimpleDateFormat
import java.util.*

enum class AICallTab(val titleMr: String, val titleEn: String, val icon: ImageVector) {
    DASHBOARD("डॅशबोर्ड", "Dashboard", Icons.Default.Dashboard),
    CREATE("नवीन घोषणा", "Create Call", Icons.Default.AddCircleOutline),
    PREVIEW("पूर्वदृश्य व चाचणी", "Voice Preview", Icons.Default.RecordVoiceOver),
    HISTORY("कॉल इतिहास", "Call History", Icons.Default.History),
    SCHEDULED("नियोजित कॉल्स", "Scheduled", Icons.Default.Schedule),
    SETTINGS("सेटिंग्ज", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AICallSystemScreen(
    language: AppLanguage,
    panchayatProfile: PanchayatProfile,
    aiCampaigns: List<AiCallCampaign>,
    aiCallLogs: List<AiCallLog>,
    aiScheduledCalls: List<AiScheduledCall>,
    aiCallSettings: AiCallSettings,
    citizens: List<UserProfile>,
    isBatchCallingActive: Boolean,
    batchProgress: Pair<Int, Int>,
    currentCallingCitizen: String,
    isVoicePlaying: Boolean,
    onPreviewVoice: (text: String, lang: String, voiceId: String) -> Unit,
    onStopPreviewVoice: () -> Unit,
    onStartCampaign: (title: String, msg: String, lang: String, voiceId: String, target: String) -> Unit,
    onScheduleCall: (title: String, msg: String, lang: String, voiceId: String, target: String, date: String, time: String, ts: Long) -> Unit,
    onCancelScheduledCall: (id: String) -> Unit,
    onSaveSettings: (AiCallSettings) -> Unit,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(AICallTab.DASHBOARD) }

    // Form states for announcement creation
    var announcementTitle by remember { mutableStateOf("") }
    var announcementMessage by remember { mutableStateOf("") }
    var selectedVoice by remember { mutableStateOf(AiVoiceType.AROHI) }
    var selectedTargetGroup by remember { mutableStateOf(AiTargetGroup.ALL) }
    var callMode by remember { mutableStateOf("NOW") } // "NOW" or "SCHEDULE"
    var scheduledDate by remember { mutableStateOf("१० सप्टेंबर २०२६") }
    var scheduledTime by remember { mutableStateOf("सकाळी १०:००") }
    var selectedLogForDetail by remember { mutableStateOf<AiCallLog?>(null) }

    val isMarathi = language == AppLanguage.MARATHI

    // Live Calling Batch Dialog
    if (isBatchCallingActive) {
        LiveCallBatchDialog(
            isMarathi = isMarathi,
            progress = batchProgress,
            currentCitizen = currentCallingCitizen,
            voiceName = selectedVoice.nameMr
        )
    }

    // Detail Dialog for Call Log
    selectedLogForDetail?.let { log ->
        CallLogDetailDialog(
            log = log,
            isMarathi = isMarathi,
            onDismiss = { selectedLogForDetail = null }
        )
    }

    Scaffold(
        containerColor = Color(0xFF0F172A),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🤖 AI कॉल सिस्टम",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = if (aiCallSettings.isDemoMode) Color(0xFFE65100) else Color(0xFF00C853),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (aiCallSettings.isDemoMode) "डेमो मोड" else "लाईव्ह मोड",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = panchayatProfile.nameMr,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E293B))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Strict Policy & Disclaimer Bar
            Surface(
                color = Color(0xFF1E1B4B),
                border = BorderStroke(1.dp, Color(0xFF4338CA).copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color(0xFF818CF8),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isMarathi)
                            "केवळ अधिकृत प्रशासकीय घोषणांसाठी | AI वर तक्रार नोंदणी होत नाही"
                        else
                            "Outgoing Announcements Only | No grievance intake on AI calls",
                        fontSize = 11.sp,
                        color = Color(0xFFC7D2FE),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Horizontal Tab Bar
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = Color(0xFF1E293B),
                contentColor = Color(0xFF38BDF8),
                edgePadding = 8.dp,
                divider = {}
            ) {
                AICallTab.values().forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isMarathi) tab.titleMr else tab.titleEn,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        },
                        selectedContentColor = Color(0xFF38BDF8),
                        unselectedContentColor = Color(0xFF94A3B8)
                    )
                }
            }

            // Tab Content
            Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                when (selectedTab) {
                    AICallTab.DASHBOARD -> AICallDashboardView(
                        isMarathi = isMarathi,
                        campaigns = aiCampaigns,
                        logs = aiCallLogs,
                        scheduled = aiScheduledCalls,
                        settings = aiCallSettings,
                        onNavigateCreate = { selectedTab = AICallTab.CREATE },
                        onNavigateHistory = { selectedTab = AICallTab.HISTORY }
                    )
                    AICallTab.CREATE -> AICallCreateView(
                        isMarathi = isMarathi,
                        title = announcementTitle,
                        onTitleChange = { announcementTitle = it },
                        message = announcementMessage,
                        onMessageChange = { announcementMessage = it },
                        selectedVoice = selectedVoice,
                        onVoiceSelect = { selectedVoice = it },
                        selectedTarget = selectedTargetGroup,
                        onTargetSelect = { selectedTargetGroup = it },
                        callMode = callMode,
                        onCallModeChange = { callMode = it },
                        scheduledDate = scheduledDate,
                        onDateChange = { scheduledDate = it },
                        scheduledTime = scheduledTime,
                        onTimeChange = { scheduledTime = it },
                        onGoToPreview = { selectedTab = AICallTab.PREVIEW }
                    )
                    AICallTab.PREVIEW -> AICallPreviewView(
                        isMarathi = isMarathi,
                        panchayatName = panchayatProfile.nameMr,
                        title = announcementTitle.ifBlank { "ग्रामपंचायत विशेष सूचना" },
                        message = announcementMessage.ifBlank { "आदर्श ग्रामपंचायत पळसखेड दौलतच्या सर्व नागरिकांना कळविण्यात येते की..." },
                        voice = selectedVoice,
                        targetGroup = selectedTargetGroup,
                        callMode = callMode,
                        scheduledDate = scheduledDate,
                        scheduledTime = scheduledTime,
                        isVoicePlaying = isVoicePlaying,
                        onPlayPreview = {
                            onPreviewVoice(
                                announcementMessage.ifBlank { "आदर्श ग्रामपंचायत पळसखेड दौलतच्या वतीने ही चाचणी घोषणा आहे." },
                                "mr",
                                selectedVoice.id
                            )
                        },
                        onStopPreview = onStopPreviewVoice,
                        onStartCampaignNow = {
                            val finalTitle = announcementTitle.ifBlank { "ग्रामपंचायत विशेष जाहीर सूचना" }
                            val finalMsg = announcementMessage.ifBlank { "सर्व सन्माननीय ग्रामस्थांना ग्रामपंचायत कार्यालयाच्या वतीने कळविण्यात येते." }
                            onStartCampaign(finalTitle, finalMsg, "mr", selectedVoice.id, selectedTargetGroup.key)
                        },
                        onConfirmSchedule = {
                            val finalTitle = announcementTitle.ifBlank { "ग्रामपंचायत विशेष जाहीर सूचना" }
                            val finalMsg = announcementMessage.ifBlank { "सर्व सन्माननीय ग्रामस्थांना ग्रामपंचायत कार्यालयाच्या वतीने कळविण्यात येते." }
                            onScheduleCall(
                                finalTitle,
                                finalMsg,
                                "mr",
                                selectedVoice.id,
                                selectedTargetGroup.key,
                                scheduledDate,
                                scheduledTime,
                                System.currentTimeMillis() + 86400000L
                            )
                            selectedTab = AICallTab.SCHEDULED
                        }
                    )
                    AICallTab.HISTORY -> AICallHistoryView(
                        isMarathi = isMarathi,
                        callLogs = aiCallLogs,
                        onLogClick = { selectedLogForDetail = it }
                    )
                    AICallTab.SCHEDULED -> AICallScheduledView(
                        isMarathi = isMarathi,
                        scheduledCalls = aiScheduledCalls,
                        onCancel = onCancelScheduledCall,
                        onAddNew = { selectedTab = AICallTab.CREATE }
                    )
                    AICallTab.SETTINGS -> AICallSettingsView(
                        isMarathi = isMarathi,
                        settings = aiCallSettings,
                        onSave = onSaveSettings
                    )
                }
            }
        }
    }
}

// ================= TAB 1: DASHBOARD =================

@Composable
fun AICallDashboardView(
    isMarathi: Boolean,
    campaigns: List<AiCallCampaign>,
    logs: List<AiCallLog>,
    scheduled: List<AiScheduledCall>,
    settings: AiCallSettings,
    onNavigateCreate: () -> Unit,
    onNavigateHistory: () -> Unit
) {
    val totalCalls = logs.size
    val connectedCalls = logs.count { it.status == "CONNECTED" }
    val unansweredCalls = logs.count { it.status == "NOT_ANSWERED" }
    val busyCalls = logs.count { it.status == "BUSY" }
    val failedCalls = logs.count { it.status == "FAILED" }
    val successRate = if (totalCalls > 0) (connectedCalls * 100) / totalCalls else 85

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Action Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isMarathi) "🤖 AI कॉल केंद्र" else "AI Call Center",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isMarathi) "स्वयंचलित व्हॉईस कॉलद्वारे नागरिकांना सूचना" else "Automated citizen announcement system",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Button(
                        onClick = onNavigateCreate,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PhoneCallback, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isMarathi) "कॉल सुरू करा" else "New Call", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Stats KPI Grid
        Text(
            text = if (isMarathi) "📊 कॉलिंग आकडेवारी व कार्यक्षमता" else "Call Statistics & Performance",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KPICard(
                title = if (isMarathi) "एकूण कॉल्स" else "Total Calls",
                value = "$totalCalls",
                subtitle = if (isMarathi) "मोहीम नोंदी" else "All records",
                color = Color(0xFF38BDF8),
                modifier = Modifier.weight(1f)
            )
            KPICard(
                title = if (isMarathi) "यशस्वी जोडले" else "Connected",
                value = "$connectedCalls",
                subtitle = "$successRate% यश दर",
                color = Color(0xFF4ADE80),
                modifier = Modifier.weight(1f)
            )
            KPICard(
                title = if (isMarathi) "उत्तर नाही" else "Unanswered",
                value = "$unansweredCalls",
                subtitle = if (isMarathi) "पुन्हा प्रयत्न" else "Pending retry",
                color = Color(0xFFFBBF24),
                modifier = Modifier.weight(1f)
            )
            KPICard(
                title = if (isMarathi) "व्यस्त/त्रुटी" else "Busy/Fail",
                value = "${busyCalls + failedCalls}",
                subtitle = if (isMarathi) "नेटवर्क त्रुटी" else "Network issue",
                color = Color(0xFFF87171),
                modifier = Modifier.weight(1f)
            )
        }

        // Recent Campaigns Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isMarathi) "📢 अलीकडील कॉल मोहीमा" else "Recent Campaigns",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            TextButton(onClick = onNavigateHistory) {
                Text(text = if (isMarathi) "सर्व पहा" else "View All", color = Color(0xFF38BDF8), fontSize = 12.sp)
            }
        }

        campaigns.take(3).forEach { camp ->
            CampaignSummaryCard(campaign = camp, isMarathi = isMarathi)
        }

        // Telephony Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF0F766E).copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.CloudQueue, contentDescription = null, tint = Color(0xFF2DD4BF))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (settings.isDemoMode)
                            "सिम्युलेशन व सुरक्षित चाचणी मोड सक्रिय"
                        else
                            "लाईव्ह टेलिफोनी गेटवे कनेक्टेड",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isMarathi)
                            "कॉल्सची वेळ: सकाळी ${settings.callingHoursStart} ते संध्याकाळी ${settings.callingHoursEnd} (TRAI नियमांनुसार)"
                        else
                            "Calling hours: ${settings.callingHoursStart} to ${settings.callingHoursEnd}",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}

@Composable
fun KPICard(title: String, value: String, subtitle: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 11.sp, color = Color(0xFF94A3B8), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 9.sp, color = Color(0xFF64748B), maxLines = 1)
        }
    }
}

@Composable
fun CampaignSummaryCard(campaign: AiCallCampaign, isMarathi: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF334155))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = campaign.announcementTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = Color(0xFF14532D),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (campaign.status == "COMPLETED") "पूर्ण झाले" else campaign.status,
                        fontSize = 10.sp,
                        color = Color(0xFF4ADE80),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = campaign.announcementMessage,
                fontSize = 12.sp,
                color = Color(0xFFCBD5E1),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🎯 ${campaign.targetAudienceLabel}",
                    fontSize = 11.sp,
                    color = Color(0xFF38BDF8)
                )
                Text(
                    text = "✅ ${campaign.connectedCalls}/${campaign.totalRecipients} जोडले",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ADE80)
                )
            }
        }
    }
}

// ================= TAB 2: CREATE ANNOUNCEMENT =================

@Composable
fun AICallCreateView(
    isMarathi: Boolean,
    title: String,
    onTitleChange: (String) -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    selectedVoice: AiVoiceType,
    onVoiceSelect: (AiVoiceType) -> Unit,
    selectedTarget: AiTargetGroup,
    onTargetSelect: (AiTargetGroup) -> Unit,
    callMode: String,
    onCallModeChange: (String) -> Unit,
    scheduledDate: String,
    onDateChange: (String) -> Unit,
    scheduledTime: String,
    onTimeChange: (String) -> Unit,
    onGoToPreview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Quick Templates
        Text(
            text = if (isMarathi) "⚡ जलद विषय निवडा (Templates)" else "Quick Announcement Templates",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                TemplateChip(label = "📢 विशेष ग्रामसभा") {
                    onTitleChange("स्वातंत्र्य दिन विशेष ग्रामसभा सूचना")
                    onMessageChange("आदर्श ग्रामपंचायत पळसखेड दौलतच्या सर्व नागरिकांना कळविण्यात येते की आगामी १५ ऑगस्ट रोजी सकाळी १० वाजता विशेष ग्रामसभेचे आयोजन केले आहे. सर्वांनी उपस्थित राहावे.")
                }
            }
            item {
                TemplateChip(label = "💧 पाणीपुरवठा सूचना") {
                    onTitleChange("पाणीपुरवठा दुरुस्ती व वेळ बदल सूचना")
                    onMessageChange("मुख्य जलवाहिनीच्या देखभाल दुरुस्ती कामामुळे उद्या सकाळी पाणीपुरवठा २ तास उशिरा होणार आहे. कृपया नागरिकांनी नोंद घ्यावी.")
                }
            }
            item {
                TemplateChip(label = "💰 कर सवलत योजना") {
                    onTitleChange("घरपट्टी व पाणीपट्टी १०% विशेष सवलत")
                    onMessageChange("चालू आर्थिक वर्षाचा कर ३१ ऑगस्टपूर्वी भरणाऱ्या नागरिकांना १० टक्के विशेष सूट दिली जात आहे. ग्रामपंचायत कार्यालयात किंवा ऑनलाइन भरणा करावा.")
                }
            }
            item {
                TemplateChip(label = "💉 आरोग्य शिबिर") {
                    onTitleChange("मोफत आरोग्य व बाल लसीकरण शिबिर")
                    onMessageChange("ग्रामपंचायत आणि प्राथमिक आरोग्य केंद्रातर्फे आगामी मंगळवारी मोफत आरोग्य तपासणी व लसीकरण शिबिर आयोजित करण्यात आले आहे.")
                }
            }
        }

        // Title Input
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text(if (isMarathi) "घोषणा शीर्षक / विषय" else "Announcement Title", color = Color(0xFF94A3B8)) },
            placeholder = { Text("उदा. ग्रामसभा जाहीर सूचना किंवा पाणीपुरवठा दुरुस्ती", color = Color(0xFF64748B)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0xFF334155),
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            ),
            shape = RoundedCornerShape(10.dp)
        )

        // Announcement Message Input
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text(if (isMarathi) "AI द्वारे वाचला जाणारा अधिकृत संदेश (मराठी)" else "Announcement Message (Marathi)", color = Color(0xFF94A3B8)) },
            placeholder = { Text("येथे नागरिकांना सांगावयाची माहिती स्पष्टपणे लिहा...", color = Color(0xFF64748B)) },
            minLines = 4,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF38BDF8),
                unfocusedBorderColor = Color(0xFF334155),
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            ),
            shape = RoundedCornerShape(10.dp)
        )

        // Voice Persona Selector
        Text(
            text = if (isMarathi) "🎙️ AI व्हॉईस स्वर निवडा" else "Select AI Voice Persona",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AiVoiceType.values().forEach { voice ->
                val isSelected = selectedVoice == voice
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVoiceSelect(voice) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF0C4A6E) else Color(0xFF1E293B)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onVoiceSelect(voice) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF38BDF8))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isMarathi) voice.nameMr else voice.nameEn,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isMarathi) voice.personaMr else voice.personaEn,
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Surface(
                            color = if (voice.gender == "FEMALE") Color(0xFF831843) else Color(0xFF1E3A8A),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (voice.gender == "FEMALE") "महिला स्वर" else "पुरुष स्वर",
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Target Group Selector
        Text(
            text = if (isMarathi) "🎯 कॉल कोणाला पाठवायचा? (Target Group)" else "Select Target Audience",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            AiTargetGroup.values().take(6).forEach { target ->
                val isSelected = selectedTarget == target
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTargetSelect(target) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF064E3B) else Color(0xFF1E293B)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFF34D399) else Color(0xFF334155)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onTargetSelect(target) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF34D399))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isMarathi) target.labelMr else target.labelEn,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            color = Color(0xFF334155),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "~${target.estimatedCitizens} नागरिक",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Mode: Now or Schedule
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { onCallModeChange("NOW") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (callMode == "NOW") Color(0xFF0284C7) else Color(0xFF334155)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.PhoneCallback, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isMarathi) "आत्ताच कॉल करा" else "Call Now")
            }

            Button(
                onClick = { onCallModeChange("SCHEDULE") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (callMode == "SCHEDULE") Color(0xFF7C3AED) else Color(0xFF334155)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isMarathi) "कॉल शेड्यूल करा" else "Schedule Call")
            }
        }

        if (callMode == "SCHEDULE") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = scheduledDate,
                    onValueChange = onDateChange,
                    label = { Text("दिनांक", color = Color(0xFF94A3B8)) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B)
                    )
                )
                OutlinedTextField(
                    value = scheduledTime,
                    onValueChange = onTimeChange,
                    label = { Text("वेळ", color = Color(0xFF94A3B8)) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B)
                    )
                )
            }
        }

        // Proceed to Preview Button
        Button(
            onClick = onGoToPreview,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isMarathi) "कॉल पूर्वदृश्य व आवाज चाचणी (Preview)" else "Proceed to Call Preview",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TemplateChip(label: String, onClick: () -> Unit) {
    Surface(
        color = Color(0xFF1E293B),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF38BDF8),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

// ================= TAB 3: PREVIEW & TEST =================

@Composable
fun AICallPreviewView(
    isMarathi: Boolean,
    panchayatName: String,
    title: String,
    message: String,
    voice: AiVoiceType,
    targetGroup: AiTargetGroup,
    callMode: String,
    scheduledDate: String,
    scheduledTime: String,
    isVoicePlaying: Boolean,
    onPlayPreview: () -> Unit,
    onStopPreview: () -> Unit,
    onStartCampaignNow: () -> Unit,
    onConfirmSchedule: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Announcement Voice Script Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📜 AI व्हॉईस स्क्रिप्ट (अंतिम स्वरूप)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Surface(
                        color = Color(0xFF0369A1),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = voice.nameMr,
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📞 \"नमस्कार! आदर्श ग्रामपंचायत पळसखेड दौलत यांच्या वतीने अधिकृत प्रशासकीय सूचना:\"",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "विषय: $title",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = message,
                            fontSize = 13.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ℹ️ \"(टीप: हा कॉल केवळ अधिकृत माहितीसाठी आहे. कॉलवर कोणतीही तक्रार नोंदवली जात नाही. तक्रारीसाठी अधिकृत पोर्टल वापरावे.)\"",
                            fontSize = 11.sp,
                            color = Color(0xFFFBBF24)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // TTS Play/Stop Button
                Button(
                    onClick = {
                        if (isVoicePlaying) onStopPreview() else onPlayPreview()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isVoicePlaying) Color(0xFFDC2626) else Color(0xFF0284C7)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isVoicePlaying) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVoicePlaying) "आवाज थांबवा (Stop Voice)" else "AI आवाज ऐका (Listen Voice Preview)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Audience & Batch Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "🎯 कॉल वितरण तपशील",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "लक्ष्य गट:", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    Text(text = targetGroup.labelMr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "अंदाजे नागरिक संख्या:", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    Text(text = "${targetGroup.estimatedCitizens} कुटुंब / नागरिक", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "अंदाजे कॉल कालावधी:", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    Text(text = "~४५ सेकंद प्रति कॉल", fontSize = 12.sp, color = Color.White)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "मोहीम प्रकार:", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    Text(
                        text = if (callMode == "NOW") "तात्काळ कॉल (Call Now)" else "नियोजित ($scheduledDate $scheduledTime)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (callMode == "NOW") Color(0xFF38BDF8) else Color(0xFFA78BFA)
                    )
                }
            }
        }

        // Final Confirm Button
        if (callMode == "NOW") {
            Button(
                onClick = onStartCampaignNow,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.PhoneCallback, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🚀 कॉल मोहीम आत्ताच सुरू करा (Start Calls)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Button(
                onClick = onConfirmSchedule,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "📅 $scheduledDate साठी कॉल मोहीम शेड्यूल करा",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ================= TAB 4: CALL HISTORY =================

@Composable
fun AICallHistoryView(
    isMarathi: Boolean,
    callLogs: List<AiCallLog>,
    onLogClick: (AiCallLog) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf("ALL") }

    val filteredLogs = callLogs.filter { log ->
        val matchesQuery = searchQuery.isBlank() ||
                log.citizenName.contains(searchQuery, ignoreCase = true) ||
                log.mobileNumber.contains(searchQuery) ||
                log.announcementTitle.contains(searchQuery, ignoreCase = true)
        val matchesStatus = statusFilter == "ALL" || log.status == statusFilter
        matchesQuery && matchesStatus
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("नागरिकाचे नाव किंवा मोबाईल नंबर शोधा...", color = Color(0xFF64748B)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            ),
            shape = RoundedCornerShape(10.dp)
        )

        // Status Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            val filters = listOf(
                "ALL" to "सर्व (${callLogs.size})",
                "CONNECTED" to "जोडले (${callLogs.count { it.status == "CONNECTED" }})",
                "NOT_ANSWERED" to "उत्तर नाही (${callLogs.count { it.status == "NOT_ANSWERED" }})",
                "BUSY" to "व्यस्त (${callLogs.count { it.status == "BUSY" }})",
                "FAILED" to "अयशस्वी (${callLogs.count { it.status == "FAILED" }})"
            )
            items(filters) { (key, label) ->
                val isSelected = statusFilter == key
                Surface(
                    color = if (isSelected) Color(0xFF0284C7) else Color(0xFF1E293B),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155)),
                    modifier = Modifier.clickable { statusFilter = key }
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = if (isSelected) Color.White else Color(0xFF94A3B8),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Call Logs List
        if (filteredLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "कोणतेही कॉल रेकॉर्ड आढळले नाही.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredLogs) { log ->
                    CallLogItemCard(log = log, isMarathi = isMarathi, onClick = { onLogClick(log) })
                }
            }
        }
    }
}

@Composable
fun CallLogItemCard(log: AiCallLog, isMarathi: Boolean, onClick: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val dateStr = dateFormat.format(Date(log.timestamp))

    val statusColor = when (log.status) {
        "CONNECTED" -> Color(0xFF4ADE80)
        "NOT_ANSWERED" -> Color(0xFFFBBF24)
        "BUSY" -> Color(0xFFF97316)
        "FAILED" -> Color(0xFFF87171)
        else -> Color(0xFF94A3B8)
    }

    val statusBg = when (log.status) {
        "CONNECTED" -> Color(0xFF14532D)
        "NOT_ANSWERED" -> Color(0xFF78350F)
        "BUSY" -> Color(0xFF7C2D12)
        "FAILED" -> Color(0xFF7F1D1D)
        else -> Color(0xFF1E293B)
    }

    val statusLabel = when (log.status) {
        "CONNECTED" -> "जोडले (${log.durationSec}s)"
        "NOT_ANSWERED" -> "उत्तर नाही"
        "BUSY" -> "व्यस्त"
        "FAILED" -> "अयशस्वी"
        else -> log.status
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color(0xFF334155))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(statusBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (log.status) {
                        "CONNECTED" -> Icons.Default.PhoneCallback
                        "NOT_ANSWERED" -> Icons.Default.PhoneMissed
                        "BUSY" -> Icons.Default.PhonePaused
                        else -> Icons.Default.PhoneDisabled
                    },
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = log.citizenName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Surface(
                        color = statusBg,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${log.mobileNumber} • प्रभाग ${log.wardNumber}",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "📢 ${log.announcementTitle}",
                    fontSize = 11.sp,
                    color = Color(0xFF38BDF8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = dateStr,
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

// ================= TAB 5: SCHEDULED CALLS =================

@Composable
fun AICallScheduledView(
    isMarathi: Boolean,
    scheduledCalls: List<AiScheduledCall>,
    onCancel: (String) -> Unit,
    onAddNew: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isMarathi) "📅 आगामी नियोजित कॉल्स" else "Upcoming Scheduled Calls",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Button(
                onClick = onAddNew,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = if (isMarathi) "नवीन शेड्यूल" else "Schedule New", fontSize = 12.sp)
            }
        }

        if (scheduledCalls.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "कोणतेही नियोजित कॉल्स नाहीत.", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(scheduledCalls) { sched ->
                    ScheduledCallCard(sched = sched, isMarathi = isMarathi, onCancel = { onCancel(sched.id) })
                }
            }
        }
    }
}

@Composable
fun ScheduledCallCard(sched: AiScheduledCall, isMarathi: Boolean, onCancel: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF7C3AED).copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sched.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = Color(0xFF581C87),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "नियोजित (PENDING)",
                        fontSize = 10.sp,
                        color = Color(0xFFD8B4FE),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = sched.message,
                fontSize = 12.sp,
                color = Color(0xFFCBD5E1),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "⏰ ${sched.scheduledDate} • ${sched.scheduledTime}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                    Text(
                        text = "🎯 ${sched.targetAudienceLabel} (~${sched.recipientCount} नागरिक)",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
                OutlinedButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                    border = BorderStroke(1.dp, Color(0xFFF87171).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = "रद्द करा", fontSize = 11.sp)
                }
            }
        }
    }
}

// ================= TAB 6: SETTINGS =================

@Composable
fun AICallSettingsView(
    isMarathi: Boolean,
    settings: AiCallSettings,
    onSave: (AiCallSettings) -> Unit
) {
    var isEnabled by remember { mutableStateOf(settings.isAiCallingEnabled) }
    var isDemoMode by remember { mutableStateOf(settings.isDemoMode) }
    var hoursStart by remember { mutableStateOf(settings.callingHoursStart) }
    var hoursEnd by remember { mutableStateOf(settings.callingHoursEnd) }
    var maxCallsPerBatch by remember { mutableStateOf(settings.maxCallsPerBatch.toString()) }
    var maxRetries by remember { mutableStateOf(settings.maxRetries.toString()) }
    var webhookUrl by remember { mutableStateOf(settings.webhookEndpointUrl) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "⚙️ AI कॉल प्रणाली सेटिंग्ज",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Master Switch
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "AI आउटगोइंग कॉल सेवा", fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "ग्रामपंचायत नागरिकांना स्वयंचलित कॉल्स पाठवणे सक्षम करा", fontSize = 11.sp, color = Color(0xFF94A3B8))
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { isEnabled = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF38BDF8))
                )
            }
        }

        // Demo vs Real Mode Toggle
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isDemoMode) "डेमो / सिम्युलेशन मोड सक्रिय" else "लाईव्ह टेलिफोनी मोड सक्रिय",
                        fontWeight = FontWeight.Bold,
                        color = if (isDemoMode) Color(0xFFFBBF24) else Color(0xFF4ADE80)
                    )
                    Text(
                        text = if (isDemoMode) "सुरक्षित चाचणी मोड (टेलिफोनी क्रेडिट खर्च होत नाही)" else "प्रत्यक्ष नागरिकांच्या फोनवर कॉल जाईल",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
                Switch(
                    checked = isDemoMode,
                    onCheckedChange = { isDemoMode = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFBBF24))
                )
            }
        }

        // Calling Hours
        Text(text = "⏰ कॉल वेळ मर्यादा (TRAI नियमावली)", fontWeight = FontWeight.Bold, color = Color.White)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = hoursStart,
                onValueChange = { hoursStart = it },
                label = { Text("सुरुवात वेळ", color = Color(0xFF94A3B8)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B)
                )
            )
            OutlinedTextField(
                value = hoursEnd,
                onValueChange = { hoursEnd = it },
                label = { Text("समाप्ती वेळ", color = Color(0xFF94A3B8)) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B)
                )
            )
        }

        // Webhook URL
        OutlinedTextField(
            value = webhookUrl,
            onValueChange = { webhookUrl = it },
            label = { Text("Telephony Webhook Endpoint URL", color = Color(0xFF94A3B8)) },
            placeholder = { Text("https://api.telephony.gov.in/v1/voice/outbound", color = Color(0xFF64748B)) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            )
        )

        Button(
            onClick = {
                val updated = settings.copy(
                    isAiCallingEnabled = isEnabled,
                    isDemoMode = isDemoMode,
                    callingHoursStart = hoursStart,
                    callingHoursEnd = hoursEnd,
                    maxCallsPerBatch = maxCallsPerBatch.toIntOrNull() ?: 50,
                    maxRetries = maxRetries.toIntOrNull() ?: 2,
                    webhookEndpointUrl = webhookUrl
                )
                onSave(updated)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "सेटिंग्ज सेव्ह करा (Save Settings)", fontWeight = FontWeight.Bold)
        }
    }
}

// ================= LIVE CALL BATCH PROGRESS DIALOG =================

@Composable
fun LiveCallBatchDialog(
    isMarathi: Boolean,
    progress: Pair<Int, Int>,
    currentCitizen: String,
    voiceName: String
) {
    val completed = progress.first
    val total = progress.second
    val progressFraction = if (total > 0) completed.toFloat() / total.toFloat() else 0f

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF38BDF8))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF0284C7).copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneCallback,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "🤖 AI कॉल मोहीम सुरू आहे...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "व्हॉईस स्वर: $voiceName",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF38BDF8),
                    trackColor = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "प्रगती: $completed / $total नागरिक", fontSize = 12.sp, color = Color(0xFFCBD5E1))
                    Text(text = "${(progressFraction * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8))
                }

                if (currentCitizen.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color(0xFF4ADE80),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "डायल करत आहे: $currentCitizen",
                                fontSize = 12.sp,
                                color = Color(0xFF4ADE80),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ================= CALL LOG DETAIL DIALOG =================

@Composable
fun CallLogDetailDialog(
    log: AiCallLog,
    isMarathi: Boolean,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy, hh:mm:ss a", Locale.getDefault()) }
    val dateStr = dateFormat.format(Date(log.timestamp))

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📞 कॉल तपशील",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                HorizontalDivider(color = Color(0xFF334155))

                DetailRow(label = "नागरिकाचे नाव:", value = log.citizenName)
                DetailRow(label = "मोबाईल नंबर:", value = log.mobileNumber)
                DetailRow(label = "प्रभाग क्र.:", value = "प्रभाग ${log.wardNumber}")
                DetailRow(label = "दिनांक व वेळ:", value = dateStr)
                DetailRow(label = "कॉल स्थिती:", value = log.status)
                DetailRow(label = "कॉल कालावधी:", value = "${log.durationSec} सेकंद")
                DetailRow(label = "वापरलेला AI स्वर:", value = log.aiVoiceUsed)

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "📢 वाचली गेलेली घोषणा:", fontWeight = FontWeight.Bold, color = Color(0xFF38BDF8), fontSize = 12.sp)
                Surface(
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(text = log.announcementTitle, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = log.announcementMessage, color = Color(0xFFCBD5E1), fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "📝 AI इंटरॅक्शन नोट्स:", fontWeight = FontWeight.Bold, color = Color(0xFFFBBF24), fontSize = 12.sp)
                Surface(
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = log.notes.ifBlank { "घोषणा सुरळीत पूर्ण झाली." },
                        color = Color(0xFFE2E8F0),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "बंद करा (Close)")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF94A3B8))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
