package com.example.data.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.data.local.*
import com.example.data.notification.NotificationHelper
import com.example.data.remote.AiAssistanceResult
import com.example.data.remote.FirestoreDataSource
import com.example.data.remote.GeminiGrampanchayatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GrampanchayatRepository(context: Context) {
    private val TAG = "GrampanchayatRepo"
    private val appContext = context.applicationContext
    private val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "grampanchayat_citizen.db"
    ).fallbackToDestructiveMigration().build()

    private val userDao = database.userDao()
    private val complaintDao = database.complaintDao()
    private val noticeDao = database.noticeDao()
    private val waterDao = database.waterDao()
    private val serviceDao = database.serviceDao()
    private val chatDao = database.chatDao()

    val firestoreSource = FirestoreDataSource()
    val authRepository = AuthRepository(context.applicationContext)
    private val scope = CoroutineScope(Dispatchers.IO)

    // In-memory / reactive flows for remote-synced entities
    private val _panchayatProfile = MutableStateFlow(InitialData.initialPanchayatProfile)
    val panchayatProfile: StateFlow<PanchayatProfile> = _panchayatProfile.asStateFlow()

    private val _wards = MutableStateFlow<List<WardEntity>>(InitialData.initialWards)
    val wards: StateFlow<List<WardEntity>> = _wards.asStateFlow()

    private val _officials = MutableStateFlow<List<OfficialContactEntity>>(InitialData.officials)
    val officials: StateFlow<List<OfficialContactEntity>> = _officials.asStateFlow()

    private val _developmentProjects = MutableStateFlow<List<DevelopmentProjectEntity>>(InitialData.developmentProjects)
    val developmentProjects: StateFlow<List<DevelopmentProjectEntity>> = _developmentProjects.asStateFlow()

    private val _onlineServices = MutableStateFlow<List<OnlineServiceItem>>(InitialData.initialServices)
    val onlineServices: StateFlow<List<OnlineServiceItem>> = _onlineServices.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(InitialData.initialNotifications)
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _citizens = MutableStateFlow<List<UserProfile>>(emptyList())
    val citizens: StateFlow<List<UserProfile>> = _citizens.asStateFlow()

    // AI Call System StateFlows
    val aiCallService = com.example.data.remote.AICallService(context.applicationContext)

    private val _aiCampaigns = MutableStateFlow<List<AiCallCampaign>>(InitialData.initialAiCampaigns)
    val aiCampaigns: StateFlow<List<AiCallCampaign>> = _aiCampaigns.asStateFlow()

    private val _aiCallLogs = MutableStateFlow<List<AiCallLog>>(InitialData.initialAiCallLogs)
    val aiCallLogs: StateFlow<List<AiCallLog>> = _aiCallLogs.asStateFlow()

    private val _aiScheduledCalls = MutableStateFlow<List<AiScheduledCall>>(InitialData.initialAiScheduledCalls)
    val aiScheduledCalls: StateFlow<List<AiScheduledCall>> = _aiScheduledCalls.asStateFlow()

    private val _aiCallSettings = MutableStateFlow<AiCallSettings>(InitialData.initialAiCallSettings)
    val aiCallSettings: StateFlow<AiCallSettings> = _aiCallSettings.asStateFlow()

    init {
        scope.launch {
            seedLocalDataIfNeeded()
            startFirestoreSync()
        }
    }

    private suspend fun seedLocalDataIfNeeded() {
        if (complaintDao.getComplaintCount() == 0) {
            complaintDao.insertAllComplaints(InitialData.initialComplaints)
            noticeDao.insertNotices(InitialData.initialNotices)
            waterDao.insertSchedules(InitialData.initialWaterSchedules)
            serviceDao.insertAllApplications(InitialData.initialApplications)

            chatDao.insertMessage(
                ChatMessageEntity(
                    text = "नमस्कार! मी ग्राममित्र AI सहाय्यक आहे. 🙏\nआपल्या ग्रामपंचायतीशी संबंधित कोणत्याही तक्रारी, दाखले, पाणीपुरवठा किंवा विकासकामांबाबत मला विचारा.",
                    isUser = false
                )
            )
        }

        // Enforce Firebase Authentication Check:
        val currentUser = authRepository.getCurrentFirebaseUser()
        if (currentUser == null) {
            // Unauthenticated user -> ensure local profile is cleared
            userDao.clearUserProfile()
        } else {
            // Authenticated user -> load their specific profile from Firestore
            val uid = currentUser.uid
            val remoteProfile = firestoreSource.getCitizen(uid)
            if (remoteProfile != null) {
                userDao.insertOrUpdateProfile(remoteProfile)
            } else {
                val localUser = userDao.getUserProfileDirect()
                if (localUser != null && localUser.id == uid && localUser.isRegistered) {
                    // Local profile matches current auth UID
                } else {
                    userDao.clearUserProfile()
                }
            }
        }
    }

    private fun startFirestoreSync() {
        // 1. Sync Panchayat Profile
        scope.launch {
            firestoreSource.getPanchayatProfileFlow().collect { profile ->
                if (profile != null) {
                    _panchayatProfile.value = profile
                    // Also sync name to local userProfile if present
                    val localUser = userDao.getUserProfileDirect()
                    if (localUser != null) {
                        userDao.insertOrUpdateProfile(
                            localUser.copy(
                                grampanchayatNameMr = profile.nameMr,
                                grampanchayatNameEn = profile.nameEn,
                                talukaMr = profile.talukaMr,
                                talukaEn = profile.talukaEn,
                                districtMr = profile.districtMr,
                                districtEn = profile.districtEn
                            )
                        )
                    }
                }
            }
        }

        // 2. Sync Wards
        scope.launch {
            firestoreSource.getWardsFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _wards.value = list
                }
            }
        }

        // 3. Sync Water Schedules -> update Room cache
        scope.launch {
            firestoreSource.getWaterSchedulesFlow().collect { schedules ->
                if (schedules.isNotEmpty()) {
                    waterDao.syncWaterSchedules(schedules)
                }
            }
        }

        // 4. Sync Notices -> update Room cache
        scope.launch {
            firestoreSource.getNoticesFlow().collect { noticesList ->
                if (noticesList.isNotEmpty()) {
                    noticeDao.syncNotices(noticesList)
                }
            }
        }

        // 5. Sync Complaints -> update Room cache
        scope.launch {
            firestoreSource.getComplaintsFlow().collect { complaintsList ->
                if (complaintsList.isNotEmpty()) {
                    complaintDao.syncComplaints(complaintsList)
                }
            }
        }

        // 6. Sync Officials
        scope.launch {
            firestoreSource.getOfficialsFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _officials.value = list
                }
            }
        }

        // 7. Sync Projects
        scope.launch {
            firestoreSource.getProjectsFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _developmentProjects.value = list
                }
            }
        }

        // 8. Sync Online Services
        scope.launch {
            firestoreSource.getServicesFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _onlineServices.value = list
                }
            }
        }

        // 9. Sync Notifications (Citizen-Facing Public Alerts)
        scope.launch {
            firestoreSource.getNotificationsFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _notifications.value = list
                    // Show notification for recent public alerts arriving while app is running/foreground
                    val latest = list.firstOrNull()
                    if (latest != null && (System.currentTimeMillis() - latest.createdAtEpoch) < 180_000) {
                        NotificationHelper.showPublicNotification(appContext, latest)
                    }
                }
            }
        }

        // 10. Sync Citizens (for admin)
        scope.launch {
            firestoreSource.getCitizensFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _citizens.value = list
                }
            }
        }

        // 11. Sync AI Call Campaigns
        scope.launch {
            firestoreSource.getAiCampaignsFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _aiCampaigns.value = list
                }
            }
        }

        // 12. Sync AI Call Logs
        scope.launch {
            firestoreSource.getAiCallLogsFlow().collect { list ->
                if (list.isNotEmpty()) {
                    _aiCallLogs.value = list
                }
            }
        }

        // 13. Sync AI Scheduled Calls
        scope.launch {
            firestoreSource.getAiScheduledCallsFlow().collect { list ->
                _aiScheduledCalls.value = list
            }
        }

        // 14. Sync AI Call Settings
        scope.launch {
            firestoreSource.getAiCallSettingsFlow().collect { settings ->
                if (settings != null) {
                    _aiCallSettings.value = settings
                }
            }
        }
    }

    // ================= CITIZEN APP OPERATIONS =================

    // User Profile & Authentication
    fun getUserProfile(): Flow<UserProfile?> = userDao.getUserProfile()

    fun isCitizenLoggedIn(): Boolean = authRepository.isCitizenLoggedIn()

    fun getCurrentCitizenUid(): String? = authRepository.getCurrentCitizenUid()

    suspend fun verifyCitizenGramPanchayat(mobileNumber: String): AuthRepository.CitizenVerificationRecord? {
        return authRepository.lookupCitizenVerificationRecord(mobileNumber)
    }

    suspend fun registerCitizen(
        fullName: String,
        districtId: String,
        talukaId: String,
        gramPanchayatId: String,
        wardNumber: Int,
        mobileNumber: String,
        otp: String = "123456",
        expectedOtp: String = "123456",
        password: String = "Citizen@123"
    ): Result<UserProfile> {
        val result = authRepository.registerCitizen(
            fullName = fullName,
            districtId = districtId,
            talukaId = talukaId,
            gramPanchayatId = gramPanchayatId,
            wardNumber = wardNumber,
            mobileNumber = mobileNumber,
            otp = otp,
            expectedOtp = expectedOtp,
            password = password
        )
        if (result.isSuccess) {
            val profile = result.getOrThrow()
            userDao.insertOrUpdateProfile(profile)
        }
        return result
    }

    suspend fun loginCitizen(
        mobileNumber: String,
        selectedGramPanchayatId: String,
        password: String = "Citizen@123"
    ): Result<UserProfile> {
        val result = authRepository.loginCitizen(
            mobileNumber = mobileNumber,
            selectedGramPanchayatId = selectedGramPanchayatId,
            password = password
        )
        if (result.isSuccess) {
            val profile = result.getOrThrow()
            userDao.insertOrUpdateProfile(profile)
        }
        return result
    }

    suspend fun logoutCitizen() {
        authRepository.signOutCitizen()
        userDao.clearUserProfile()
    }

    // ================= REAL FIREBASE PHONE AUTHENTICATION =================

    fun sendPhoneOtp(
        activity: android.app.Activity,
        phoneNumber: String,
        forceResendingToken: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken? = null,
        onCodeSent: (verificationId: String, token: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken) -> Unit,
        onVerificationCompleted: (credential: com.google.firebase.auth.PhoneAuthCredential) -> Unit,
        onVerificationFailed: (exception: Exception) -> Unit
    ) {
        authRepository.sendPhoneOtp(
            activity = activity,
            phoneNumber = phoneNumber,
            forceResendingToken = forceResendingToken,
            onCodeSent = onCodeSent,
            onVerificationCompleted = onVerificationCompleted,
            onVerificationFailed = onVerificationFailed
        )
    }

    suspend fun verifyOtpAndSignIn(
        verificationId: String,
        smsCode: String
    ): Result<com.google.firebase.auth.FirebaseUser> {
        return authRepository.verifyOtpAndSignIn(verificationId, smsCode)
    }

    suspend fun signInWithPhoneCredential(
        credential: com.google.firebase.auth.PhoneAuthCredential
    ): Result<com.google.firebase.auth.FirebaseUser> {
        return authRepository.signInWithPhoneCredential(credential)
    }

    suspend fun completeCitizenRegistrationWithFirebaseUser(
        firebaseUser: com.google.firebase.auth.FirebaseUser,
        fullName: String,
        districtId: String,
        talukaId: String,
        gramPanchayatId: String,
        wardNumber: Int,
        mobileNumber: String,
        password: String = "Citizen@123"
    ): Result<UserProfile> {
        val result = authRepository.completeCitizenRegistrationWithFirebaseUser(
            firebaseUser = firebaseUser,
            fullName = fullName,
            districtId = districtId,
            talukaId = talukaId,
            gramPanchayatId = gramPanchayatId,
            wardNumber = wardNumber,
            mobileNumber = mobileNumber,
            password = password
        )
        if (result.isSuccess) {
            val profile = result.getOrThrow()
            userDao.insertOrUpdateProfile(profile)
        }
        return result
    }

    suspend fun completeCitizenLoginWithFirebaseUser(
        firebaseUser: com.google.firebase.auth.FirebaseUser,
        mobileNumber: String,
        selectedGramPanchayatId: String
    ): Result<UserProfile> {
        val result = authRepository.completeCitizenLoginWithFirebaseUser(
            firebaseUser = firebaseUser,
            mobileNumber = mobileNumber,
            selectedGramPanchayatId = selectedGramPanchayatId
        )
        if (result.isSuccess) {
            val profile = result.getOrThrow()
            userDao.insertOrUpdateProfile(profile)
        }
        return result
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        userDao.insertOrUpdateProfile(profile)
        scope.launch {
            firestoreSource.saveCitizen(profile)
        }
    }

    // Complaints
    fun getAllComplaints(): Flow<List<ComplaintEntity>> = complaintDao.getAllComplaints()
    suspend fun createComplaint(complaint: ComplaintEntity) {
        complaintDao.insertComplaint(complaint)
        scope.launch {
            firestoreSource.saveComplaint(complaint)
        }
    }

    suspend fun updateComplaintRating(id: String, rating: Int, feedback: String) {
        complaintDao.updateRatingAndFeedback(id, rating, feedback)
        val comp = complaintDao.getAllComplaints().firstOrNull()?.find { it.id == id }
        if (comp != null) {
            val updated = comp.copy(rating = rating, citizenFeedback = feedback)
            scope.launch {
                firestoreSource.saveComplaint(updated)
            }
        }
    }

    // Notices
    fun getAllNotices(): Flow<List<NoticeEntity>> = noticeDao.getAllNotices()

    // Water
    fun getWaterSchedules(): Flow<List<WaterScheduleEntity>> = waterDao.getAllWaterSchedules()
    fun getTankerBookings(): Flow<List<TankerBookingEntity>> = waterDao.getAllTankerBookings()
    suspend fun bookWaterTanker(booking: TankerBookingEntity) = waterDao.insertTankerBooking(booking)

    // Services
    fun getServiceApplications(): Flow<List<ServiceApplicationEntity>> = serviceDao.getAllApplications()
    suspend fun submitServiceApplication(application: ServiceApplicationEntity) = serviceDao.insertApplication(application)

    // Chat
    fun getChatMessages(): Flow<List<ChatMessageEntity>> = chatDao.getAllChatMessages()
    suspend fun sendChatMessage(userText: String, lang: AppLanguage, wardNumber: Int): AiAssistanceResult {
        chatDao.insertMessage(
            ChatMessageEntity(text = userText, isUser = true)
        )

        val aiResult = GeminiGrampanchayatService.getAiResponse(userText, lang, wardNumber)

        chatDao.insertMessage(
            ChatMessageEntity(
                text = aiResult.responseText,
                isUser = false,
                draftComplaintTitle = aiResult.draftTitle ?: "",
                draftComplaintCategory = aiResult.draftCategory ?: "",
                draftComplaintDesc = aiResult.draftDescription ?: "",
                draftWard = aiResult.draftWard
            )
        )

        return aiResult
    }

    suspend fun clearChatHistory() {
        chatDao.clearChat()
        chatDao.insertMessage(
            ChatMessageEntity(
                text = "संभाषण रीसेट झाले. मी आपणास कशी मदत करू शकेन?",
                isUser = false
            )
        )
    }

    // ================= ADMIN OPERATIONS =================

    fun signOutAdmin() {
        authRepository.signOut()
    }

    suspend fun savePanchayatProfile(profile: PanchayatProfile): Boolean {
        return withContext(Dispatchers.IO) {
            val prev = _panchayatProfile.value
            val isNewOrChanged = prev.officeHoursMr != profile.officeHoursMr ||
                prev.phone != profile.phone ||
                prev.addressMr != profile.addressMr ||
                prev.email != profile.email ||
                prev.totalPopulation != profile.totalPopulation

            _panchayatProfile.value = profile
            val success = firestoreSource.savePanchayatProfile(profile)

            // Automatic Notification for Citizen-Facing Panchayat Public Information Changes
            if (success && isNewOrChanged) {
                val detailMr = "कार्यालयीन वेळ: ${profile.officeHoursMr}, संपर्क: ${profile.phone}"
                val notif = NotificationItem(
                    id = "gp_info_${System.currentTimeMillis()}",
                    titleMr = "🏛️ ग्रामपंचायत सार्वजनिक माहिती अद्ययावत",
                    titleEn = "🏛️ Gram Panchayat Information Updated",
                    messageMr = detailMr,
                    messageEn = "Grampanchayat public information, office hours or contacts have been updated.",
                    timestamp = "नुकतेच",
                    isUrgent = false,
                    targetScreen = "GP_INFO",
                    createdAtEpoch = System.currentTimeMillis()
                )
                saveNotification(notif)
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun saveWard(ward: WardEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _wards.value.toMutableList()
            val idx = current.indexOfFirst { it.wardNumber == ward.wardNumber }
            val prev = if (idx >= 0) current[idx] else null
            val isNewOrChanged = prev == null ||
                prev.representativeName != ward.representativeName ||
                prev.nameMr != ward.nameMr ||
                prev.population != ward.population

            if (idx >= 0) current[idx] = ward else current.add(ward)
            _wards.value = current.sortedBy { it.wardNumber }
            val success = firestoreSource.saveWard(ward)

            // Automatic Notification for Citizen-Facing Ward/Prabhag Information Changes
            if (success && isNewOrChanged) {
                val notif = NotificationItem(
                    id = "ward_${ward.wardNumber}_${System.currentTimeMillis()}",
                    titleMr = "🏘️ प्रभाग क्र. ${ward.wardNumber} सार्वजनिक माहिती",
                    titleEn = "🏘️ Ward No. ${ward.wardNumber} Public Info",
                    messageMr = "प्रतिनिधी: ${ward.representativeName}, प्रभाग: ${ward.nameMr}",
                    messageEn = "Representative: ${ward.representativeName}, Ward: ${ward.nameEn}",
                    timestamp = "नुकतेच",
                    isUrgent = false,
                    targetScreen = "GP_INFO",
                    wardNumber = ward.wardNumber,
                    createdAtEpoch = System.currentTimeMillis()
                )
                saveNotification(notif)
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun deleteWard(wardId: String): Boolean {
        return withContext(Dispatchers.IO) {
            _wards.value = _wards.value.filter { it.id != wardId }
            firestoreSource.deleteWard(wardId)
        }
    }

    suspend fun saveWaterSchedule(schedule: WaterScheduleEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val prevList = waterDao.getAllWaterSchedules().firstOrNull() ?: emptyList()
            val prev = prevList.find { it.wardNumber == schedule.wardNumber }
            val isNewOrChanged = prev == null ||
                prev.morningTiming != schedule.morningTiming ||
                prev.eveningTiming != schedule.eveningTiming ||
                prev.status != schedule.status ||
                prev.statusNoteMr != schedule.statusNoteMr

            waterDao.insertSchedules(listOf(schedule))
            val success = firestoreSource.saveWaterSchedule(schedule)

            // Automatic Notification for Public Water Supply Information / Schedule Changes
            if (success && isNewOrChanged) {
                val statusText = if (schedule.status.isNotBlank()) " | स्थिती: ${schedule.status}" else ""
                val notif = NotificationItem(
                    id = "water_ward_${schedule.wardNumber}_${System.currentTimeMillis()}",
                    titleMr = "💧 पाणीपुरवठा अपडेट: प्रभाग क्र. ${schedule.wardNumber}",
                    titleEn = "💧 Water Supply: Ward No. ${schedule.wardNumber}",
                    messageMr = "वेळ: सकाळ ${schedule.morningTiming}, संध्याकाळ ${schedule.eveningTiming}$statusText",
                    messageEn = "Schedule: Morning ${schedule.morningTiming}, Evening ${schedule.eveningTiming}$statusText",
                    timestamp = "नुकतेच",
                    isUrgent = schedule.status.contains("बंद") || schedule.status.contains("दुरुस्ती"),
                    targetScreen = "WATER_SERVICES",
                    targetId = schedule.wardNumber.toString(),
                    wardNumber = schedule.wardNumber,
                    createdAtEpoch = System.currentTimeMillis()
                )
                saveNotification(notif)
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun deleteWaterSchedule(wardNumber: Int): Boolean {
        return withContext(Dispatchers.IO) {
            waterDao.deleteScheduleByWard(wardNumber)
            firestoreSource.deleteWaterSchedule(wardNumber)
        }
    }

    suspend fun saveNotice(notice: NoticeEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val prevList = noticeDao.getAllNotices().firstOrNull() ?: emptyList()
            val prev = prevList.find { it.id == notice.id }
            val isNewOrChanged = prev == null ||
                prev.titleMr != notice.titleMr ||
                prev.descriptionMr != notice.descriptionMr ||
                prev.category != notice.category ||
                prev.isUrgent != notice.isUrgent

            noticeDao.insertNotices(listOf(notice))
            val success = firestoreSource.saveNotice(notice)

            // Automatic Notification for Notices, Announcements, Events & Gram Sabha
            if (success && isNewOrChanged) {
                val isGramSabha = notice.category.contains("GRAM", ignoreCase = true) ||
                    notice.titleMr.contains("ग्रामसभा") ||
                    notice.titleEn.contains("Gram Sabha", ignoreCase = true)
                val titleMr = if (isGramSabha) "📢 ग्रामसभा महत्त्वाची सूचना: ${notice.titleMr}" else "📢 नवीन सूचना: ${notice.titleMr}"
                val titleEn = if (isGramSabha) "📢 Gram Sabha Notice: ${notice.titleEn.ifBlank { notice.titleMr }}" else "📢 Notice: ${notice.titleEn.ifBlank { notice.titleMr }}"

                val notif = NotificationItem(
                    id = "notice_${notice.id}_${System.currentTimeMillis()}",
                    titleMr = titleMr,
                    titleEn = titleEn,
                    messageMr = notice.descriptionMr.take(150),
                    messageEn = notice.descriptionEn.ifBlank { notice.descriptionMr }.take(150),
                    timestamp = "नुकतेच",
                    isUrgent = notice.isUrgent || isGramSabha,
                    targetScreen = "NOTICES",
                    targetId = notice.id,
                    createdAtEpoch = System.currentTimeMillis()
                )
                saveNotification(notif)
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun deleteNotice(noticeId: String): Boolean {
        return withContext(Dispatchers.IO) {
            noticeDao.deleteNoticeById(noticeId)
            firestoreSource.deleteNotice(noticeId)
        }
    }

    suspend fun updateComplaintAdminFields(
        complaintId: String,
        status: String,
        remarks: String,
        officer: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val comp = complaintDao.getAllComplaints().firstOrNull()?.find { it.id == complaintId }
            if (comp != null) {
                val updated = comp.copy(
                    status = status,
                    officialRemarks = remarks,
                    assignedOfficer = officer,
                    updatedAt = System.currentTimeMillis()
                )
                complaintDao.insertComplaint(updated)
            }
            firestoreSource.updateComplaintAdminFields(complaintId, status, remarks, officer)
        }
    }

    suspend fun deleteComplaint(complaintId: String): Boolean {
        return withContext(Dispatchers.IO) {
            complaintDao.deleteComplaintById(complaintId)
            firestoreSource.deleteComplaint(complaintId)
        }
    }

    suspend fun saveOfficial(official: OfficialContactEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _officials.value.toMutableList()
            val idx = current.indexOfFirst { it.id == official.id || (it.nameMr == official.nameMr && it.nameMr.isNotBlank()) }
            val prev = if (idx >= 0) current[idx] else null
            val isNewOrChanged = prev == null ||
                prev.phoneNumber != official.phoneNumber ||
                prev.designationMr != official.designationMr

            if (idx >= 0) current[idx] = official else current.add(official)
            _officials.value = current
            val success = firestoreSource.saveOfficial(official)

            // Automatic Notification for Public Officials Updates
            if (success && isNewOrChanged) {
                val notif = NotificationItem(
                    id = "official_${official.id}_${System.currentTimeMillis()}",
                    titleMr = "👤 पदाधिकारी संपर्क अपडेट: ${official.nameMr}",
                    titleEn = "👤 Official Contact: ${official.nameMr}",
                    messageMr = "पद: ${official.designationMr} | फोन: ${official.phoneNumber}",
                    messageEn = "Designation: ${official.designationEn} | Phone: ${official.phoneNumber}",
                    timestamp = "नुकतेच",
                    isUrgent = false,
                    targetScreen = "GP_INFO",
                    createdAtEpoch = System.currentTimeMillis()
                )
                saveNotification(notif)
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun deleteOfficial(officialId: String): Boolean {
        return withContext(Dispatchers.IO) {
            _officials.value = _officials.value.filter { it.id != officialId }
            firestoreSource.deleteOfficial(officialId)
        }
    }

    suspend fun saveProject(project: DevelopmentProjectEntity): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _developmentProjects.value.toMutableList()
            val idx = current.indexOfFirst { it.id == project.id || (it.titleMr == project.titleMr && it.titleMr.isNotBlank()) }
            val prev = if (idx >= 0) current[idx] else null
            val isNewOrChanged = prev == null ||
                prev.titleMr != project.titleMr ||
                prev.status != project.status ||
                prev.sanctionedBudget != project.sanctionedBudget

            if (idx >= 0) current[idx] = project else current.add(project)
            _developmentProjects.value = current
            val success = firestoreSource.saveProject(project)

            // Automatic Notification for Public Development / Facility Updates
            if (success && isNewOrChanged) {
                val notif = NotificationItem(
                    id = "proj_${project.id}_${System.currentTimeMillis()}",
                    titleMr = "🏗️ विकास काम अपडेट: ${project.titleMr}",
                    titleEn = "🏗️ Development Work: ${project.titleEn.ifBlank { project.titleMr }}",
                    messageMr = "सद्यस्थिती: ${project.status} | मंजूर निधी: ${project.sanctionedBudget}",
                    messageEn = "Status: ${project.status} | Budget: ${project.sanctionedBudget}",
                    timestamp = "नुकतेच",
                    isUrgent = false,
                    targetScreen = "GP_INFO",
                    createdAtEpoch = System.currentTimeMillis()
                )
                saveNotification(notif)
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun deleteProject(projectId: String): Boolean {
        return withContext(Dispatchers.IO) {
            _developmentProjects.value = _developmentProjects.value.filter { it.id != projectId }
            firestoreSource.deleteProject(projectId)
        }
    }

    suspend fun saveOnlineService(service: OnlineServiceItem): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _onlineServices.value.toMutableList()
            val idx = current.indexOfFirst { it.id == service.id || it.key == service.key }
            val prev = if (idx >= 0) current[idx] else null
            val isNewOrChanged = prev == null ||
                prev.titleMr != service.titleMr ||
                prev.fee != service.fee ||
                prev.processingDays != service.processingDays

            if (idx >= 0) current[idx] = service else current.add(service)
            _onlineServices.value = current
            val success = firestoreSource.saveService(service)

            // Automatic Notification for Public Services / Schemes Updates
            if (success && isNewOrChanged) {
                val notif = NotificationItem(
                    id = "service_${service.id}_${System.currentTimeMillis()}",
                    titleMr = "📋 सार्वजनिक सेवा / योजना: ${service.titleMr}",
                    titleEn = "📋 Public Service: ${service.titleEn.ifBlank { service.titleMr }}",
                    messageMr = "कालावधी: ${service.processingDays} | शुल्क: ₹${service.fee}",
                    messageEn = "Processing: ${service.processingDays} | Fee: ₹${service.fee}",
                    timestamp = "नुकतेच",
                    isUrgent = false,
                    targetScreen = "ONLINE_SERVICES",
                    targetId = service.id,
                    createdAtEpoch = System.currentTimeMillis()
                )
                saveNotification(notif)
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun deleteOnlineService(serviceId: String): Boolean {
        return withContext(Dispatchers.IO) {
            _onlineServices.value = _onlineServices.value.filter { it.id != serviceId }
            firestoreSource.deleteService(serviceId)
        }
    }

    suspend fun saveNotification(notif: NotificationItem): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _notifications.value.toMutableList()
            val idx = current.indexOfFirst { it.id == notif.id }
            if (idx >= 0) current[idx] = notif else current.add(0, notif)
            _notifications.value = current
            val success = firestoreSource.saveNotification(notif)
            if (success) {
                NotificationHelper.showPublicNotification(appContext, notif)
            }
            success
        }
    }

    suspend fun deleteNotification(notifId: String): Boolean {
        return withContext(Dispatchers.IO) {
            _notifications.value = _notifications.value.filter { it.id != notifId }
            firestoreSource.deleteNotification(notifId)
        }
    }

    /**
     * STRICT PRIVACY MANDATE:
     * NEVER send notifications for PERSONAL / PRIVATE CITIZEN INFORMATION changes:
     * - Citizen name
     * - Citizen mobile number
     * - Citizen address
     * - Citizen personal profile
     * - Citizen personal documents
     * - Citizen-specific private information
     */
    suspend fun saveCitizen(profile: UserProfile): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _citizens.value.toMutableList()
            val idx = current.indexOfFirst { it.id == profile.id }
            if (idx >= 0) current[idx] = profile else current.add(profile)
            _citizens.value = current
            // Strictly NO notification event is created or dispatched!
            firestoreSource.saveCitizen(profile)
        }
    }

    suspend fun seedAllDataToFirestore(): Boolean {
        return withContext(Dispatchers.IO) {
            firestoreSource.seedAllDataToFirestore()
        }
    }

    suspend fun lookupOfficerForActivation(query: String): Result<PreapprovedOfficer> {
        return withContext(Dispatchers.IO) {
            authRepository.lookupOfficerForActivation(query)
        }
    }

    suspend fun activateOfficerAccount(
        adminIdOrMobile: String,
        otp: String,
        expectedOtp: String,
        password: String
    ): Result<AdminUser> {
        return withContext(Dispatchers.IO) {
            authRepository.activateOfficerAccount(adminIdOrMobile, otp, expectedOtp, password)
        }
    }

    suspend fun signInAdmin(
        adminIdOrMobile: String,
        pass: String,
        otp: String = "",
        expectedOtp: String? = null
    ): Result<AdminUser> {
        return withContext(Dispatchers.IO) {
            val result = authRepository.signInAdmin(adminIdOrMobile, pass, otp, expectedOtp)
            if (result.isSuccess) {
                val admin = result.getOrNull()
                if (admin != null) {
                    loadPanchayatDataForGp(admin.gramPanchayatId)
                }
            }
            result
        }
    }

    suspend fun sendPasswordResetAdmin(adminIdOrMobile: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            var emailToReset = adminIdOrMobile.trim()
            if (!emailToReset.contains("@")) {
                val officer = MaharashtraDirectory.findPreapprovedOfficer(emailToReset)
                if (officer != null) {
                    emailToReset = if (officer.officialEmail.contains("@")) officer.officialEmail else authRepository.formatOfficerEmail(officer.mobileNumber, officer.adminId)
                } else {
                    val cleanDigits = emailToReset.filter { it.isDigit() }
                    emailToReset = authRepository.formatOfficerEmail(cleanDigits, emailToReset)
                }
            }
            authRepository.sendPasswordReset(emailToReset)
        }
    }

    fun loadPanchayatDataForGp(gpId: String) {
        scope.launch {
            firestoreSource.getPanchayatProfileFlow(gpId).collect { profile ->
                if (profile != null) {
                    _panchayatProfile.value = profile
                }
            }
        }
    }

    // ================= AI CALL SYSTEM OPERATIONS =================

    fun previewAiVoice(text: String, language: String, voiceId: String) {
        aiCallService.speakPreview(text, language, voiceId)
    }

    fun stopAiVoicePreview() {
        aiCallService.stopPreview()
    }

    suspend fun saveAiCampaign(campaign: AiCallCampaign): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _aiCampaigns.value.toMutableList()
            val idx = current.indexOfFirst { it.id == campaign.id }
            if (idx >= 0) current[idx] = campaign else current.add(0, campaign)
            _aiCampaigns.value = current
            firestoreSource.saveAiCampaign(campaign)
        }
    }

    suspend fun saveAiCallLog(log: AiCallLog): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _aiCallLogs.value.toMutableList()
            val idx = current.indexOfFirst { it.id == log.id }
            if (idx >= 0) current[idx] = log else current.add(0, log)
            _aiCallLogs.value = current
            firestoreSource.saveAiCallLog(log)
        }
    }

    suspend fun saveAiScheduledCall(call: AiScheduledCall): Boolean {
        return withContext(Dispatchers.IO) {
            val current = _aiScheduledCalls.value.toMutableList()
            val idx = current.indexOfFirst { it.id == call.id }
            if (idx >= 0) current[idx] = call else current.add(call)
            _aiScheduledCalls.value = current
            firestoreSource.saveAiScheduledCall(call)
        }
    }

    suspend fun deleteAiScheduledCall(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            _aiScheduledCalls.value = _aiScheduledCalls.value.filter { it.id != id }
            firestoreSource.deleteAiScheduledCall(id)
        }
    }

    suspend fun saveAiCallSettings(settings: AiCallSettings): Boolean {
        return withContext(Dispatchers.IO) {
            _aiCallSettings.value = settings
            firestoreSource.saveAiCallSettings(settings)
        }
    }
}


