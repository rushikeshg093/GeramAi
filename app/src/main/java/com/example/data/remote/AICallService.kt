package com.example.data.remote

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.data.local.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class AICallService(private val context: Context) {
    private val TAG = "AICallService"
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    init {
        try {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val marathiLocale = Locale("mr", "IN")
                    val result = tts?.setLanguage(marathiLocale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.language = Locale.ENGLISH
                    }
                    isTtsReady = true
                    Log.d(TAG, "TTS Initialized successfully")
                } else {
                    Log.w(TAG, "TTS Initialization failed: status $status")
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not initialize TextToSpeech: ${e.message}")
        }
    }

    /**
     * Preview AI Voice announcement using local TTS
     */
    fun speakPreview(text: String, language: String, voiceId: String) {
        if (!isTtsReady || tts == null) {
            Log.w(TAG, "TTS is not initialized yet")
            return
        }

        try {
            if (language == "mr") {
                val mrLocale = Locale("mr", "IN")
                tts?.setLanguage(mrLocale)
            } else {
                tts?.setLanguage(Locale.ENGLISH)
            }

            // Adjust pitch and rate depending on voice persona
            when (voiceId) {
                "mr_female_1" -> { // Arohi - Calm, polite
                    tts?.setPitch(1.1f)
                    tts?.setSpeechRate(0.95f)
                }
                "mr_male_1" -> { // Aniket - Authoritative
                    tts?.setPitch(0.85f)
                    tts?.setSpeechRate(0.9f)
                }
                "mr_female_2" -> { // Priya - Urgent alert
                    tts?.setPitch(1.2f)
                    tts?.setSpeechRate(1.1f)
                }
                "mr_male_2" -> { // Rohan - Informative
                    tts?.setPitch(0.95f)
                    tts?.setSpeechRate(1.0f)
                }
                else -> {
                    tts?.setPitch(1.0f)
                    tts?.setSpeechRate(1.0f)
                }
            }

            val prefix = if (language == "mr") {
                "नमस्कार! आदर्श ग्रामपंचायत पळसखेड दौलत यांच्या वतीने अधिकृत प्रशासकीय सूचना: "
            } else {
                "Hello! Official administrative announcement from Model Grampanchayat Palaskhed Daulat: "
            }

            val fullSpokenText = "$prefix $text"
            tts?.speak(fullSpokenText, TextToSpeech.QUEUE_FLUSH, null, "AI_PREVIEW_UTTERANCE")
        } catch (e: Exception) {
            Log.e(TAG, "Error in speakPreview: ${e.message}")
        }
    }

    /**
     * Stop preview audio
     */
    fun stopPreview() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping TTS: ${e.message}")
        }
    }

    /**
     * Formats official greeting and standard Gram Panchayat announcement script
     */
    fun generateOfficialAnnouncementScript(
        grampanchayatName: String,
        announcementTitle: String,
        message: String,
        language: String
    ): String {
        return if (language == "mr") {
            """
            नमस्कार, मी $grampanchayatName कार्यालयाचा AI कॉल सहाय्यक बोलत आहे.
            
            विषय: $announcementTitle
            
            सूचना:
            $message
            
            (टीप: हा कॉल केवळ ग्रामपंचायतीच्या अधिकृत माहिती व सूचनेसाठी आहे. कॉलवर कोणतीही तक्रार नोंदवून घेतली जात नाही. तक्रारीसाठी कृपया अधिकृत तक्रार निवारण कक्षात संपर्क साधावा.)
            
            धन्यवाद! जय हिंद, जय महाराष्ट्र!
            """.trimIndent()
        } else {
            """
            Hello, this is the AI Voice Call Assistant from $grampanchayatName.
            
            Subject: $announcementTitle
            
            Announcement:
            $message
            
            (Note: This call is strictly for official Gram Panchayat announcements and updates. Grievances are NOT accepted on calls; please submit complaints through the official Grievance Portal.)
            
            Thank you!
            """.trimIndent()
        }
    }

    /**
     * Simulate / execute call batch with step callbacks
     */
    suspend fun executeCallCampaign(
        campaign: AiCallCampaign,
        recipients: List<UserProfile>,
        onProgressUpdate: (completed: Int, total: Int, currentCitizen: String, log: AiCallLog) -> Unit
    ): Pair<AiCallCampaign, List<AiCallLog>> {
        val total = recipients.size
        val logs = mutableListOf<AiCallLog>()
        var connected = 0
        var unanswered = 0
        var busy = 0
        var failed = 0

        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        for ((index, citizen) in recipients.withIndex()) {
            val isSuccessProb = Math.random()
            val status = when {
                isSuccessProb < 0.80 -> "CONNECTED"
                isSuccessProb < 0.90 -> "NOT_ANSWERED"
                isSuccessProb < 0.96 -> "BUSY"
                else -> "FAILED"
            }

            val duration = when (status) {
                "CONNECTED" -> (35..55).random()
                "NOT_ANSWERED" -> 0
                "BUSY" -> 0
                "FAILED" -> 0
                else -> 0
            }

            when (status) {
                "CONNECTED" -> connected++
                "NOT_ANSWERED" -> unanswered++
                "BUSY" -> busy++
                "FAILED" -> failed++
            }

            val maskedMobile = if (citizen.mobileNumber.length >= 10) {
                val prefix = citizen.mobileNumber.substring(0, 5)
                "$prefix*****"
            } else {
                "98765*****"
            }

            val log = AiCallLog(
                id = "call_log_${System.currentTimeMillis()}_${index}",
                campaignId = campaign.id,
                citizenName = citizen.fullName,
                mobileNumber = maskedMobile,
                wardNumber = citizen.wardNumber,
                announcementTitle = campaign.announcementTitle,
                announcementMessage = campaign.announcementMessage,
                status = status,
                durationSec = duration,
                timestamp = System.currentTimeMillis(),
                isDemo = campaign.isDemoMode,
                aiVoiceUsed = campaign.voiceName,
                notes = when (status) {
                    "CONNECTED" -> "नागरिकाने कॉल स्वीकारला. AI ने अधिकृत घोषणा पूर्ण सांगितली."
                    "NOT_ANSWERED" -> "कॉलची रिंग वाजली परंतु नागरिकाने उत्तर दिले नाही."
                    "BUSY" -> "लाईन व्यस्त (Busy) होती."
                    "FAILED" -> "नेटवर्क समस्या / संपर्क क्षेत्राबाहेर."
                    else -> ""
                }
            )

            logs.add(log)
            onProgressUpdate(index + 1, total, citizen.fullName, log)

            // Small delay for realistic UX animation in Demo mode
            delay(120)
        }

        val completedCampaign = campaign.copy(
            status = "COMPLETED",
            connectedCalls = connected,
            unansweredCalls = unanswered,
            busyCalls = busy,
            failedCalls = failed,
            completedAt = System.currentTimeMillis()
        )

        return Pair(completedCampaign, logs)
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e(TAG, "Error shutting down TTS: ${e.message}")
        }
    }
}
