package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.repository.GrampanchayatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ScreenDestination {
    SPLASH,
    ROLE_SELECTION,
    AUTH,
    HOME,
    COMPLAINTS,
    AI_ASSISTANT,
    NOTICES,
    PROFILE,
    GP_INFO,
    WATER_SERVICES,
    ONLINE_SERVICES,
    NOTIFICATIONS,
    ADMIN_LOGIN,
    ADMIN_ACTIVATION,
    ADMIN_DASHBOARD
}

enum class PhoneAuthStep {
    INPUT_DETAILS,
    VERIFY_OTP
}

data class ComplaintDraftState(
    val title: String = "",
    val category: String = "water",
    val description: String = "",
    val wardNumber: Int = 3,
    val locationDetail: String = "",
    val urgency: String = "MEDIUM"
)

class GrampanchayatViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GrampanchayatRepository(application)

    // Language state
    private val _language = MutableStateFlow(AppLanguage.MARATHI)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    // Navigation state
    private val _currentScreen = MutableStateFlow(ScreenDestination.SPLASH)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Citizen Auth State
    private val _isCitizenAuthLoading = MutableStateFlow(false)
    val isCitizenAuthLoading: StateFlow<Boolean> = _isCitizenAuthLoading.asStateFlow()

    private val _citizenAuthError = MutableStateFlow<String?>(null)
    val citizenAuthError: StateFlow<String?> = _citizenAuthError.asStateFlow()

    // Real Firebase Phone Auth (SMS OTP) States
    private val _phoneAuthStep = MutableStateFlow(PhoneAuthStep.INPUT_DETAILS)
    val phoneAuthStep: StateFlow<PhoneAuthStep> = _phoneAuthStep.asStateFlow()

    private val _phoneAuthVerificationId = MutableStateFlow<String?>(null)
    val phoneAuthVerificationId: StateFlow<String?> = _phoneAuthVerificationId.asStateFlow()

    private val _phoneAuthResendToken = MutableStateFlow<com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken?>(null)
    val phoneAuthResendToken: StateFlow<com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken?> = _phoneAuthResendToken.asStateFlow()

    private val _phoneAuthCooldown = MutableStateFlow(0)
    val phoneAuthCooldown: StateFlow<Int> = _phoneAuthCooldown.asStateFlow()

    private val _phoneAuthMobile = MutableStateFlow("")
    val phoneAuthMobile: StateFlow<String> = _phoneAuthMobile.asStateFlow()

    private var cooldownJob: kotlinx.coroutines.Job? = null

    // Admin Auth State
    private val _adminUser = MutableStateFlow<AdminUser?>(null)
    val adminUser: StateFlow<AdminUser?> = _adminUser.asStateFlow()

    private val _isAdminLoading = MutableStateFlow(false)
    val isAdminLoading: StateFlow<Boolean> = _isAdminLoading.asStateFlow()

    private val _adminLoginError = MutableStateFlow<String?>(null)
    val adminLoginError: StateFlow<String?> = _adminLoginError.asStateFlow()

    private val _isActivatingOfficer = MutableStateFlow(false)
    val isActivatingOfficer: StateFlow<Boolean> = _isActivatingOfficer.asStateFlow()

    private val _adminActivationError = MutableStateFlow<String?>(null)
    val adminActivationError: StateFlow<String?> = _adminActivationError.asStateFlow()

    private val _verifiedPreapprovedOfficer = MutableStateFlow<PreapprovedOfficer?>(null)
    val verifiedPreapprovedOfficer: StateFlow<PreapprovedOfficer?> = _verifiedPreapprovedOfficer.asStateFlow()

    // Data streams from Room / Repository
    val userProfile: StateFlow<UserProfile?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val complaints: StateFlow<List<ComplaintEntity>> = repository.getAllComplaints()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notices: StateFlow<List<NoticeEntity>> = repository.getAllNotices()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val waterSchedules: StateFlow<List<WaterScheduleEntity>> = repository.getWaterSchedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tankerBookings: StateFlow<List<TankerBookingEntity>> = repository.getTankerBookings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviceApplications: StateFlow<List<ServiceApplicationEntity>> = repository.getServiceApplications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.getChatMessages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Firestore-synced streams
    val panchayatProfile: StateFlow<PanchayatProfile> = repository.panchayatProfile
    val wards: StateFlow<List<WardEntity>> = repository.wards
    val officialContacts: StateFlow<List<OfficialContactEntity>> = repository.officials
    val developmentProjects: StateFlow<List<DevelopmentProjectEntity>> = repository.developmentProjects
    val onlineServices: StateFlow<List<OnlineServiceItem>> = repository.onlineServices
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val citizens: StateFlow<List<UserProfile>> = repository.citizens

    // AI Call System StateFlows
    val aiCampaigns: StateFlow<List<AiCallCampaign>> = repository.aiCampaigns
    val aiCallLogs: StateFlow<List<AiCallLog>> = repository.aiCallLogs
    val aiScheduledCalls: StateFlow<List<AiScheduledCall>> = repository.aiScheduledCalls
    val aiCallSettings: StateFlow<AiCallSettings> = repository.aiCallSettings

    private val _isAiBatchCallingActive = MutableStateFlow(false)
    val isAiBatchCallingActive: StateFlow<Boolean> = _isAiBatchCallingActive.asStateFlow()

    private val _aiBatchProgress = MutableStateFlow(Pair(0, 0))
    val aiBatchProgress: StateFlow<Pair<Int, Int>> = _aiBatchProgress.asStateFlow()

    private val _currentCallingCitizenName = MutableStateFlow("")
    val currentCallingCitizenName: StateFlow<String> = _currentCallingCitizenName.asStateFlow()

    private val _isAiPreviewPlaying = MutableStateFlow(false)
    val isAiPreviewPlaying: StateFlow<Boolean> = _isAiPreviewPlaying.asStateFlow()

    // UI Interactive States
    private val _complaintFilter = MutableStateFlow("ALL") // ALL, PENDING, IN_PROGRESS, RESOLVED
    val complaintFilter: StateFlow<String> = _complaintFilter.asStateFlow()

    private val _noticeCategoryFilter = MutableStateFlow(NoticeCategory.ALL)
    val noticeCategoryFilter: StateFlow<NoticeCategory> = _noticeCategoryFilter.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _complaintDraftState = MutableStateFlow(ComplaintDraftState())
    val complaintDraftState: StateFlow<ComplaintDraftState> = _complaintDraftState.asStateFlow()

    private val _selectedComplaintForDetail = MutableStateFlow<ComplaintEntity?>(null)
    val selectedComplaintForDetail: StateFlow<ComplaintEntity?> = _selectedComplaintForDetail.asStateFlow()

    private val _selectedApplicationForCertificate = MutableStateFlow<ServiceApplicationEntity?>(null)
    val selectedApplicationForCertificate: StateFlow<ServiceApplicationEntity?> = _selectedApplicationForCertificate.asStateFlow()

    private val _showNewComplaintDialog = MutableStateFlow(false)
    val showNewComplaintDialog: StateFlow<Boolean> = _showNewComplaintDialog.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _selectedDistrictId = MutableStateFlow("buldhana")
    val selectedDistrictId: StateFlow<String> = _selectedDistrictId.asStateFlow()

    private val _selectedTalukaId = MutableStateFlow("chikhli")
    val selectedTalukaId: StateFlow<String> = _selectedTalukaId.asStateFlow()

    private val _selectedGramPanchayatId = MutableStateFlow("gp_palaskhed_daulat")
    val selectedGramPanchayatId: StateFlow<String> = _selectedGramPanchayatId.asStateFlow()

    fun setSelectedGramPanchayat(districtId: String, talukaId: String, gpId: String) {
        _selectedDistrictId.value = districtId
        _selectedTalukaId.value = talukaId
        _selectedGramPanchayatId.value = gpId
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.MARATHI) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.MARATHI
        }
    }

    fun navigateTo(screen: ScreenDestination) {
        _currentScreen.value = screen
    }

    fun handleNotificationNavigation(targetScreen: String?, targetId: String? = null, wardNumber: Int? = null) {
        if (targetScreen.isNullOrBlank()) return
        when (targetScreen.uppercase().trim()) {
            "WATER_SERVICES", "WATER", "WATER_SCHEDULE" -> {
                _currentScreen.value = ScreenDestination.WATER_SERVICES
            }
            "NOTICES", "NOTICE", "GRAM_SABHA", "GRAMSABHA" -> {
                if (targetScreen.contains("GRAM", ignoreCase = true)) {
                    _noticeCategoryFilter.value = NoticeCategory.GRAMSABHA
                }
                _currentScreen.value = ScreenDestination.NOTICES
            }
            "GP_INFO", "PANCHAYAT_INFO", "WARD_INFO", "PROJECTS" -> {
                _currentScreen.value = ScreenDestination.GP_INFO
            }
            "ONLINE_SERVICES", "SERVICES", "SCHEMES" -> {
                _currentScreen.value = ScreenDestination.ONLINE_SERVICES
            }
            "NOTIFICATIONS", "PUBLIC_ALERT" -> {
                _currentScreen.value = ScreenDestination.NOTIFICATIONS
            }
            else -> {
                _currentScreen.value = ScreenDestination.NOTIFICATIONS
            }
        }
    }

    fun setComplaintFilter(filter: String) {
        _complaintFilter.value = filter
    }

    fun setNoticeCategoryFilter(category: NoticeCategory) {
        _noticeCategoryFilter.value = category
    }

    fun selectComplaintForDetail(complaint: ComplaintEntity?) {
        _selectedComplaintForDetail.value = complaint
    }

    fun selectApplicationForCertificate(app: ServiceApplicationEntity?) {
        _selectedApplicationForCertificate.value = app
    }

    fun openNewComplaintDialog(prefill: ComplaintDraftState? = null) {
        if (prefill != null) {
            _complaintDraftState.value = prefill
        }
        _showNewComplaintDialog.value = true
    }

    fun closeNewComplaintDialog() {
        _showNewComplaintDialog.value = false
    }

    fun updateComplaintDraft(updater: (ComplaintDraftState) -> ComplaintDraftState) {
        _complaintDraftState.value = updater(_complaintDraftState.value)
    }

    fun submitComplaint(
        title: String,
        category: String,
        description: String,
        wardNumber: Int,
        locationDetail: String,
        photoUri: String = ""
    ) {
        viewModelScope.launch {
            val randomNum = (1000..9999).random()
            val newId = "GP-2026-$randomNum"
            val complaint = ComplaintEntity(
                id = newId,
                title = title.ifBlank { "नागरिक तक्रार ($category)" },
                description = description,
                category = category,
                wardNumber = wardNumber,
                locationDetail = locationDetail.ifBlank { "प्रभाग क्र. $wardNumber" },
                status = "PENDING",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                officialRemarks = if (_language.value == AppLanguage.MARATHI) "तक्रार नोंदवली गेली आहे. संबंधित विभागाकडे चौकशीसाठी वर्ग केली आहे." else "Grievance received. Forwarded to departmental officer for inspection.",
                assignedOfficer = if (_language.value == AppLanguage.MARATHI) "ग्रामपंचायत हेल्पडेस्क" else "Helpdesk Officer",
                photoUri = photoUri
            )
            repository.createComplaint(complaint)
            _showNewComplaintDialog.value = false
            _complaintDraftState.value = ComplaintDraftState(wardNumber = userProfile.value?.wardNumber ?: 3)
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "तक्रार क्रमांक $newId यशस्वीरित्या नोंदवली गेली!"
                else
                    "Grievance $newId submitted successfully!"
            )
        }
    }

    fun submitRating(complaintId: String, rating: Int, feedback: String) {
        viewModelScope.launch {
            repository.updateComplaintRating(complaintId, rating, feedback)
            _selectedComplaintForDetail.value = _selectedComplaintForDetail.value?.copy(
                rating = rating,
                citizenFeedback = feedback
            )
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "अभिप्राय नोंदवला गेला! धन्यवाद."
                else
                    "Rating & feedback recorded. Thank you!"
            )
        }
    }

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return
        val currentLang = _language.value
        val ward = userProfile.value?.wardNumber ?: 3

        viewModelScope.launch {
            _isAiThinking.value = true
            try {
                repository.sendChatMessage(userText, currentLang, ward)
            } finally {
                _isAiThinking.value = false
            }
        }
    }

    fun openNewComplaintFromAiDraft(
        title: String,
        category: String,
        desc: String,
        ward: Int
    ) {
        _complaintDraftState.value = ComplaintDraftState(
            title = title,
            category = category.ifBlank { "water" },
            description = desc,
            wardNumber = if (ward in 1..6) ward else (userProfile.value?.wardNumber ?: 3),
            locationDetail = if (_language.value == AppLanguage.MARATHI) "प्रभाग क्र. $ward मुख्य रस्ता" else "Ward $ward Main Area"
        )
        _showNewComplaintDialog.value = true
        _currentScreen.value = ScreenDestination.COMPLAINTS
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    fun bookTanker(
        name: String,
        mobile: String,
        ward: Int,
        address: String,
        date: String,
        slot: String,
        purpose: String
    ) {
        viewModelScope.launch {
            val bookingId = "TNK-${(100..999).random()}"
            val booking = TankerBookingEntity(
                id = bookingId,
                applicantName = name,
                mobileNumber = mobile,
                wardNumber = ward,
                deliveryAddress = address,
                requiredDate = date,
                timeSlot = slot,
                purpose = purpose,
                status = "CONFIRMED"
            )
            repository.bookWaterTanker(booking)
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "पाणी टँकर बुकिंग क्रमांक $bookingId मंजूर झाले!"
                else
                    "Water tanker booked! Token: $bookingId"
            )
        }
    }

    fun applyForService(
        serviceType: String,
        applicantName: String,
        mobile: String,
        ward: Int,
        details: String,
        feeAmount: Int
    ) {
        viewModelScope.launch {
            val randomAppNum = (1000..9999).random()
            val appId = "APP-2026-$randomAppNum"
            val certNum = "GP-${serviceType.uppercase().take(3)}-2026/$randomAppNum"
            val todayStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

            val application = ServiceApplicationEntity(
                id = appId,
                serviceType = serviceType,
                applicantName = applicantName,
                mobileNumber = mobile,
                wardNumber = ward,
                details = details,
                amountPaid = feeAmount,
                status = "APPROVED",
                appliedDate = todayStr,
                certificateNumber = certNum,
                remarks = if (_language.value == AppLanguage.MARATHI) "ऑनलाइन पडताळणी पूर्ण झाली व डिजिटल दाखला तयार करण्यात आला." else "Online verification complete and digital certificate issued."
            )
            repository.submitServiceApplication(application)
            _selectedApplicationForCertificate.value = application
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "अर्ज $appId यशस्वीरित्या स्वीकारला गेला!"
                else
                    "Application $appId submitted and approved!"
            )
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "माहिती सेव्ह झाली!"
                else
                    "Profile updated!"
            )
        }
    }

    fun registerCitizen(
        fullName: String,
        districtId: String = _selectedDistrictId.value,
        talukaId: String = _selectedTalukaId.value,
        gramPanchayatId: String = _selectedGramPanchayatId.value,
        ward: Int,
        mobile: String,
        otp: String = "123456",
        expectedOtp: String = "123456",
        password: String = "Citizen@123",
        onComplete: (Boolean) -> Unit
    ) {
        _isCitizenAuthLoading.value = true
        _citizenAuthError.value = null
        viewModelScope.launch {
            val result = repository.registerCitizen(
                fullName = fullName,
                districtId = districtId,
                talukaId = talukaId,
                gramPanchayatId = gramPanchayatId,
                wardNumber = ward,
                mobileNumber = mobile,
                otp = otp,
                expectedOtp = expectedOtp,
                password = password
            )
            _isCitizenAuthLoading.value = false
            if (result.isSuccess) {
                val registeredProfile = result.getOrNull()
                _citizenAuthError.value = null
                _selectedDistrictId.value = districtId
                _selectedTalukaId.value = talukaId
                _selectedGramPanchayatId.value = gramPanchayatId
                _currentScreen.value = ScreenDestination.HOME
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "नोंदणी यशस्वी झाली! आपले स्वागत आहे, ${registeredProfile?.fullName ?: fullName}"
                    else
                        "Registration successful! Welcome, ${registeredProfile?.fullNameEn ?: fullName}"
                )
                onComplete(true)
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "नोंदणी अयशस्वी (Registration failed)"
                _citizenAuthError.value = err
                showToast(err)
                onComplete(false)
            }
        }
    }

    fun loginCitizen(
        mobile: String,
        selectedGramPanchayatId: String = _selectedGramPanchayatId.value,
        password: String = "Citizen@123",
        onComplete: (Boolean) -> Unit
    ) {
        _isCitizenAuthLoading.value = true
        _citizenAuthError.value = null
        viewModelScope.launch {
            val result = repository.loginCitizen(mobile, selectedGramPanchayatId, password)
            _isCitizenAuthLoading.value = false
            if (result.isSuccess) {
                val profile = result.getOrNull()
                _citizenAuthError.value = null
                if (profile != null) {
                    _selectedDistrictId.value = profile.districtId
                    _selectedTalukaId.value = profile.talukaId
                    _selectedGramPanchayatId.value = profile.gramPanchayatId
                }
                _currentScreen.value = ScreenDestination.HOME
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "लॉगिन यशस्वी! आपले स्वागत आहे, ${profile?.fullName ?: ""}"
                    else
                        "Login successful! Welcome, ${profile?.fullNameEn ?: ""}"
                )
                onComplete(true)
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "लॉगिन अयशस्वी (Login failed)"
                _citizenAuthError.value = err
                showToast(err)
                onComplete(false)
            }
        }
    }

    fun startCooldownTimer(durationSeconds: Int = 60) {
        cooldownJob?.cancel()
        _phoneAuthCooldown.value = durationSeconds
        cooldownJob = viewModelScope.launch {
            while (_phoneAuthCooldown.value > 0) {
                kotlinx.coroutines.delay(1000)
                _phoneAuthCooldown.value = _phoneAuthCooldown.value - 1
            }
        }
    }

    fun sendPhoneOtp(
        activity: android.app.Activity,
        mobileNumber: String,
        onSuccess: () -> Unit = {}
    ) {
        val cleanDigits = mobileNumber.filter { it.isDigit() }
        val cleanMobile = if (cleanDigits.length >= 10) cleanDigits.takeLast(10) else cleanDigits
        if (cleanMobile.length < 10) {
            val err = if (_language.value == AppLanguage.MARATHI)
                "कृपया १० अंकी वैध मोबाईल नंबर प्रविष्ट करा."
            else
                "Please enter a valid 10-digit mobile number."
            _citizenAuthError.value = err
            showToast(err)
            return
        }

        _phoneAuthMobile.value = cleanMobile
        _isCitizenAuthLoading.value = true
        _citizenAuthError.value = null

        repository.sendPhoneOtp(
            activity = activity,
            phoneNumber = cleanMobile,
            forceResendingToken = null,
            onCodeSent = { verificationId, token ->
                _isCitizenAuthLoading.value = false
                _phoneAuthVerificationId.value = verificationId
                _phoneAuthResendToken.value = token
                _phoneAuthStep.value = PhoneAuthStep.VERIFY_OTP
                startCooldownTimer(60)
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "मोबाईलवर SMS OTP पाठवला गेला आहे."
                    else
                        "SMS OTP sent to your mobile number."
                )
                onSuccess()
            },
            onVerificationCompleted = { credential ->
                _isCitizenAuthLoading.value = false
                // Firebase instant auto-verification by Google Play Services
                viewModelScope.launch {
                    val signInRes = repository.signInWithPhoneCredential(credential)
                    if (signInRes.isSuccess) {
                        Log.i("ViewModel", "Auto-verification completed successfully.")
                    }
                }
            },
            onVerificationFailed = { exception ->
                _isCitizenAuthLoading.value = false
                val err = exception.localizedMessage ?: "OTP पाठवण्यात त्रुटी आली."
                _citizenAuthError.value = err
                showToast(err)
            }
        )
    }

    fun resendPhoneOtp(activity: android.app.Activity) {
        if (_phoneAuthCooldown.value > 0) {
            val waitMsg = if (_language.value == AppLanguage.MARATHI)
                "कृपया ${_phoneAuthCooldown.value} सेकंद प्रतीक्षा करा."
            else
                "Please wait ${_phoneAuthCooldown.value} seconds."
            showToast(waitMsg)
            return
        }

        val cleanMobile = _phoneAuthMobile.value
        if (cleanMobile.isBlank()) return

        _isCitizenAuthLoading.value = true
        _citizenAuthError.value = null

        repository.sendPhoneOtp(
            activity = activity,
            phoneNumber = cleanMobile,
            forceResendingToken = _phoneAuthResendToken.value,
            onCodeSent = { verificationId, token ->
                _isCitizenAuthLoading.value = false
                _phoneAuthVerificationId.value = verificationId
                _phoneAuthResendToken.value = token
                startCooldownTimer(60)
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "नवीन SMS OTP पाठवला गेला आहे."
                    else
                        "New SMS OTP sent."
                )
            },
            onVerificationCompleted = { credential ->
                _isCitizenAuthLoading.value = false
            },
            onVerificationFailed = { exception ->
                _isCitizenAuthLoading.value = false
                val err = exception.localizedMessage ?: "OTP पाठवण्यात त्रुटी आली."
                _citizenAuthError.value = err
                showToast(err)
            }
        )
    }

    fun verifyPhoneOtpAndRegister(
        enteredOtp: String,
        fullName: String,
        districtId: String = _selectedDistrictId.value,
        talukaId: String = _selectedTalukaId.value,
        gramPanchayatId: String = _selectedGramPanchayatId.value,
        ward: Int,
        password: String = "Citizen@123",
        onComplete: (Boolean) -> Unit
    ) {
        val verificationId = _phoneAuthVerificationId.value
        if (verificationId.isNullOrBlank()) {
            val err = if (_language.value == AppLanguage.MARATHI)
                "पडताळणी आयडी सापडला नाही. कृपया पुन्हा OTP पाठवा."
            else
                "Verification ID missing. Please resend OTP."
            _citizenAuthError.value = err
            showToast(err)
            onComplete(false)
            return
        }

        if (enteredOtp.trim().length != 6) {
            _citizenAuthError.value = "Invalid OTP"
            showToast(if (_language.value == AppLanguage.MARATHI) "अवैध OTP! ६ अंकी कोड टाका." else "Invalid OTP")
            onComplete(false)
            return
        }

        _isCitizenAuthLoading.value = true
        _citizenAuthError.value = null

        viewModelScope.launch {
            val verifyRes = repository.verifyOtpAndSignIn(verificationId, enteredOtp.trim())
            if (verifyRes.isFailure) {
                _isCitizenAuthLoading.value = false
                val err = "Invalid OTP"
                _citizenAuthError.value = err
                showToast(if (_language.value == AppLanguage.MARATHI) "अवैध OTP! कृपया SMS तपासून पुन्हा प्रयत्न करा." else "Invalid OTP")
                onComplete(false)
                return@launch
            }

            val firebaseUser = verifyRes.getOrNull()!!
            val regRes = repository.completeCitizenRegistrationWithFirebaseUser(
                firebaseUser = firebaseUser,
                fullName = fullName,
                districtId = districtId,
                talukaId = talukaId,
                gramPanchayatId = gramPanchayatId,
                wardNumber = ward,
                mobileNumber = _phoneAuthMobile.value,
                password = password
            )

            _isCitizenAuthLoading.value = false
            if (regRes.isSuccess) {
                _phoneAuthStep.value = PhoneAuthStep.INPUT_DETAILS
                _phoneAuthVerificationId.value = null
                _citizenAuthError.value = null
                _selectedDistrictId.value = districtId
                _selectedTalukaId.value = talukaId
                _selectedGramPanchayatId.value = gramPanchayatId
                _currentScreen.value = ScreenDestination.HOME
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "नोंदणी यशस्वी झाली! आपले स्वागत आहे, $fullName"
                    else
                        "Registration successful! Welcome, $fullName"
                )
                onComplete(true)
            } else {
                val err = regRes.exceptionOrNull()?.localizedMessage ?: "नोंदणी अयशस्वी"
                _citizenAuthError.value = err
                showToast(err)
                onComplete(false)
            }
        }
    }

    fun verifyPhoneOtpAndLogin(
        enteredOtp: String,
        selectedGramPanchayatId: String = _selectedGramPanchayatId.value,
        onComplete: (Boolean) -> Unit
    ) {
        val verificationId = _phoneAuthVerificationId.value
        if (verificationId.isNullOrBlank()) {
            val err = if (_language.value == AppLanguage.MARATHI)
                "पडताळणी आयडी सापडला नाही. कृपया पुन्हा OTP पाठवा."
            else
                "Verification ID missing. Please resend OTP."
            _citizenAuthError.value = err
            showToast(err)
            onComplete(false)
            return
        }

        if (enteredOtp.trim().length != 6) {
            _citizenAuthError.value = "Invalid OTP"
            showToast(if (_language.value == AppLanguage.MARATHI) "अवैध OTP! ६ अंकी कोड टाका." else "Invalid OTP")
            onComplete(false)
            return
        }

        _isCitizenAuthLoading.value = true
        _citizenAuthError.value = null

        viewModelScope.launch {
            val verifyRes = repository.verifyOtpAndSignIn(verificationId, enteredOtp.trim())
            if (verifyRes.isFailure) {
                _isCitizenAuthLoading.value = false
                val err = "Invalid OTP"
                _citizenAuthError.value = err
                showToast(if (_language.value == AppLanguage.MARATHI) "अवैध OTP! कृपया SMS तपासून पुन्हा प्रयत्न करा." else "Invalid OTP")
                onComplete(false)
                return@launch
            }

            val firebaseUser = verifyRes.getOrNull()!!
            val loginRes = repository.completeCitizenLoginWithFirebaseUser(
                firebaseUser = firebaseUser,
                mobileNumber = _phoneAuthMobile.value,
                selectedGramPanchayatId = selectedGramPanchayatId
            )

            _isCitizenAuthLoading.value = false
            if (loginRes.isSuccess) {
                val profile = loginRes.getOrNull()
                _phoneAuthStep.value = PhoneAuthStep.INPUT_DETAILS
                _phoneAuthVerificationId.value = null
                _citizenAuthError.value = null
                if (profile != null) {
                    _selectedDistrictId.value = profile.districtId
                    _selectedTalukaId.value = profile.talukaId
                    _selectedGramPanchayatId.value = profile.gramPanchayatId
                }
                _currentScreen.value = ScreenDestination.HOME
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "लॉगिन यशस्वी! आपले स्वागत आहे, ${profile?.fullName ?: ""}"
                    else
                        "Login successful! Welcome, ${profile?.fullNameEn ?: ""}"
                )
                onComplete(true)
            } else {
                val err = loginRes.exceptionOrNull()?.localizedMessage ?: "लॉगिन अयशस्वी"
                _citizenAuthError.value = err
                showToast(err)
                onComplete(false)
            }
        }
    }

    fun resetPhoneAuthStep() {
        _phoneAuthStep.value = PhoneAuthStep.INPUT_DETAILS
        _phoneAuthVerificationId.value = null
        _citizenAuthError.value = null
    }

    fun logout() {
        viewModelScope.launch {
            repository.logoutCitizen()
            _currentScreen.value = ScreenDestination.ROLE_SELECTION
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "आपण यशस्वीरीत्या लॉग आउट झाला आहात."
                else
                    "Logged out successfully."
            )
        }
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // ================= ADMIN / OFFICER AUTH & DASHBOARD OPERATIONS =================

    fun lookupOfficerForActivation(
        adminIdOrMobile: String,
        onResult: (Boolean, PreapprovedOfficer?, String?) -> Unit
    ) {
        _isActivatingOfficer.value = true
        _adminActivationError.value = null
        viewModelScope.launch {
            val result = repository.lookupOfficerForActivation(adminIdOrMobile)
            _isActivatingOfficer.value = false
            if (result.isSuccess) {
                val officer = result.getOrNull()
                _verifiedPreapprovedOfficer.value = officer
                _adminActivationError.value = null
                onResult(true, officer, null)
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "अधिकारी खाते पडताळणी अयशस्वी"
                _adminActivationError.value = err
                _verifiedPreapprovedOfficer.value = null
                onResult(false, null, err)
            }
        }
    }

    fun clearVerifiedOfficer() {
        _verifiedPreapprovedOfficer.value = null
        _adminActivationError.value = null
    }

    fun activateOfficerAccount(
        adminIdOrMobile: String,
        otp: String,
        expectedOtp: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) {
        _isActivatingOfficer.value = true
        _adminActivationError.value = null
        viewModelScope.launch {
            val result = repository.activateOfficerAccount(adminIdOrMobile, otp, expectedOtp, password)
            _isActivatingOfficer.value = false
            if (result.isSuccess) {
                val activatedAdmin = result.getOrNull()
                _adminActivationError.value = null
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "अधिकारी खाते यशस्वीरीत्या सक्रिय झाले! कृपया आता पासवर्ड व OTP वापरून लॉगिन करा."
                    else
                        "Officer account activated successfully! Please login with your password & OTP."
                )
                _currentScreen.value = ScreenDestination.ADMIN_LOGIN
                onComplete(true)
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "सक्रियीकरण अयशस्वी"
                _adminActivationError.value = err
                showToast(err)
                onComplete(false)
            }
        }
    }

    fun loginAdmin(
        adminIdOrMobile: String,
        pass: String,
        otp: String = "",
        expectedOtp: String? = null,
        onComplete: (Boolean) -> Unit
    ) {
        _isAdminLoading.value = true
        _adminLoginError.value = null

        viewModelScope.launch {
            val result = repository.signInAdmin(adminIdOrMobile, pass, otp, expectedOtp)
            _isAdminLoading.value = false
            if (result.isSuccess) {
                val user = result.getOrNull()
                _adminUser.value = user
                _adminLoginError.value = null
                if (user != null) {
                    _selectedDistrictId.value = user.districtId
                    _selectedTalukaId.value = user.talukaId
                    _selectedGramPanchayatId.value = user.gramPanchayatId
                    repository.loadPanchayatDataForGp(user.gramPanchayatId)
                }
                _currentScreen.value = ScreenDestination.ADMIN_DASHBOARD
                showToast(
                    if (_language.value == AppLanguage.MARATHI)
                        "प्रशासकीय लॉगिन यशस्वी! आपले स्वागत आहे, ${user?.name ?: ""}"
                    else
                        "Admin login successful! Welcome, ${user?.name ?: ""}"
                )
                onComplete(true)
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "Login failed"
                _adminLoginError.value = err
                showToast(err)
                onComplete(false)
            }
        }
    }

    fun resetAdminPassword(adminIdOrEmail: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = repository.sendPasswordResetAdmin(adminIdOrEmail)
            if (result.isSuccess) {
                val msg = if (_language.value == AppLanguage.MARATHI)
                    "पासवर्ड रीसेट लिंक/सूचना पाठवली आहे. कृपया तपासा."
                else
                    "Password reset instructions sent. Please check."
                showToast(msg)
                onResult(true, msg)
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "Failed to send reset link."
                showToast(err)
                onResult(false, err)
            }
        }
    }

    fun logoutAdmin() {
        repository.signOutAdmin()
        _adminUser.value = null
        _adminLoginError.value = null
        _currentScreen.value = ScreenDestination.ROLE_SELECTION
        showToast(
            if (_language.value == AppLanguage.MARATHI)
                "प्रशासक लॉग आउट झाले."
            else
                "Admin logged out."
        )
    }

    // Admin CRUD Operations

    fun adminSavePanchayatProfile(profile: PanchayatProfile) {
        viewModelScope.launch {
            val success = repository.savePanchayatProfile(profile)
            showToast(
                if (success) {
                    if (_language.value == AppLanguage.MARATHI) "ग्रामपंचायत माहिती सेव्ह झाली!" else "Panchayat profile saved!"
                } else {
                    if (_language.value == AppLanguage.MARATHI) "माहिती अपडेट झाली (स्थानिक कॅशे)" else "Profile updated (Local Cache)"
                }
            )
        }
    }

    fun adminSaveWard(ward: WardEntity) {
        viewModelScope.launch {
            repository.saveWard(ward)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "प्रभाग माहिती सेव्ह झाली!" else "Ward saved successfully!"
            )
        }
    }

    fun adminDeleteWard(wardId: String) {
        viewModelScope.launch {
            repository.deleteWard(wardId)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "प्रभाग हटवण्यात आला." else "Ward deleted."
            )
        }
    }

    fun adminSaveWaterSchedule(schedule: WaterScheduleEntity) {
        viewModelScope.launch {
            repository.saveWaterSchedule(schedule)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "पाणीपुरवठा वेळापत्रक अपडेट झाले!" else "Water schedule updated!"
            )
        }
    }

    fun adminDeleteWaterSchedule(wardNumber: Int) {
        viewModelScope.launch {
            repository.deleteWaterSchedule(wardNumber)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "वेळापत्रक हटवले गेले." else "Schedule deleted."
            )
        }
    }

    fun adminSaveNotice(notice: NoticeEntity) {
        viewModelScope.launch {
            repository.saveNotice(notice)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "सूचना प्रसिद्ध झाली!" else "Notice published!"
            )
        }
    }

    fun adminDeleteNotice(noticeId: String) {
        viewModelScope.launch {
            repository.deleteNotice(noticeId)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "सूचना हटवली गेली." else "Notice deleted."
            )
        }
    }

    fun adminUpdateComplaint(complaintId: String, status: String, remarks: String, officer: String) {
        viewModelScope.launch {
            repository.updateComplaintAdminFields(complaintId, status, remarks, officer)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "तक्रार स्थिती अपडेट झाली!" else "Grievance status updated!"
            )
        }
    }

    fun adminDeleteComplaint(complaintId: String) {
        viewModelScope.launch {
            repository.deleteComplaint(complaintId)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "तक्रार हटवली गेली." else "Grievance deleted."
            )
        }
    }

    fun adminSaveOfficial(official: OfficialContactEntity) {
        viewModelScope.launch {
            repository.saveOfficial(official)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "अधिकारी / कर्मचारी माहिती सेव्ह झाली!" else "Official contact saved!"
            )
        }
    }

    fun adminDeleteOfficial(officialId: String) {
        viewModelScope.launch {
            repository.deleteOfficial(officialId)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "माहिती हटवली गेली." else "Official contact deleted."
            )
        }
    }

    fun adminSaveProject(project: DevelopmentProjectEntity) {
        viewModelScope.launch {
            repository.saveProject(project)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "विकास प्रकल्प सेव्ह झाला!" else "Project saved successfully!"
            )
        }
    }

    fun adminDeleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "प्रकल्प हटवला गेला." else "Project deleted."
            )
        }
    }

    fun adminSaveOnlineService(service: OnlineServiceItem) {
        viewModelScope.launch {
            repository.saveOnlineService(service)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "सेवा तपशील सेव्ह झाले!" else "Service details saved!"
            )
        }
    }

    fun adminDeleteOnlineService(serviceId: String) {
        viewModelScope.launch {
            repository.deleteOnlineService(serviceId)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "सेवा हटवली गेली." else "Service deleted."
            )
        }
    }

    fun adminSaveNotification(notification: NotificationItem) {
        viewModelScope.launch {
            repository.saveNotification(notification)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "सूचना/अलर्ट पाठवला गेला!" else "Notification alert sent!"
            )
        }
    }

    fun adminDeleteNotification(notifId: String) {
        viewModelScope.launch {
            repository.deleteNotification(notifId)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "सूचना हटवली गेली." else "Notification deleted."
            )
        }
    }

    fun adminSaveCitizen(citizen: UserProfile) {
        viewModelScope.launch {
            repository.saveCitizen(citizen)
            showToast(
                if (_language.value == AppLanguage.MARATHI) "नागरिक माहिती सेव्ह झाली!" else "Citizen details saved!"
            )
        }
    }

    fun adminSeedFirestore() {
        viewModelScope.launch {
            _isAdminLoading.value = true
            val success = repository.seedAllDataToFirestore()
            _isAdminLoading.value = false
            showToast(
                if (success) {
                    if (_language.value == AppLanguage.MARATHI)
                        "Firestore डेटा सिंक / इनिशियलायझेशन यशस्वी!"
                    else
                        "Firestore data initialized / seeded successfully!"
                } else {
                    if (_language.value == AppLanguage.MARATHI)
                        "स्थानिक डेटा तयार आहे (Firestore कनेक्शन तपासा)"
                    else
                        "Local data ready (Check Firestore connection)"
                }
            )
        }
    }

    // ================= AI CALL SYSTEM ACTIONS =================

    fun previewAiVoice(text: String, language: String, voiceId: String) {
        _isAiPreviewPlaying.value = true
        repository.previewAiVoice(text, language, voiceId)
    }

    fun stopAiVoicePreview() {
        _isAiPreviewPlaying.value = false
        repository.stopAiVoicePreview()
    }

    fun startAiCallCampaign(
        title: String,
        message: String,
        language: String,
        voiceId: String,
        targetAudience: String,
        customCitizenList: List<UserProfile> = emptyList(),
        onComplete: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {
            val allCitizens = repository.citizens.value
            val targetCitizens: List<UserProfile> = when (targetAudience) {
                "ALL" -> allCitizens
                "WARD_1" -> allCitizens.filter { it.wardNumber == 1 }
                "WARD_2" -> allCitizens.filter { it.wardNumber == 2 }
                "WARD_3" -> allCitizens.filter { it.wardNumber == 3 }
                "WARD_4" -> allCitizens.filter { it.wardNumber == 4 }
                "WARD_5" -> allCitizens.filter { it.wardNumber == 5 }
                "WARD_6" -> allCitizens.filter { it.wardNumber == 6 }
                "CUSTOM" -> if (customCitizenList.isNotEmpty()) customCitizenList else allCitizens.take(1)
                else -> allCitizens
            }

            // Ensure we have at least simulated recipient citizen profiles if list is small
            val finalRecipients = if (targetCitizens.isNotEmpty()) {
                targetCitizens
            } else {
                listOf(
                    UserProfile(fullName = "राजेश विष्णू सावंत", mobileNumber = "9876543210", wardNumber = 3),
                    UserProfile(fullName = "आनंद बापूराव शिंदे", mobileNumber = "9422112345", wardNumber = 1),
                    UserProfile(fullName = "सुनीता रमेश मोरे", mobileNumber = "9158098765", wardNumber = 4)
                )
            }

            val voiceName = when (voiceId) {
                "mr_female_1" -> "आरोही (मराठी महिला)"
                "mr_male_1" -> "अनिकेत (मराठी पुरुष)"
                "mr_female_2" -> "प्रिया (जलद सूचना)"
                "mr_male_2" -> "रोहन (मार्गदर्शक)"
                else -> "आरोही (मराठी महिला)"
            }

            val targetLabel = when (targetAudience) {
                "ALL" -> "सर्व पात्र नागरिक"
                "WARD_1" -> "प्रभाग १ नागरिक"
                "WARD_2" -> "प्रभाग २ नागरिक"
                "WARD_3" -> "प्रभाग ३ नागरिक"
                "WARD_4" -> "प्रभाग ४ नागरिक"
                "WARD_5" -> "प्रभाग ५ नागरिक"
                "WARD_6" -> "प्रभाग ६ नागरिक"
                "FARMERS" -> "शेतकरी गट"
                "WOMEN_SHG" -> "महिला बचत गट"
                "SENIOR_CITIZENS" -> "ज्येष्ठ नागरिक"
                "TAX_DEFAULTERS" -> "करधारक नागरिक"
                else -> "निवडक नागरिक"
            }

            val campaign = AiCallCampaign(
                id = "camp_${System.currentTimeMillis()}",
                announcementId = "ann_${System.currentTimeMillis()}",
                announcementTitle = title,
                announcementMessage = message,
                targetAudience = targetAudience,
                targetAudienceLabel = targetLabel,
                language = language,
                voiceId = voiceId,
                voiceName = voiceName,
                status = "IN_PROGRESS",
                totalRecipients = finalRecipients.size,
                startedAt = System.currentTimeMillis(),
                isDemoMode = repository.aiCallSettings.value.isDemoMode
            )

            _isAiBatchCallingActive.value = true
            _aiBatchProgress.value = Pair(0, finalRecipients.size)

            val (completedCampaign, logs) = repository.aiCallService.executeCallCampaign(
                campaign = campaign,
                recipients = finalRecipients,
                onProgressUpdate = { completed, total, citizenName, log ->
                    _aiBatchProgress.value = Pair(completed, total)
                    _currentCallingCitizenName.value = citizenName
                    viewModelScope.launch {
                        repository.saveAiCallLog(log)
                    }
                }
            )

            repository.saveAiCampaign(completedCampaign)
            _isAiBatchCallingActive.value = false
            _currentCallingCitizenName.value = ""

            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "AI कॉल मोहीम पूर्ण! ${completedCampaign.connectedCalls}/${completedCampaign.totalRecipients} कॉल्स जोडले गेले."
                else
                    "AI Call Campaign completed! ${completedCampaign.connectedCalls}/${completedCampaign.totalRecipients} connected."
            )
            onComplete(true)
        }
    }

    fun scheduleAiCall(
        title: String,
        message: String,
        language: String,
        voiceId: String,
        targetAudience: String,
        scheduledDate: String,
        scheduledTime: String,
        scheduledTimestamp: Long
    ) {
        viewModelScope.launch {
            val targetLabel = when (targetAudience) {
                "ALL" -> "सर्व नागरिक (१,८२०)"
                "WARD_1" -> "प्रभाग १ नागरिक (२९५)"
                "WARD_2" -> "प्रभाग २ नागरिक (३१०)"
                "WARD_3" -> "प्रभाग ३ नागरिक (३४०)"
                "WARD_4" -> "प्रभाग ४ नागरिक (२८०)"
                "WARD_5" -> "प्रभाग ५ नागरिक (३०५)"
                "WARD_6" -> "प्रभाग ६ नागरिक (२९०)"
                "FARMERS" -> "शेतकरी गट (४८०)"
                "WOMEN_SHG" -> "महिला बचत गट (३२०)"
                "SENIOR_CITIZENS" -> "ज्येष्ठ नागरिक (२१५)"
                "TAX_DEFAULTERS" -> "करधारक नागरिक (३९०)"
                else -> "निवडक नागरिक"
            }

            val estimatedRecipients = when (targetAudience) {
                "ALL" -> 1820
                "WARD_1" -> 295
                "WARD_2" -> 310
                "WARD_3" -> 340
                "WARD_4" -> 280
                "WARD_5" -> 305
                "WARD_6" -> 290
                "FARMERS" -> 480
                "WOMEN_SHG" -> 320
                "SENIOR_CITIZENS" -> 215
                "TAX_DEFAULTERS" -> 390
                else -> 10
            }

            val scheduledItem = AiScheduledCall(
                id = "sched_${System.currentTimeMillis()}",
                title = title,
                message = message,
                language = language,
                voiceId = voiceId,
                targetAudience = targetAudience,
                targetAudienceLabel = targetLabel,
                recipientCount = estimatedRecipients,
                scheduledDate = scheduledDate,
                scheduledTime = scheduledTime,
                scheduledTimestamp = scheduledTimestamp,
                status = "PENDING"
            )

            repository.saveAiScheduledCall(scheduledItem)
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "AI कॉल मोहीम $scheduledDate $scheduledTime साठी यशस्वीरित्या नियोजित केली!"
                else
                    "AI Call scheduled for $scheduledDate at $scheduledTime successfully!"
            )
        }
    }

    fun cancelScheduledAiCall(id: String) {
        viewModelScope.launch {
            repository.deleteAiScheduledCall(id)
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "नियोजित AI कॉल मोहीम रद्द करण्यात आली."
                else
                    "Scheduled AI Call cancelled."
            )
        }
    }

    fun saveAiCallSettings(settings: AiCallSettings) {
        viewModelScope.launch {
            repository.saveAiCallSettings(settings)
            showToast(
                if (_language.value == AppLanguage.MARATHI)
                    "AI कॉल प्रणाली सेटिंग्ज सेव्ह केल्या!"
                else
                    "AI Call System settings saved successfully!"
            )
        }
    }
}

