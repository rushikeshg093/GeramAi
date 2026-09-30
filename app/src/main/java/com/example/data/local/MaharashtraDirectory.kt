package com.example.data.local

data class District(
    val id: String,
    val nameMr: String,
    val nameEn: String
)

data class Taluka(
    val id: String,
    val districtId: String,
    val nameMr: String,
    val nameEn: String
)

data class GramPanchayatInfo(
    val id: String,
    val districtId: String,
    val talukaId: String,
    val nameMr: String,
    val nameEn: String,
    val pinCode: String = "443201",
    val totalWards: Int = 6,
    val officeAddressMr: String = "",
    val officeAddressEn: String = "",
    val phone: String = "+91 7264 242001",
    val email: String = ""
)

data class AuthorizedCitizen(
    val mobileNumber: String = "",
    val fullName: String = "",
    val fullNameEn: String = "",
    val districtId: String = "",
    val talukaId: String = "",
    val gramPanchayatId: String = "",
    val wardNumber: Int = 1,
    val verified: Boolean = true
)

object MaharashtraDirectory {
    val districts = listOf(
        District("buldhana", "बुलढाणा", "Buldhana"),
        District("pune", "पुणे", "Pune"),
        District("chhatrapati_sambhajinagar", "छत्रपती संभाजीनगर", "Chhatrapati Sambhajinagar"),
        District("nashik", "नाशिक", "Nashik"),
        District("satara", "सातारा", "Satara"),
        District("nagpur", "नागपूर", "Nagpur"),
        District("kolhapur", "कोल्हापूर", "Kolhapur"),
        District("amravati", "अमरावती", "Amravati"),
        District("solapur", "सोलापूर", "Solapur"),
        District("thane", "ठाणे", "Thane")
    )

    val talukas = listOf(
        // Buldhana
        Taluka("chikhli", "buldhana", "चिखली", "Chikhli"),
        Taluka("khamgaon", "buldhana", "खामगाव", "Khamgaon"),
        Taluka("mehkar", "buldhana", "मेहकर", "Mehkar"),
        Taluka("malkapur", "buldhana", "मलकापूर", "Malkapur"),

        // Pune
        Taluka("haveli", "pune", "हवेली", "Haveli"),
        Taluka("baramati", "pune", "बारामती", "Baramati"),
        Taluka("khed", "pune", "खेड (राजगुरुनगर)", "Khed (Rajgurunagar)"),
        Taluka("shirur", "pune", "शिरूर", "Shirur"),

        // Chhatrapati Sambhajinagar
        Taluka("khuldabad", "chhatrapati_sambhajinagar", "खुलताबाद", "Khuldabad"),
        Taluka("gangapur", "chhatrapati_sambhajinagar", "गंगापूर", "Gangapur"),
        Taluka("paithan", "chhatrapati_sambhajinagar", "पैठण", "Paithan"),

        // Nashik
        Taluka("niphad", "nashik", "निफाड", "Niphad"),
        Taluka("dindori", "nashik", "दिंडोरी", "Dindori"),
        Taluka("sinnar", "nashik", "सिन्नर", "Sinnar"),

        // Satara
        Taluka("koregaon", "satara", "कोरेगाव", "Koregaon"),
        Taluka("karad", "satara", "कराड", "Karad"),
        Taluka("wai", "satara", "वाई", "Wai"),

        // Nagpur
        Taluka("hingna", "nagpur", "हिंगणा", "Hingna"),
        Taluka("kamptee", "nagpur", "कामठी", "Kamptee"),

        // Kolhapur
        Taluka("karvir", "kolhapur", "करवीर", "Karvir"),
        Taluka("hatkanangle", "kolhapur", "हातकणंगले", "Hatkanangle"),

        // Amravati
        Taluka("achlapur", "amravati", "अचलपूर", "Achalpur"),
        Taluka("chandur_bazar", "amravati", "चांदूर बाजार", "Chandur Bazar"),

        // Solapur
        Taluka("pandharpur", "solapur", "पंढरपूर", "Pandharpur"),
        Taluka("barshi", "solapur", "बार्शी", "Barshi"),

        // Thane
        Taluka("bhiwandi", "thane", "भिवंडी", "Bhiwandi"),
        Taluka("kalyan", "thane", "कल्याण", "Kalyan")
    )

