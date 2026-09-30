package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.local.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AiAssistanceResult(
    val responseText: String,
    val draftTitle: String? = null,
    val draftCategory: String? = null,
    val draftDescription: String? = null,
    val draftWard: Int = 1
)

object GeminiGrampanchayatService {
    private const val TAG = "GeminiGPService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    suspend fun getAiResponse(
        userMessage: String,
        currentLanguage: AppLanguage,
        citizenWard: Int
    ): AiAssistanceResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineKnowledgeResponse(userMessage, currentLanguage, citizenWard)
        }

        try {
            val systemPrompt = """
                You are 'ग्राममित्र AI' (GramMitra AI), an empathetic, knowledgeable AI citizen assistant for Grampanchayat Palaskhed Daulat (Taluka Chikhli, District Buldhana, Maharashtra, India).
                You speak fluent Marathi (मराठी) and English.
                Respond naturally in the language user asks (primarily Marathi if asked in Marathi).
                
                Your job:
                1. Assist citizens in understanding village problems, government schemes (घरकुल, जल जीवन मिशन, पीएम किसान, संजय गांधी निराधार योजना).
                2. Explain Grampanchayat certificates (जन्म, मृत्यू, विवाह दाखला, घरपट्टी, पाणीपट्टी).
                3. If the user mentions an issue or grievance (e.g. water supply, broken street lights, potholes/roads, garbage, drainage, sanitation):
                   - Provide a clear, helpful response explaining what steps Grampanchayat will take.
                   - ALSO draft a structured complaint formatted at the end of your response with this EXACT marker:
                   [DRAFT_COMPLAINT]
                   TITLE: <Short Marathi/English title>
                   CATEGORY: <One of: water, road, streetlight, garbage, drainage, sanitation, amenities, other>
                   DESCRIPTION: <Clear formal grievance text>
                   WARD: <Ward number 1 to 6, default $citizenWard>
                   [/DRAFT_COMPLAINT]
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray()
                val contentObj = JSONObject()
                val partsArray = JSONArray()

                val promptPart = JSONObject().apply {
                    put("text", "$systemPrompt\n\nUser Ward: $citizenWard\nCitizen Question: $userMessage")
                }
                partsArray.put(promptPart)
                contentObj.put("parts", partsArray)
                contentsArray.put(contentObj)
                put("contents", contentsArray)
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful || responseBody.isBlank()) {
                Log.w(TAG, "Gemini API error code ${response.code}: $responseBody")
                return@withContext getOfflineKnowledgeResponse(userMessage, currentLanguage, citizenWard)
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (text.isNullOrBlank()) {
                return@withContext getOfflineKnowledgeResponse(userMessage, currentLanguage, citizenWard)
            }

            return@withContext parseAiResponseWithDraft(text, citizenWard)
        } catch (e: Exception) {
            Log.e(TAG, "Error calling Gemini API", e)
            return@withContext getOfflineKnowledgeResponse(userMessage, currentLanguage, citizenWard)
        }
    }

    private fun parseAiResponseWithDraft(rawText: String, defaultWard: Int): AiAssistanceResult {
        var cleanText = rawText
        var draftTitle: String? = null
        var draftCategory: String? = null
        var draftDescription: String? = null
        var draftWard = defaultWard

        val startTag = "[DRAFT_COMPLAINT]"
        val endTag = "[/DRAFT_COMPLAINT]"

        if (rawText.contains(startTag) && rawText.contains(endTag)) {
            val startIndex = rawText.indexOf(startTag)
            val endIndex = rawText.indexOf(endTag)
            val draftBlock = rawText.substring(startIndex + startTag.length, endIndex).trim()
            cleanText = rawText.substring(0, startIndex).trim()

            val lines = draftBlock.lines()
            for (line in lines) {
                when {
                    line.startsWith("TITLE:", ignoreCase = true) -> {
                        draftTitle = line.substringAfter("TITLE:").trim()
                    }
                    line.startsWith("CATEGORY:", ignoreCase = true) -> {
                        draftCategory = line.substringAfter("CATEGORY:").trim().lowercase()
                    }
                    line.startsWith("DESCRIPTION:", ignoreCase = true) -> {
                        draftDescription = line.substringAfter("DESCRIPTION:").trim()
                    }
                    line.startsWith("WARD:", ignoreCase = true) -> {
                        draftWard = line.substringAfter("WARD:").trim().toIntOrNull() ?: defaultWard
                    }
                }
            }
        }

        return AiAssistanceResult(
            responseText = cleanText,
            draftTitle = draftTitle,
            draftCategory = draftCategory,
            draftDescription = draftDescription,
            draftWard = draftWard
        )
    }

    private fun getOfflineKnowledgeResponse(
        query: String,
        lang: AppLanguage,
        ward: Int
    ): AiAssistanceResult {
        val lower = query.lowercase()

        // 1. Water supply issue
        if (lower.contains("पाणी") || lower.contains("water") || lower.contains("नळ") || lower.contains("पाईप")) {
            val title = if (lang == AppLanguage.MARATHI) "प्रभाग $ward मध्ये पाणीपुरवठा समस्या" else "Water Supply Issue in Ward $ward"
            val desc = if (lang == AppLanguage.MARATHI)
                "आमच्या प्रभाग $ward मधील परिसरात वेळेवर पाणी येत नाही / पाईपलाईनमध्ये गळती झाली आहे. कृपया पाणीपुरवठा सुरळीत करावा."
            else
                "Irregular water supply / pipeline leakage reported in Ward $ward. Please restore regular drinking water supply."

            val reply = if (lang == AppLanguage.MARATHI)
                "नमस्कार! प्रभाग क्र. $ward मधील पाणीपुरवठ्याबाबत मी तुमची तक्रार नोंदवण्यासाठी मसुदा तयार केला आहे. पळसखेड दौलत ग्रामपंचायतीचे पाणीपुरवठा प्रमुख संतोष पाटील (मो. ९४२२००११२२) यांना थेट माहिती पाठवली जाईल. खालील 'तक्रार फॉर्ममध्ये भरा' बटनावर क्लिक करून आपण तात्काळ तक्रार दाखल करू शकता."
            else
                "Hello! I have prepared a draft grievance for the water supply issue in Ward $ward. The Grampanchayat Water Operator Santosh Patil (+91 9422001122) will be notified. Click 'Fill into Complaint Form' below to submit immediately."

            return AiAssistanceResult(
                responseText = reply,
                draftTitle = title,
                draftCategory = "water",
                draftDescription = desc,
                draftWard = ward
            )
        }

        // 2. Streetlight issue
        if (lower.contains("लाईट") || lower.contains("light") || lower.contains("अंधार") || lower.contains("बल्ब") || lower.contains("दिवा")) {
            val title = if (lang == AppLanguage.MARATHI) "प्रभाग $ward मध्ये स्ट्रीट लाईट दुरुस्ती" else "Street Light Repair in Ward $ward"
            val desc = if (lang == AppLanguage.MARATHI)
                "रस्त्यावरील स्ट्रीट लाईटचे बल्ब बंद असल्याने रात्रीच्या वेळी अंधार पसरला आहे. नवीन बल्ब बसवून वीज पुरवठा सुरू करावा."
            else
                "Street lights on the main lane are malfunctioning causing darkness at night. Please replace the LED bulbs."

            val reply = if (lang == AppLanguage.MARATHI)
                "ग्रामपंचायत वायरमन पथकाकडे ही तक्रार पाठवण्यासाठी मी मसुदा तयार केला आहे. साधारण २४ ते ४८ तासांत वायरमन पोलची तपासणी करून नवीन LED बल्ब बसवतील. खालील तक्रार फॉर्ममध्ये भरा बटनावर क्लिक करा."
            else
                "I have drafted a repair request for the street lighting team. Grampanchayat electricians usually resolve lighting issues within 24-48 hours. Tap the button below to submit."

            return AiAssistanceResult(
                responseText = reply,
                draftTitle = title,
                draftCategory = "streetlight",
                draftDescription = desc,
                draftWard = ward
            )
        }

        // 3. Garbage / Sanitation
        if (lower.contains("कचरा") || lower.contains("garbage") || lower.contains("गटार") || lower.contains("drainage") || lower.contains("घाण") || lower.contains("स्वच्छता")) {
            val title = if (lang == AppLanguage.MARATHI) "कचरा कुंडी व गटार स्वच्छता मोहीम" else "Garbage & Drainage Cleaning Request"
            val desc = if (lang == AppLanguage.MARATHI)
                "परिसरातील गटार तुंबले असून कचरा कुंडी भरली आहे. यामुळे डासांचा प्रादुर्भाव वाढत आहे. तातडीने स्वच्छता करावी."
            else
                "Overflowing garbage bin and clogged drainage in our locality. Please dispatch the sanitation vehicle immediately."

            val reply = if (lang == AppLanguage.MARATHI)
                "आरोग्य व स्वच्छता विभागामार्फत नियमित कचरा गाडी व फवारणी पथक पाठवले जाते. मी तुमची तक्रार मसुदा तयार केला आहे, खालील बटनाने तात्काळ तक्रार पेटीत नोंदवा."
            else
                "Grampanchayat sanitation staff and garbage collection vehicle will be deployed. I have generated a formal complaint draft below for you to file."

            return AiAssistanceResult(
                responseText = reply,
                draftTitle = title,
                draftCategory = "garbage",
                draftDescription = desc,
                draftWard = ward
            )
        }

        // 4. Certificates / Documents Info
        if (lower.contains("दाखला") || lower.contains("certificate") || lower.contains("जन्म") || lower.contains("मृत्यू") || lower.contains("विवाह") || lower.contains("कागदपत्र")) {
            val reply = if (lang == AppLanguage.MARATHI)
                "📄 **ग्रामपंचायत दाखले मिळवण्याची प्रक्रिया:**\n\n" +
                "१. **जन्म दाखला:** रुग्णालयाचा डिस्चार्ज कार्ड किंवा जन्म अहवाल, आई-वडिलांचे आधार कार्ड. (अर्ज फी: रु. २०/-)\n" +
                "२. **मृत्यू दाखला:** डॉक्टरांचे मृत्यू प्रमाणपत्र, मयताचे आधार कार्ड. (अर्ज फी: रु. २०/-)\n" +
                "३. **विवाह नोंदणी:** वर व वधूचे वयाचा पुरावा, आधार कार्ड, लग्नाची पत्रिका व ३ साक्षीदार. (अर्ज फी: रु. ५०/-)\n" +
                "४. **नाहरकत दाखला (NOC):** चालू वर्षाची घरपट्टी पावती व मालकी हक्क पुरावा.\n\n" +
                "💡 आपण ॲपमधील 'ऑनलाइन सेवा' मेनूमधून थेट अर्ज करू शकता आणि डिजिटल सहीचा दाखला डाउनलोड करू शकता!"
            else
                "📄 **Grampanchayat Certificate Guidelines:**\n\n" +
                "1. **Birth Certificate:** Hospital discharge summary, parents' Aadhar cards. (Fee: ₹20)\n" +
                "2. **Death Certificate:** Doctor's death report, deceased's Aadhar card. (Fee: ₹20)\n" +
                "3. **Marriage Certificate:** Age & residence proofs of bride & groom, wedding card, 3 witnesses. (Fee: ₹50)\n" +
                "4. **NOC Certificate:** Current year Property Tax receipt and ownership doc.\n\n" +
                "💡 You can apply directly through the 'Online Services' section of this app and receive a digitally certified copy!"

            return AiAssistanceResult(responseText = reply)
        }

        // 5. Tax Info
        if (lower.contains("घरपट्टी") || lower.contains("पाणीपट्टी") || lower.contains("कर") || lower.contains("tax")) {
            val reply = if (lang == AppLanguage.MARATHI)
                "🏠 **घरपट्टी व पाणीपट्टी ऑनलाइन भरणा:**\n\n" +
                "• ३० सप्टेंबरपूर्वी ऑनलाइन कर भरणाऱ्या नागरिकांना चालू वर्षाच्या घरपट्टीत **१०% विशेष सवलत** दिली जाते.\n" +
                "• ॲपमधील 'ऑनलाइन सेवा' -> 'घरपट्टी भरणा' पर्यायावर जाऊन आपला मालमत्ता क्रमांक टाकून UPI द्वारे तात्काळ कर भरणा करून अधिकृत डिजिटल पावती मिळवा."
            else
                "🏠 **Property & Water Tax Online Payment:**\n\n" +
                "• Citizens paying before 30th September receive an exclusive **10% rebate** on property tax.\n" +
                "• Head to 'Online Services' -> 'Property Tax Payment', enter your property ID, pay via UPI, and download the verified digital receipt instantly."

            return AiAssistanceResult(responseText = reply)
        }

        // General Welcome / Help
        val defaultText = if (lang == AppLanguage.MARATHI)
            "मी **ग्राममित्र AI सहाय्यक** आहे! 😊\n" +
            "आपण मला ग्रामपंचायतीच्या कोणत्याही सेवेबद्दल, विकासकामांबद्दल, पाणीपुरवठ्याबद्दल किंवा तक्रार नोंदवण्याबाबत विचारू शकता.\n\n" +
            "उदा:\n" +
            "• 'आमच्या रस्त्यावरील खड्डे बुजवण्यासाठी तक्रार करा'\n" +
            "• 'जन्म दाखल्यासाठी काय नियम आहेत?'\n" +
            "• 'पाण्याचे वेळापत्रक काय आहे?'\n" +
            "• 'घरपट्टी ऑनलाइन कशी भरायची?'"
        else
            "I am **GramMitra AI Assistant**! 😊\n" +
            "You can ask me anything regarding Grampanchayat civic services, government welfare schemes, water schedules, or filing citizen grievances.\n\n" +
            "Examples:\n" +
            "• 'Draft a complaint for broken road and potholes'\n" +
            "• 'What documents are required for birth certificate?'\n" +
            "• 'What is my ward water supply schedule?'\n" +
            "• 'How do I pay property tax online?'"

        return AiAssistanceResult(responseText = defaultText)
    }
}
