package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    MARATHI("mr", "Marathi", "मराठी"),
    ENGLISH("en", "English", "English")
}

enum class ComplaintCategory(
    val key: String,
    val titleMr: String,
    val titleEn: String,
    val iconName: String
) {
    WATER("water", "पाणीपुरवठा", "Water Supply", "water_drop"),
    ROAD("road", "रस्ता व खड्डे", "Road & Potholes", "add_road"),
    STREETLIGHT("streetlight", "स्ट्रीट लाईट", "Street Light", "lightbulb"),
    GARBAGE("garbage", "कचरा व्यवस्थापन", "Garbage & Waste", "delete"),
    DRAINAGE("drainage", "गटार / नाली", "Drainage & Sewer", "waves"),
    SANITATION("sanitation", "स्वच्छता", "Sanitation", "cleaning_services"),
    AMENITIES("amenities", "सार्वजनिक सुविधा", "Public Amenities", "apartment"),
    OTHER("other", "इतर तक्रार", "Other Grievances", "help_outline")
}

enum class ComplaintStatus(
    val statusMr: String,
    val statusEn: String
) {
    PENDING("प्रलंबित", "Pending"),
    IN_PROGRESS("प्रगतीपथावर", "In Progress"),
    RESOLVED("निवारण झाले", "Resolved"),
    REJECTED("नाकारले", "Rejected")
}

enum class NoticeCategory(
    val titleMr: String,
    val titleEn: String
) {
    ALL("सर्व", "All"),
    GRAMSABHA("ग्रामसभा", "Gramsabha"),
    TAX("कर आकारणी", "Tax & Dues"),
    DEVELOPMENT("विकासकामे", "Development"),
    EMERGENCY("आपत्कालीन", "Emergency"),
    TENDER("निविदा", "Tenders")
}

enum class ServiceType(
    val key: String,
    val titleMr: String,
    val titleEn: String,
    val fee: Int,
    val processingDays: String
) {
    BIRTH_CERTIFICATE("birth_cert", "जन्म दाखला", "Birth Certificate", 20, "३-५ दिवस / 3-5 Days"),
    DEATH_CERTIFICATE("death_cert", "मृत्यू दाखला", "Death Certificate", 20, "३-५ दिवस / 3-5 Days"),
    MARRIAGE_REGISTRATION("marriage_cert", "विवाह नोंदणी दाखला", "Marriage Certificate", 50, "७ दिवस / 7 Days"),
    PROPERTY_TAX("property_tax", "घरपट्टी ऑनलाइन भरणा", "Property Tax Payment", 0, "त्वरित / Instant"),
    WATER_TAX("water_tax", "पाणीपट्टी ऑनलाइन भरणा", "Water Tax Payment", 0, "त्वरित / Instant"),
    NOC_CERTIFICATE("noc_cert", "नाहरकत दाखला (NOC)", "NOC Certificate", 30, "५ दिवस / 5 Days"),
    BPL_CERTIFICATE("bpl_cert", "दारिद्र्यरेषा दाखला (BPL)", "BPL Certificate", 10, "३ दिवस / 3 Days")
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "",
    val fullName: String = "",
    val fullNameEn: String = "",
    val mobileNumber: String = "",
    val wardNumber: Int = 1,
    val aadharMasked: String = "",
    val address: String = "",
    val addressEn: String = "",
    val grampanchayatNameMr: String = "आदर्श ग्रामपंचायत पळसखेड दौलत",
    val grampanchayatNameEn: String = "Model Grampanchayat Palaskhed Daulat",
    val talukaMr: String = "चिखली",
    val talukaEn: String = "Chikhli",
    val districtMr: String = "बुलढाणा",
    val districtEn: String = "Buldhana",
    val districtId: String = "buldhana",
    val talukaId: String = "chikhli",
    val gramPanchayatId: String = "gp_palaskhed_daulat",
    val verified: Boolean = true,
    val isRegistered: Boolean = false
)

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String, // from ComplaintCategory key
    val wardNumber: Int,
    val locationDetail: String,
    val status: String = "PENDING", // PENDING, IN_PROGRESS, RESOLVED, REJECTED
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val officialRemarks: String = "",
    val assignedOfficer: String = "",
    val rating: Int = 0, // 1 to 5
    val citizenFeedback: String = "",
    val photoUri: String = "",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