    val gramPanchayats = listOf(
        // Buldhana - Chikhli
        GramPanchayatInfo(
            id = "gp_palaskhed_daulat",
            districtId = "buldhana",
            talukaId = "chikhli",
            nameMr = "आदर्श ग्रामपंचायत पळसखेड दौलत",
            nameEn = "Grampanchayat Palaskhed Daulat",
            pinCode = "443201",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, मेन रोड, पळसखेड दौलत, ता. चिखली, जि. बुलढाणा",
            officeAddressEn = "Grampanchayat Office, Main Road, Palaskhed Daulat, Tal. Chikhli, Dist. Buldhana",
            phone = "+91 7264 242001",
            email = "contact@palaskheddaulatgp.gov.in"
        ),
        GramPanchayatInfo(
            id = "gp_sawaladbara",
            districtId = "buldhana",
            talukaId = "chikhli",
            nameMr = "ग्रामपंचायत सावळदबारा",
            nameEn = "Grampanchayat Sawaladbara",
            pinCode = "443201",
            totalWards = 5,
            officeAddressMr = "ग्रामपंचायत कार्यालय, बाजार चौक, सावळदबारा, ता. चिखली, जि. बुलढाणा",
            officeAddressEn = "Grampanchayat Office, Bazar Chowk, Sawaladbara, Tal. Chikhli, Dist. Buldhana",
            phone = "+91 7264 242045",
            email = "contact@sawaladbaragp.gov.in"
        ),
        GramPanchayatInfo(
            id = "gp_amrapur",
            districtId = "buldhana",
            talukaId = "chikhli",
            nameMr = "ग्रामपंचायत अमरापूर",
            nameEn = "Grampanchayat Amrapur",
            pinCode = "443201",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, अमरापूर, ता. चिखली, जि. बुलढाणा",
            officeAddressEn = "Grampanchayat Office, Amrapur, Tal. Chikhli, Dist. Buldhana",
            phone = "+91 7264 242088",
            email = "contact@amrapurgp.gov.in"
        ),

        // Buldhana - Khamgaon
        GramPanchayatInfo(
            id = "gp_rohinkhed",
            districtId = "buldhana",
            talukaId = "khamgaon",
            nameMr = "ग्रामपंचायत रोहिणखेड",
            nameEn = "Grampanchayat Rohinkhed",
            pinCode = "444303",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, रोहिणखेड, ता. खामगाव, जि. बुलढाणा",
            officeAddressEn = "Grampanchayat Office, Rohinkhed, Tal. Khamgaon, Dist. Buldhana",
            phone = "+91 7263 252110",
            email = "contact@rohinkhedgp.gov.in"
        ),

        // Pune - Haveli
        GramPanchayatInfo(
            id = "gp_wagholi",
            districtId = "pune",
            talukaId = "haveli",
            nameMr = "ग्रामपंचायत वाघोली",
            nameEn = "Grampanchayat Wagholi",
            pinCode = "412207",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, पुणे-नगर रोड, वाघोली, ता. हवेली, जि. पुणे",
            officeAddressEn = "Grampanchayat Office, Pune-Nagar Road, Wagholi, Tal. Haveli, Dist. Pune",
            phone = "+91 20 27051122",
            email = "info@wagholigp.gov.in"
        ),
        GramPanchayatInfo(
            id = "gp_loni_kalbhor",
            districtId = "pune",
            talukaId = "haveli",
            nameMr = "ग्रामपंचायत लोणी काळभोर",
            nameEn = "Grampanchayat Loni Kalbhor",
            pinCode = "412201",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, स्टेशन रोड, लोणी काळभोर, ता. हवेली, जि. पुणे",
            officeAddressEn = "Grampanchayat Office, Station Road, Loni Kalbhor, Tal. Haveli, Dist. Pune",
            phone = "+91 20 26913344",
            email = "info@lonikalbhorgp.gov.in"
        ),

        // Pune - Baramati
        GramPanchayatInfo(
            id = "gp_malegaon_budruk",
            districtId = "pune",
            talukaId = "baramati",
            nameMr = "ग्रामपंचायत माळेगाव बुद्रुक",
            nameEn = "Grampanchayat Malegaon Budruk",
            pinCode = "413115",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, माळेगाव बुद्रुक, ता. बारामती, जि. पुणे",
            officeAddressEn = "Grampanchayat Office, Malegaon Budruk, Tal. Baramati, Dist. Pune",
            phone = "+91 2112 254433",
            email = "info@malegaongp.gov.in"
        ),

        // Chhatrapati Sambhajinagar - Khuldabad
        GramPanchayatInfo(
            id = "gp_verul",
            districtId = "chhatrapati_sambhajinagar",
            talukaId = "khuldabad",
            nameMr = "ग्रामपंचायत वेरूळ (एलोरा)",
            nameEn = "Grampanchayat Verul (Ellora)",
            pinCode = "431102",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, कैलास लेणी परिसर, वेरूळ, ता. खुलताबाद, जि. छत्रपती संभाजीनगर",
            officeAddressEn = "Grampanchayat Office, Ellora Caves Area, Verul, Tal. Khuldabad, Dist. Chhatrapati Sambhajinagar",
            phone = "+91 2437 244555",
            email = "info@verulgp.gov.in"
        ),

        // Nashik - Niphad
        GramPanchayatInfo(
            id = "gp_ozar",
            districtId = "nashik",
            talukaId = "niphad",
            nameMr = "ग्रामपंचायत ओझर",
            nameEn = "Grampanchayat Ozar",
            pinCode = "422206",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, विमानतळ रोड, ओझर, ता. निफाड, जि. नाशिक",
            officeAddressEn = "Grampanchayat Office, Airport Road, Ozar, Tal. Niphad, Dist. Nashik",
            phone = "+91 2550 275522",
            email = "info@ozargp.gov.in"
        ),

        // Satara - Koregaon
        GramPanchayatInfo(
            id = "gp_koregaon_rural",
            districtId = "satara",
            talukaId = "koregaon",
            nameMr = "ग्रामपंचायत कोरेगाव ग्रामीण",
            nameEn = "Grampanchayat Koregaon Rural",
            pinCode = "415501",
            totalWards = 6,
            officeAddressMr = "ग्रामपंचायत कार्यालय, कोरेगाव ग्रामीण, ता. कोरेगाव, जि. सातारा",
            officeAddressEn = "Grampanchayat Office, Koregaon Rural, Tal. Koregaon, Dist. Satara",
            phone = "+91 2163 220033",
            email = "info@koregaongp.gov.in"
        )
    )

