package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppLanguage
import com.example.data.notification.NotificationHelper
import com.example.ui.screens.*
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.theme.GrampanchayatTheme
import com.example.ui.viewmodel.GrampanchayatViewModel
import com.example.ui.viewmodel.ScreenDestination
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var mainViewModel: GrampanchayatViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: GrampanchayatViewModel = viewModel()
            mainViewModel = viewModel

            val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
            ) { _ -> }

            androidx.compose.runtime.LaunchedEffect(Unit) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
                handleNotificationIntent(intent, viewModel)
            }

            GrampanchayatTheme {
                GrampanchayatApp(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        mainViewModel?.let { handleNotificationIntent(intent, it) }
    }

    private fun handleNotificationIntent(intent: Intent?, viewModel: GrampanchayatViewModel) {
        val targetScreen = intent?.getStringExtra(NotificationHelper.EXTRA_TARGET_SCREEN)
        val targetId = intent?.getStringExtra(NotificationHelper.EXTRA_TARGET_ID)
        val wardNum = intent?.getIntExtra(NotificationHelper.EXTRA_WARD_NUMBER, -1)
        if (!targetScreen.isNullOrBlank()) {
            viewModel.handleNotificationNavigation(
                targetScreen = targetScreen,
                targetId = targetId,
                wardNumber = if (wardNum != null && wardNum >= 0) wardNum else null
            )
        }
    }
}