@Entity(tableName = "notices")
data class NoticeEntity(
    @PrimaryKey val id: String,
    val titleMr: String,
    val titleEn: String,
    val descriptionMr: String,
    val descriptionEn: String,
    val category: String, // from NoticeCategory name
    val publishDate: String,
    val isUrgent: Boolean = false,
    val attachmentTitle: String = "",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

@Entity(tableName = "water_schedules")
data class WaterScheduleEntity(
    @PrimaryKey val wardNumber: Int,
    val wardNameMr: String,
    val wardNameEn: String,
    val morningTiming: String,
    val eveningTiming: String,
    val daysMr: String,
    val daysEn: String,
    val status: String = "NORMAL", // NORMAL, DELAYED, MAINTENANCE
    val statusNoteMr: String = "पाणीपुरवठा सुरळीत",
    val statusNoteEn: String = "Supply Normal",
    val operatorName: String = "संतोष पाटील",
    val operatorContact: String = "+91 9422001122",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

@Entity(tableName = "tanker_bookings")
data class TankerBookingEntity(
    @PrimaryKey val id: String,
    val applicantName: String,
    val mobileNumber: String,
    val wardNumber: Int,
    val deliveryAddress: String,
    val requiredDate: String,
    val timeSlot: String,
    val purpose: String,
    val status: String = "CONFIRMED", // CONFIRMED, DISPATCHED, DELIVERED
    val bookedAt: Long = System.currentTimeMillis(),
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

@Entity(tableName = "service_applications")
data class ServiceApplicationEntity(
    @PrimaryKey val id: String,
    val serviceType: String, // ServiceType key
    val applicantName: String,
    val mobileNumber: String,
    val wardNumber: Int,
    val details: String,
    val amountPaid: Int = 0,
    val status: String = "SUBMITTED", // SUBMITTED, VERIFYING, APPROVED, REJECTED
    val appliedDate: String,
    val certificateNumber: String = "",
    val remarks: String = "",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val draftComplaintTitle: String = "",
    val draftComplaintCategory: String = "",
    val draftComplaintDesc: String = "",
    val draftWard: Int = 1
)

data class OfficialContactEntity(
    val id: String = "",
    val nameMr: String = "",
    val nameEn: String = "",
    val designationMr: String = "",
    val designationEn: String = "",
    val phoneNumber: String = "",
    val wardOrDeptMr: String = "",
    val wardOrDeptEn: String = "",
    val imageRes: String = "",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

data class DevelopmentProjectEntity(
    val id: String = "",
    val titleMr: String = "",
    val titleEn: String = "",
    val sanctionedBudget: String = "",
    val duration: String = "",
    val status: String = "IN_PROGRESS", // PLANNED, ONGOING, IN_PROGRESS, RESOLVED, COMPLETED, ON_HOLD
    val location: String = "",
    val descriptionMr: String = "",
    val descriptionEn: String = "",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

data class NotificationItem(
    val id: String = "",
    val titleMr: String = "",
    val titleEn: String = "",
    val messageMr: String = "",
    val messageEn: String = "",
    val timestamp: String = "",
    val isUrgent: Boolean = false,
    val targetScreen: String = "NOTIFICATIONS", // "WATER_SERVICES", "NOTICES", "PANCHAYAT_INFO", "WARD_INFO", "PROJECTS", "ONLINE_SERVICES", "NOTIFICATIONS"
    val targetId: String = "",
    val wardNumber: Int? = null,
    val createdAtEpoch: Long = System.currentTimeMillis(),
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

data class PanchayatProfile(
    val id: String = "profile",
    val gramPanchayatId: String = "gp_palaskhed_daulat",
    val districtId: String = "buldhana",
    val talukaId: String = "chikhli",
    val nameMr: String = "आदर्श ग्रामपंचायत पळसखेड दौलत",
    val nameEn: String = "Model Grampanchayat Palaskhed Daulat",
    val talukaMr: String = "चिखली",
    val talukaEn: String = "Chikhli",
    val districtMr: String = "बुलढाणा",
    val districtEn: String = "Buldhana",
    val pinCode: String = "443201",
    val addressMr: String = "ग्रामपंचायत कार्यालय, मेन रोड, पळसखेड दौलत",
    val addressEn: String = "Grampanchayat Office, Main Road, Palaskhed Daulat",
    val phone: String = "+91 7264 242001",
    val email: String = "contact@palaskheddaulatgp.gov.in",
    val website: String = "https://palaskheddaulatgp.gov.in",
    val officeHoursMr: String = "सोम ते शनि, सकाळी १०:०० ते सायं ५:४५",
    val officeHoursEn: String = "Mon to Sat, 10:00 AM - 5:45 PM",
    val logoUrl: String = "",
    val totalPopulation: String = "८,४५०",
    val totalHouseholds: String = "१,८२०",
    val totalWards: Int = 6
)

data class WardEntity(
    val id: String = "ward_1",
    val wardNumber: Int = 1,
    val nameMr: String = "प्रभाग १ (जुनी वेस व बाजार पेठ)",
    val nameEn: String = "Ward 1 (Old Gate & Market Area)",
    val descriptionMr: String = "",
    val descriptionEn: String = "",
    val population: String = "१,४००",
    val representativeName: String = "सौ. सुजाता पाटील (सरपंच)",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

data class AdminUser(
    val uid: String = "",
    val email: String = "",
    val name: String = "Grampanchayat Admin",
    val role: String = "admin",
    val active: Boolean = true,
    val districtId: String = "buldhana",
    val talukaId: String = "chikhli",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

data class OnlineServiceItem(
    val id: String = "",
    val key: String = "",
    val titleMr: String = "",
    val titleEn: String = "",
    val descriptionMr: String = "",
    val descriptionEn: String = "",
    val requiredDocsMr: String = "",
    val requiredDocsEn: String = "",
    val fee: Int = 0,
    val processingDays: String = "३-५ दिवस",
    val active: Boolean = true,
    val applicationLink: String = "",
    val gramPanchayatId: String = "gp_palaskhed_daulat"
)

// ================= AI CALL SYSTEM ENTITIES =================

enum class AiVoiceType(
    val id: String,
    val nameMr: String,
    val nameEn: String,
    val personaMr: String,
    val personaEn: String,
    val gender: String
) {
    AROHI("mr_female_1", "आरोही (मराठी महिला)", "Arohi (Marathi Female)", "स्पष्ट, शांत व नम्र प्रशासकीय स्वर", "Clear, polite administrative tone", "FEMALE"),
    ANIKET("mr_male_1", "अनिकेत (मराठी पुरुष)", "Aniket (Marathi Male)", "गंभीर, अधिकृत व स्पष्ट पुरुष स्वर", "Authoritative, official voice", "MALE"),
    PRIYA("mr_female_2", "प्रिया (जलद सूचना स्वर)", "Priya (Urgent Alert Voice)", "आपत्कालीन व जलद घोषणांसाठी", "For emergency broadcasts", "FEMALE"),
    ROHAN("mr_male_2", "रोहन (मार्गदर्शक स्वर)", "Rohan (Informative Voice)", "शासकीय योजना व मार्गदर्शनासाठी", "For government schemes info", "MALE")
}

enum class AiTargetGroup(
    val key: String,
    val labelMr: String,
    val labelEn: String,
    val estimatedCitizens: Int
) {
    ALL("ALL", "सर्व पात्र नागरिक (ग्रामपंचायत हद्द)", "All Registered Citizens", 1820),
    WARD_1("WARD_1", "प्रभाग १ नागरिक (बाजार पेठ व जुनी वेस)", "Ward 1 Residents", 295),
    WARD_2("WARD_2", "प्रभाग २ नागरिक (शाळा परिसर व कॉलनी)", "Ward 2 Residents", 310),
    WARD_3("WARD_3", "प्रभाग ३ नागरिक (गांधी चौक व मंदिर गल्ली)", "Ward 3 Residents", 340),
    WARD_4("WARD_4", "प्रभाग ४ नागरिक (शनिवार पेठ)", "Ward 4 Residents", 280),
    WARD_5("WARD_5", "प्रभाग ५ नागरिक (शेतकरी वस्ती)", "Ward 5 Residents", 305),
    WARD_6("WARD_6", "प्रभाग ६ नागरिक (विकास कॉलनी)", "Ward 6 Residents", 290),
    FARMERS("FARMERS", "शेतकरी गट (कृषी योजना व पाणीपुरवठा)", "Farmers & Agriculture Group", 480),
    WOMEN_SHG("WOMEN_SHG", "महिला बचत गट सदस्य", "Women SHG Members", 320),
    SENIOR_CITIZENS("SENIOR_CITIZENS", "ज्येष्ठ नागरिक (६०+ वर्षे)", "Senior Citizens (60+)", 215),
    TAX_DEFAULTERS("TAX_DEFAULTERS", "घरपट्टी/पाणीपट्टी करधारक", "Property & Water Tax Payers", 390),
    CUSTOM("CUSTOM", "निवडक वैयक्तिक नागरिक", "Selected Citizens", 1)
}

enum class AiCallStatus(val labelMr: String, val labelEn: String) {
    CONNECTED("यशस्वी / जोडले (Connected)", "Connected"),
    NOT_ANSWERED("उत्तर दिले नाही (Unanswered)", "Unanswered"),
    BUSY("व्यस्त / बिझी (Busy)", "Busy"),
    FAILED("अयशस्वी (Failed)", "Failed"),
    IN_PROGRESS("कॉल सुरू आहे...", "In Progress"),
    SCHEDULED("नियोजित (Scheduled)", "Scheduled")
}

data class AiAnnouncement(
    val id: String = "",
    val title: String = "",
    val messageMr: String = "",
    val messageEn: String = "",
    val language: String = "mr", // "mr" or "en"
    val voiceId: String = "mr_female_1",
    val targetAudience: String = "ALL",
    val recipientCount: Int = 0,
    val estimatedDurationSec: Int = 45,
    val createdAt: Long = System.currentTimeMillis(),
    val createdBy: String = "Admin"
)

data class AiCallCampaign(
    val id: String = "",
    val announcementId: String = "",
    val announcementTitle: String = "",
    val announcementMessage: String = "",
    val targetAudience: String = "ALL",
    val targetAudienceLabel: String = "सर्व पात्र नागरिक",
    val language: String = "mr",
    val voiceId: String = "mr_female_1",
    val voiceName: String = "आरोही (मराठी महिला)",
    val status: String = "COMPLETED", // COMPLETED, IN_PROGRESS, SCHEDULED, CANCELLED, FAILED
    val totalRecipients: Int = 0,
    val connectedCalls: Int = 0,
    val unansweredCalls: Int = 0,
    val busyCalls: Int = 0,
    val failedCalls: Int = 0,
    val scheduledTime: Long? = null,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val isDemoMode: Boolean = true
)

data class AiCallLog(
    val id: String = "",
    val campaignId: String = "",
    val citizenName: String = "",
    val mobileNumber: String = "", // formatted or masked
    val wardNumber: Int = 1,
    val announcementTitle: String = "",
    val announcementMessage: String = "",
    val status: String = "CONNECTED", // CONNECTED, NOT_ANSWERED, BUSY, FAILED
    val durationSec: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isDemo: Boolean = true,
    val aiVoiceUsed: String = "आरोही (मराठी)",
    val notes: String = "AI ने अधिकृत घोषणा यशस्वीरित्या सांगितली."
)

data class AiScheduledCall(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val language: String = "mr",
    val voiceId: String = "mr_female_1",
    val targetAudience: String = "ALL",
    val targetAudienceLabel: String = "सर्व नागरिक",
    val recipientCount: Int = 0,
    val scheduledDate: String = "",
    val scheduledTime: String = "",
    val scheduledTimestamp: Long = 0L,
    val status: String = "PENDING", // PENDING, COMPLETED, CANCELLED
    val createdAt: Long = System.currentTimeMillis()
)

data class AiCallSettings(
    val defaultLanguage: String = "mr",
    val defaultVoiceId: String = "mr_female_1",
    val callingHoursStart: String = "09:00",
    val callingHoursEnd: String = "19:00",
    val maxCallsPerBatch: Int = 50,
    val maxRetries: Int = 2,
    val isAiCallingEnabled: Boolean = true,
    val isDemoMode: Boolean = true,
    val webhookEndpointUrl: String = ""
)