    // Pre-seeded authorized citizen registry (Maharashtra-wide voter/member records)
    val initialAuthorizedCitizens = listOf(
        // Palaskhed Daulat (Buldhana)
        AuthorizedCitizen(
            mobileNumber = "9876543210",
            fullName = "राजेश विष्णू सावंत",
            fullNameEn = "Rajesh Vishnu Sawant",
            districtId = "buldhana",
            talukaId = "chikhli",
            gramPanchayatId = "gp_palaskhed_daulat",
            wardNumber = 3,
            verified = true
        ),
        AuthorizedCitizen(
            mobileNumber = "9422001122",
            fullName = "संतोष लक्ष्मण पाटील",
            fullNameEn = "Santosh Laxman Patil",
            districtId = "buldhana",
            talukaId = "chikhli",
            gramPanchayatId = "gp_palaskhed_daulat",
            wardNumber = 1,
            verified = true
        ),
        AuthorizedCitizen(
            mobileNumber = "9822114455",
            fullName = "सौ. सुजाता आनंदराव पाटील",
            fullNameEn = "Sujata Anandrao Patil",
            districtId = "buldhana",
            talukaId = "chikhli",
            gramPanchayatId = "gp_palaskhed_daulat",
            wardNumber = 2,
            verified = true
        ),
        AuthorizedCitizen(
            mobileNumber = "9123456780",
            fullName = "सुनील वसंतराव देशमुख",
            fullNameEn = "Sunil Vasantrao Deshmukh",
            districtId = "buldhana",
            talukaId = "chikhli",
            gramPanchayatId = "gp_palaskhed_daulat",
            wardNumber = 4,
            verified = true
        ),

        // Sawaladbara (Buldhana)
        AuthorizedCitizen(
            mobileNumber = "9421098765",
            fullName = "रमेश बाबाराव जाधव",
            fullNameEn = "Ramesh Babarao Jadhav",
            districtId = "buldhana",
            talukaId = "chikhli",
            gramPanchayatId = "gp_sawaladbara",
            wardNumber = 2,
            verified = true
        ),

        // Wagholi (Pune)
        AuthorizedCitizen(
            mobileNumber = "9823012345",
            fullName = "अमोल विठ्ठल गायकवाड",
            fullNameEn = "Amol Vitthal Gaikwad",
            districtId = "pune",
            talukaId = "haveli",
            gramPanchayatId = "gp_wagholi",
            wardNumber = 2,
            verified = true
        ),
        AuthorizedCitizen(
            mobileNumber = "9881122334",
            fullName = "प्रियंका सचिन शिंदे",
            fullNameEn = "Priyanka Sachin Shinde",
            districtId = "pune",
            talukaId = "haveli",
            gramPanchayatId = "gp_wagholi",
            wardNumber = 1,
            verified = true
        ),

        // Malegaon Budruk (Pune)
        AuthorizedCitizen(
            mobileNumber = "9850123456",
            fullName = "दत्तात्रय तुकाराम तावरे",
            fullNameEn = "Dattatraya Tukaram Taware",
            districtId = "pune",
            talukaId = "baramati",
            gramPanchayatId = "gp_malegaon_budruk",
            wardNumber = 3,
            verified = true
        ),

        // Verul (Chhatrapati Sambhajinagar)
        AuthorizedCitizen(
            mobileNumber = "9423112233",
            fullName = "कैलास बाबुराव काळे",
            fullNameEn = "Kailas Baburao Kale",
            districtId = "chhatrapati_sambhajinagar",
            talukaId = "khuldabad",
            gramPanchayatId = "gp_verul",
            wardNumber = 3,
            verified = true
        ),

        // Ozar (Nashik)
        AuthorizedCitizen(
            mobileNumber = "9422778899",
            fullName = "संदीप भास्कर बोरसे",
            fullNameEn = "Sandeep Bhaskar Borse",
            districtId = "nashik",
            talukaId = "niphad",
            gramPanchayatId = "gp_ozar",
            wardNumber = 2,
            verified = true
        ),

        // Koregaon Rural (Satara)
        AuthorizedCitizen(
            mobileNumber = "9890112233",
            fullName = "महेश तानाजी घोरपडे",
            fullNameEn = "Mahesh Tanaji Ghorpade",
            districtId = "satara",
            talukaId = "koregaon",
            gramPanchayatId = "gp_koregaon_rural",
            wardNumber = 1,
            verified = true
        )
    )

    fun getAllDistricts(): List<District> {
        return districts
    }

    fun findAuthorizedCitizen(mobile: String): AuthorizedCitizen? {
        val clean = mobile.filter { it.isDigit() }.takeLast(10)
        return initialAuthorizedCitizens.find { it.mobileNumber == clean || it.mobileNumber == mobile }
    }

    fun getTalukasForDistrict(districtId: String): List<Taluka> {
        return talukas.filter { it.districtId == districtId }
    }

    fun getGramPanchayatsForTaluka(talukaId: String): List<GramPanchayatInfo> {
        return gramPanchayats.filter { it.talukaId == talukaId }
    }

    fun getGramPanchayatById(gpId: String): GramPanchayatInfo? {
        return gramPanchayats.find { it.id == gpId }
    }

    fun getDistrictById(districtId: String): District? {
        return districts.find { it.id == districtId }
    }

    fun getTalukaById(talukaId: String): Taluka? {
        return talukas.find { it.id == talukaId }
    }
}