@Composable
fun GrampanchayatApp(viewModel: GrampanchayatViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val complaints by viewModel.complaints.collectAsStateWithLifecycle()
    val notices by viewModel.notices.collectAsStateWithLifecycle()
    val waterSchedules by viewModel.waterSchedules.collectAsStateWithLifecycle()
    val tankerBookings by viewModel.tankerBookings.collectAsStateWithLifecycle()
    val serviceApplications by viewModel.serviceApplications.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val officials by viewModel.officialContacts.collectAsStateWithLifecycle()
    val projects by viewModel.developmentProjects.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    // Citizen Auth state flows
    val isCitizenAuthLoading by viewModel.isCitizenAuthLoading.collectAsStateWithLifecycle()
    val citizenAuthError by viewModel.citizenAuthError.collectAsStateWithLifecycle()
    val selectedDistrictId by viewModel.selectedDistrictId.collectAsStateWithLifecycle()
    val selectedTalukaId by viewModel.selectedTalukaId.collectAsStateWithLifecycle()
    val selectedGramPanchayatId by viewModel.selectedGramPanchayatId.collectAsStateWithLifecycle()

    // Admin state flows
    val adminUser by viewModel.adminUser.collectAsStateWithLifecycle()
    val isAdminLoading by viewModel.isAdminLoading.collectAsStateWithLifecycle()
    val adminLoginError by viewModel.adminLoginError.collectAsStateWithLifecycle()
    val panchayatProfile by viewModel.panchayatProfile.collectAsStateWithLifecycle()
    val wards by viewModel.wards.collectAsStateWithLifecycle()
    val onlineServices by viewModel.onlineServices.collectAsStateWithLifecycle()
    val citizens by viewModel.citizens.collectAsStateWithLifecycle()

    // AI Call System state flows
    val aiCampaigns by viewModel.aiCampaigns.collectAsStateWithLifecycle()
    val aiCallLogs by viewModel.aiCallLogs.collectAsStateWithLifecycle()
    val aiScheduledCalls by viewModel.aiScheduledCalls.collectAsStateWithLifecycle()
    val aiCallSettings by viewModel.aiCallSettings.collectAsStateWithLifecycle()
    val isAiBatchCallingActive by viewModel.isAiBatchCallingActive.collectAsStateWithLifecycle()
    val aiBatchProgress by viewModel.aiBatchProgress.collectAsStateWithLifecycle()
    val currentCallingCitizen by viewModel.currentCallingCitizenName.collectAsStateWithLifecycle()
    val isAiPreviewPlaying by viewModel.isAiPreviewPlaying.collectAsStateWithLifecycle()

    val complaintFilter by viewModel.complaintFilter.collectAsStateWithLifecycle()
    val noticeFilter by viewModel.noticeCategoryFilter.collectAsStateWithLifecycle()
    val draftState by viewModel.complaintDraftState.collectAsStateWithLifecycle()
    val showNewComplaintDialog by viewModel.showNewComplaintDialog.collectAsStateWithLifecycle()
    val selectedComplaintForDetail by viewModel.selectedComplaintForDetail.collectAsStateWithLifecycle()
    val selectedAppForCertificate by viewModel.selectedApplicationForCertificate.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()

    // Back handler: if on sub-screens, navigate back to HOME (or AUTH if not logged in)
    BackHandler(enabled = currentScreen != ScreenDestination.HOME && currentScreen != ScreenDestination.SPLASH && currentScreen != ScreenDestination.AUTH) {
        if (userProfile?.isRegistered == true && !userProfile?.fullName.isNullOrBlank()) {
            viewModel.navigateTo(ScreenDestination.HOME)
        } else {
            viewModel.navigateTo(ScreenDestination.AUTH)
        }
    }

    val showBottomBar = currentScreen in listOf(
        ScreenDestination.HOME,
        ScreenDestination.COMPLAINTS,
        ScreenDestination.AI_ASSISTANT,
        ScreenDestination.NOTICES,
        ScreenDestination.PROFILE
    ) && (userProfile?.isRegistered == true && !userProfile?.fullName.isNullOrBlank())

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("app_bottom_bar")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == ScreenDestination.HOME,
                        onClick = { viewModel.navigateTo(ScreenDestination.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text(if (language == AppLanguage.MARATHI) "मुख्य" else "Home") },
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen == ScreenDestination.COMPLAINTS,
                        onClick = { viewModel.navigateTo(ScreenDestination.COMPLAINTS) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    val pendingCount = complaints.count { it.status == "PENDING" }
                                    if (pendingCount > 0) {
                                        Badge { Text("$pendingCount") }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ReportProblem, contentDescription = "Complaints")
                            }
                        },
                        label = { Text(if (language == AppLanguage.MARATHI) "तक्रार" else "Grievance") },
                        modifier = Modifier.testTag("nav_item_complaints")
                    )

                    NavigationBarItem(
                        selected = currentScreen == ScreenDestination.AI_ASSISTANT,
                        onClick = { viewModel.navigateTo(ScreenDestination.AI_ASSISTANT) },
                        icon = { Icon(Icons.Default.Psychology, contentDescription = "AI Assistant") },
                        label = { Text(if (language == AppLanguage.MARATHI) "AI मित्र" else "AI Help") },
                        modifier = Modifier.testTag("nav_item_ai")
                    )

                    NavigationBarItem(
                        selected = currentScreen == ScreenDestination.NOTICES,
                        onClick = { viewModel.navigateTo(ScreenDestination.NOTICES) },
                        icon = { Icon(Icons.Default.Campaign, contentDescription = "Notices") },
                        label = { Text(if (language == AppLanguage.MARATHI) "सूचना" else "Notices") },
                        modifier = Modifier.testTag("nav_item_notices")
                    )

                    NavigationBarItem(
                        selected = currentScreen == ScreenDestination.PROFILE,
                        onClick = { viewModel.navigateTo(ScreenDestination.PROFILE) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text(if (language == AppLanguage.MARATHI) "माहिती" else "Profile") },
                        modifier = Modifier.testTag("nav_item_profile")
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp)) {
            Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
                when (screen) {
                    ScreenDestination.SPLASH -> {
                        SplashScreen(
                            language = language,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onNavigateNext = {
                                if (userProfile?.isRegistered == true && !userProfile?.fullName.isNullOrBlank()) {
                                    viewModel.navigateTo(ScreenDestination.HOME)
                                } else {
                                    viewModel.navigateTo(ScreenDestination.AUTH)
                                }
                            }
                        )
                    }

                    ScreenDestination.AUTH -> {
                        AuthScreen(
                            currentProfile = userProfile,
                            language = language,
                            isLoading = isCitizenAuthLoading,
                            errorMessage = citizenAuthError,
                            initialDistrictId = selectedDistrictId,
                            initialTalukaId = selectedTalukaId,
                            initialGramPanchayatId = selectedGramPanchayatId,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onLogin = { mobile, gpId, pass ->
                                viewModel.loginCitizen(mobile, gpId, pass) { success ->
                                    if (success) {
                                        viewModel.navigateTo(ScreenDestination.HOME)
                                    }
                                }
                            },
                            onRegister = { fullName, districtId, talukaId, gpId, ward, mobile, otp, expectedOtp, pass ->
                                viewModel.registerCitizen(
                                    fullName = fullName,
                                    districtId = districtId,
                                    talukaId = talukaId,
                                    gramPanchayatId = gpId,
                                    ward = ward,
                                    mobile = mobile,
                                    otp = otp,
                                    expectedOtp = expectedOtp,
                                    password = pass
                                ) { success ->
                                    if (success) {
                                        viewModel.navigateTo(ScreenDestination.HOME)
                                    }
                                }
                            }
                        )
                    }

                    ScreenDestination.HOME -> {
                        HomeScreen(
                            profile = userProfile,
                            panchayatProfile = panchayatProfile,
                            language = language,
                            waterSchedules = waterSchedules,
                            complaints = complaints,
                            notices = notices,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onNavigate = { dest -> viewModel.navigateTo(dest) },
                            onOpenNewComplaint = { viewModel.openNewComplaintDialog() },
                            onOpenComplaintDetail = { comp -> viewModel.selectComplaintForDetail(comp) }
                        )
                    }

                    ScreenDestination.COMPLAINTS -> {
                        ComplaintsScreen(
                            complaints = complaints,
                            language = language,
                            currentFilter = complaintFilter,
                            draftState = draftState,
                            showNewDialog = showNewComplaintDialog,
                            selectedComplaintForDetail = selectedComplaintForDetail,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onFilterChange = { filter -> viewModel.setComplaintFilter(filter) },
                            onOpenNewForm = { viewModel.openNewComplaintDialog() },
                            onCloseNewForm = { viewModel.closeNewComplaintDialog() },
                            onSelectComplaint = { comp -> viewModel.selectComplaintForDetail(comp) },
                            onSubmitComplaint = { title, cat, desc, ward, loc, photo ->
                                viewModel.submitComplaint(title, cat, desc, ward, loc, photo)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (language == AppLanguage.MARATHI)
                                            "तक्रार यशस्वीरित्या दाखल झाली!"
                                        else
                                            "Grievance submitted successfully!"
                                    )
                                }
                            },
                            onSubmitRating = { id, rating, feedback ->
                                viewModel.submitRating(id, rating, feedback)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (language == AppLanguage.MARATHI)
                                            "आपला अभिप्राय सेव्ह झाला. धन्यवाद!"
                                        else
                                            "Feedback saved. Thank you!"
                                    )
                                }
                            }
                        )
                    }

                    ScreenDestination.AI_ASSISTANT -> {
                        AiAssistantScreen(
                            messages = chatMessages,
                            language = language,
                            isThinking = isAiThinking,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onSendMessage = { text -> viewModel.sendChatMessage(text) },
                            onClearChat = { viewModel.clearChat() },
                            onCopyDraftToComplaint = { title, cat, desc, ward ->
                                viewModel.openNewComplaintFromAiDraft(title, cat, desc, ward)
                            }
                        )
                    }

                    ScreenDestination.NOTICES -> {
                        NoticeBoardScreen(
                            notices = notices,
                            language = language,
                            categoryFilter = noticeFilter,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onFilterChange = { cat -> viewModel.setNoticeCategoryFilter(cat) },
                            onShowToast = { msg ->
                                scope.launch { snackbarHostState.showSnackbar(msg) }
                            }
                        )
                    }

                    ScreenDestination.WATER_SERVICES -> {
                        WaterServicesScreen(
                            waterSchedules = waterSchedules,
                            tankerBookings = tankerBookings,
                            userProfile = userProfile,
                            language = language,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) },
                            onBookTanker = { name, mob, ward, addr, date, slot, purpose ->
                                viewModel.bookTanker(name, mob, ward, addr, date, slot, purpose)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (language == AppLanguage.MARATHI)
                                            "पाणी टँकर बुकिंग यशस्वी!"
                                        else
                                            "Water Tanker booked successfully!"
                                    )
                                }
                            }
                        )
                    }

                    ScreenDestination.ONLINE_SERVICES -> {
                        OnlineServicesScreen(
                            applications = serviceApplications,
                            userProfile = userProfile,
                            panchayatProfile = panchayatProfile,
                            servicesList = onlineServices,
                            language = language,
                            selectedApplicationForCertificate = selectedAppForCertificate,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) },
                            onOpenCertificate = { app -> viewModel.selectApplicationForCertificate(app) },
                            onSubmitApplication = { type, name, mob, ward, details, fee ->
                                viewModel.applyForService(type, name, mob, ward, details, fee)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (language == AppLanguage.MARATHI)
                                            "अर्ज / कर भरणा यशस्वी!"
                                        else
                                            "Application / Tax payment successful!"
                                    )
                                }
                            },
                            onShowToast = { msg ->
                                scope.launch { snackbarHostState.showSnackbar(msg) }
                            }
                        )
                    }

                    ScreenDestination.GP_INFO -> {
                        GrampanchayatInfoScreen(
                            userProfile = userProfile,
                            panchayatProfile = panchayatProfile,
                            wards = wards,
                            officials = officials,
                            projects = projects,
                            language = language,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.NOTIFICATIONS -> {
                        NotificationsScreen(
                            notifications = notifications,
                            language = language,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onBack = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.PROFILE -> {
                        ProfileScreen(
                            userProfile = userProfile,
                            language = language,
                            complaintCount = complaints.size,
                            certificateCount = serviceApplications.size,
                            tankerCount = tankerBookings.size,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onNavigateToAdminLogin = { viewModel.navigateTo(ScreenDestination.ADMIN_LOGIN) },
                            onUpdateProfile = { updated ->
                                viewModel.updateProfile(updated)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (language == AppLanguage.MARATHI) "माहिती अद्ययावत केली!" else "Profile updated!"
                                    )
                                }
                            },
                            onLogout = {
                                viewModel.logout()
                            }
                        )
                    }

                    ScreenDestination.ADMIN_LOGIN -> {
                        AdminLoginScreen(
                            language = language,
                            isLoading = isAdminLoading,
                            errorMessage = adminLoginError,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onLogin = { email, pass ->
                                viewModel.loginAdmin(email, pass) { success ->
                                    if (success) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                if (language == AppLanguage.MARATHI) "प्रशासक लॉगिन यशस्वी!" else "Admin login successful!"
                                            )
                                        }
                                    }
                                }
                            },
                            onResetPassword = { resetEmail ->
                                viewModel.resetAdminPassword(resetEmail) { success, msg ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(msg)
                                    }
                                }
                            },
                            onBackToCitizenApp = { viewModel.navigateTo(ScreenDestination.HOME) }
                        )
                    }

                    ScreenDestination.ADMIN_DASHBOARD -> {
                        AdminDashboardScreen(
                            language = language,
                            adminUser = adminUser,
                            panchayatProfile = panchayatProfile,
                            wards = wards,
                            waterSchedules = waterSchedules,
                            notices = notices,
                            complaints = complaints,
                            officials = officials,
                            projects = projects,
                            services = onlineServices,
                            notifications = notifications,
                            citizens = citizens,
                            aiCampaigns = aiCampaigns,
                            aiCallLogs = aiCallLogs,
                            aiScheduledCalls = aiScheduledCalls,
                            aiCallSettings = aiCallSettings,
                            isBatchCallingActive = isAiBatchCallingActive,
                            batchProgress = aiBatchProgress,
                            currentCallingCitizen = currentCallingCitizen,
                            isVoicePlaying = isAiPreviewPlaying,
                            isLoading = isAdminLoading,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onLogout = { viewModel.logoutAdmin() },
                            onSavePanchayatProfile = { profile -> viewModel.adminSavePanchayatProfile(profile) },
                            onSaveWard = { ward -> viewModel.adminSaveWard(ward) },
                            onDeleteWard = { id -> viewModel.adminDeleteWard(id) },
                            onSaveWaterSchedule = { sched -> viewModel.adminSaveWaterSchedule(sched) },
                            onDeleteWaterSchedule = { wardNum -> viewModel.adminDeleteWaterSchedule(wardNum) },
                            onSaveNotice = { notice -> viewModel.adminSaveNotice(notice) },
                            onDeleteNotice = { id -> viewModel.adminDeleteNotice(id) },
                            onUpdateComplaint = { id, status, remarks, officer ->
                                viewModel.adminUpdateComplaint(id, status, remarks, officer)
                            },
                            onDeleteComplaint = { id -> viewModel.adminDeleteComplaint(id) },
                            onSaveOfficial = { off -> viewModel.adminSaveOfficial(off) },
                            onDeleteOfficial = { id -> viewModel.adminDeleteOfficial(id) },
                            onSaveProject = { proj -> viewModel.adminSaveProject(proj) },
                            onDeleteProject = { id -> viewModel.adminDeleteProject(id) },
                            onSaveService = { srv -> viewModel.adminSaveOnlineService(srv) },
                            onDeleteService = { id -> viewModel.adminDeleteOnlineService(id) },
                            onSaveNotification = { notif -> viewModel.adminSaveNotification(notif) },
                            onDeleteNotification = { id -> viewModel.adminDeleteNotification(id) },
                            onSaveCitizen = { citizen -> viewModel.adminSaveCitizen(citizen) },
                            onSeedFirestore = { viewModel.adminSeedFirestore() },
                            onPreviewVoice = { text, lang, voiceId -> viewModel.previewAiVoice(text, lang, voiceId) },
                            onStopPreviewVoice = { viewModel.stopAiVoicePreview() },
                            onStartCampaign = { title, msg, lang, voiceId, target ->
                                viewModel.startAiCallCampaign(title, msg, lang, voiceId, target)
                            },
                            onScheduleCall = { title, msg, lang, voiceId, target, date, time, ts ->
                                viewModel.scheduleAiCall(title, msg, lang, voiceId, target, date, time, ts)
                            },
                            onCancelScheduledCall = { id -> viewModel.cancelScheduledAiCall(id) },
                            onSaveAiSettings = { settings -> viewModel.saveAiCallSettings(settings) }
                        )
                    }
                }
            }
        }
    }
}
