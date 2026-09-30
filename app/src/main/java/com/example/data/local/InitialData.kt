package com.example.data.local

object InitialData {
    val initialProfile = UserProfile(
        id = "primary_citizen",
        fullName = "राजेश विष्णू सावंत",
        fullNameEn = "Rajesh Vishnu Sawant",
        mobileNumber = "9876543210",
        wardNumber = 3,
        aadharMasked = "XXXX-XXXX-4829",
        address = "घर नं. ४५, मारुती मंदिर गल्ली, पळसखेड दौलत",
        addressEn = "House No. 45, Maruti Mandir Lane, Palaskhed Daulat",
        grampanchayatNameMr = "आदर्श ग्रामपंचायत पळसखेड दौलत",
        grampanchayatNameEn = "Model Grampanchayat Palaskhed Daulat",
        talukaMr = "चिखली",
        talukaEn = "Chikhli",
        districtMr = "बुलढाणा",
        districtEn = "Buldhana",
        isRegistered = true
    )

    val initialPanchayatProfile = PanchayatProfile(
        id = "profile",
        nameMr = "आदर्श ग्रामपंचायत पळसखेड दौलत",
        nameEn = "Model Grampanchayat Palaskhed Daulat",
        talukaMr = "चिखली",
        talukaEn = "Chikhli",
        districtMr = "बुलढाणा",
        districtEn = "Buldhana",
        pinCode = "443201",
        addressMr = "ग्रामपंचायत कार्यालय, मेन रोड, पळसखेड दौलत",
        addressEn = "Grampanchayat Office, Main Road, Palaskhed Daulat",
        phone = "+91 7264 242001",
        email = "contact@palaskheddaulatgp.gov.in",
        website = "https://palaskheddaulatgp.gov.in",
        officeHoursMr = "सोम ते शनि, सकाळी १०:०० ते सायं ५:४५",
        officeHoursEn = "Mon to Sat, 10:00 AM - 5:45 PM",
        logoUrl = "",
        totalPopulation = "८,४५०",
        totalHouseholds = "१,८२०",
        totalWards = 6
    )

    val initialWards = listOf(
        WardEntity("ward_1", 1, "प्रभाग १ (जुनी वेस व बाजार पेठ)", "Ward 1 (Old Gate & Market Area)", "बाजारपेठ व जुनी वस्ती परिसर", "Market and old settlement area", "१,४००", "सौ. सुजाता पाटील (सरपंच)"),
        WardEntity("ward_2", 2, "प्रभाग २ (जि. प. शाळा व शिक्षक कॉलनी)", "Ward 2 (ZP School & Colony)", "शाळा परिसर व निवासी कॉलनी", "School area and residential colony", "१,३५०", "श्री. संभाजी जाधव (उपसरपंच)"),
        WardEntity("ward_3", 3, "प्रभाग ३ (मारुती मंदिर व गांधी चौक)", "Ward 3 (Maruti Mandir & Gandhi Chowk)", "मध्यवर्ती गावठाण व मंदिर गल्ली", "Central village and temple area", "१,५००", "श्री. राहुल माने (सदस्य)"),
        WardEntity("ward_4", 4, "प्रभाग ४ (शनिवार पेठ व कुंभार गल्ली)", "Ward 4 (Shaniwar Peth & Kumbhar Galli)", "कुंभार समाज व व्यापारी पेठ", "Potter community & commercial lane", "१,३००", "सौ. अनिता शिंदे (सदस्य)"),
        WardEntity("ward_5", 5, "प्रभाग ५ (शेतकरी वस्ती व उपनगर)", "Ward 5 (Farmers Settlement & Suburb)", "शेतमळा वस्ती व बाहेरील विस्तार", "Farm settlements and outer extension", "१,४५०", "श्री. तानाजी पाटील (सदस्य)"),
        WardEntity("ward_6", 6, "प्रभाग ६ (नवीन विकास कॉलनी व MIDC हद्द)", "Ward 6 (Vikas Colony & Border)", "नवीन रहिवासी कॉलनी व सीमा भाग", "New housing layout and boundary area", "१,४५०", "सौ. रेखा कांबळे (सदस्य)")
    )

    val initialServices = listOf(
        OnlineServiceItem("srv_1", "birth_cert", "जन्म दाखला", "Birth Certificate", "ग्रामपंचायत हद्दीतील जन्माचा अधिकृत दाखला.", "Official birth registration certificate.", "हॉस्पिटल डिस्चार्ज कार्ड, पालकांचे आधार कार्ड", "Hospital discharge card, Parents Aadhaar card", 20, "३-५ दिवस", true),
        OnlineServiceItem("srv_2", "death_cert", "मृत्यू दाखला", "Death Certificate", "अधिकृत मृत्यू नोंदणी दाखला.", "Official death registration certificate.", "डॉक्टर प्रमाणपत्र, स्मशानभूमी पावती", "Doctor certificate, Cremation receipt", 20, "३-५ दिवस", true),
        OnlineServiceItem("srv_3", "marriage_cert", "विवाह नोंदणी दाखला", "Marriage Certificate", "ग्रामपंचायत विवाह निबंधक दाखला.", "Grampanchayat marriage registration certificate.", "वर-वधू आधार कार्ड, लग्नपत्रिका, साक्षीदार", "Bride & Groom Aadhaar, Wedding card, Witnesses", 50, "७ दिवस", true),
        OnlineServiceItem("srv_4", "property_tax", "घरपट्टी ऑनलाइन भरणा", "Property Tax Payment", "चालू आर्थिक वर्षातील घरपट्टी थेट ऑनलाइन भरा.", "Pay annual property tax online directly.", "मालमत्ता क्रमांक / घरपट्टी पावती", "Property number / Assessment receipt", 0, "त्वरित", true),
        OnlineServiceItem("srv_5", "water_tax", "पाणीपट्टी ऑनलाइन भरणा", "Water Tax Payment", "वार्षिक नळ कनेक्शन पाणीपट्टी भरणा.", "Annual tap water connection tax payment.", "ग्राहक क्रमांक / नळ जोडणी पावती", "Consumer number / Tap connection receipt", 0, "त्वरित", true),
        OnlineServiceItem("srv_6", "noc_cert", "नाहरकत दाखला (NOC)", "NOC Certificate", "वीज जोडणी किंवा बांधकामासाठी ग्रामपंचायत NOC.", "Grampanchayat NOC for electricity or construction.", "जागेचा ७/१२ किंवा ८-अ, मालमत्ता पावती", "7/12 extract or 8-A, Property receipt", 30, "५ दिवस", true),
        OnlineServiceItem("srv_7", "bpl_cert", "दारिद्र्यरेषा दाखला (BPL)", "BPL Certificate", "शासकीय योजनांसाठी BPL यादीतील दाखला.", "BPL list certificate for government schemes.", "रेशन कार्ड, आधार कार्ड, उत्पन्न दाखला", "Ration card, Aadhaar card, Income certificate", 10, "३ दिवस", true)
    )

    val officials = listOf(
        OfficialContactEntity(
            id = "off_1",
            nameMr = "सौ. सुजाता आनंदराव पाटील",
            nameEn = "Mrs. Sujata Anandrao Patil",
            designationMr = "सरपंच (Sarpanch)",
            designationEn = "Sarpanch (Village Head)",
            phoneNumber = "+91 9822114455",
            wardOrDeptMr = "प्रशासकीय प्रमुख",
            wardOrDeptEn = "Administrative Head"
        ),
        OfficialContactEntity(
            id = "off_2",
            nameMr = "श्री. संभाजी मारुती जाधव",
            nameEn = "Mr. Sambhaji Maruti Jadhav",
            designationMr = "उपसरपंच (Up-Sarpanch)",
            designationEn = "Deputy Sarpanch",
            phoneNumber = "+91 9822336677",
            wardOrDeptMr = "प्रभाग क्र. २ व ४",
            wardOrDeptEn = "Ward No. 2 & 4"
        ),
        OfficialContactEntity(
            id = "off_3",
            nameMr = "श्री. गणेश बाळकृष्ण जोशी",
            nameEn = "Mr. Ganesh Balkrishna Joshi",
            designationMr = "ग्रामविकास अधिकारी / ग्रामसेवक",
            designationEn = "Village Development Officer (Gramsevak)",
            phoneNumber = "+91 9423889900",
            wardOrDeptMr = "प्रशासकीय व महसूल",
            wardOrDeptEn = "Administration & Records"
        ),
        OfficialContactEntity(
            id = "off_4",
            nameMr = "श्री. दीपक तानाजी देसाई",
            nameEn = "Mr. Deepak Tanaji Desai",
            designationMr = "तलाठी (Talathi)",
            designationEn = "Revenue Officer (Talathi)",
            phoneNumber = "+91 9422556611",
            wardOrDeptMr = "जमीन व महसूल",
            wardOrDeptEn = "Land & Revenue"
        ),
        OfficialContactEntity(
            id = "off_5",
            nameMr = "श्री. संतोष विलास पाटील",
            nameEn = "Mr. Santosh Vilas Patil",
            designationMr = "पाणीपुरवठा प्रमुख व वायरमन",
            designationEn = "Water Supply & Electrician",
            phoneNumber = "+91 9422001122",
            wardOrDeptMr = "पाणी व वीज विभाग",
            wardOrDeptEn = "Water & Utilities"
        ),
        OfficialContactEntity(
            id = "off_6",
            nameMr = "सौ. वंदना प्रकाश कांबळे",
            nameEn = "Mrs. Vandana Prakash Kamble",
            designationMr = "आरोग्य सेविका / आशा वर्कर",
            designationEn = "Health Worker (ASHA)",
            phoneNumber = "+91 9890223344",
            wardOrDeptMr = "आरोग्य व स्वच्छता",
            wardOrDeptEn = "Health & Sanitation"
        )
    )

    val developmentProjects = listOf(
        DevelopmentProjectEntity(
            id = "proj_1",
            titleMr = "प्रभाग क्र. २ व ३ सिमेंट कॉंक्रिट रस्ता डांबरीकरण",
            titleEn = "Ward 2 & 3 Concrete Road Construction",
            sanctionedBudget = "रु. २५,००,०००/- (१५ वा वित्त आयोग)",
            duration = "३ महिने (प्रगतीपथावर)",
            status = "IN_PROGRESS",
            location = "वॉर्ड २ व ३ मुख्य रस्ता",
            descriptionMr = "रस्त्याचे काँक्रीटीकरण व दोन्ही बाजूला गटार बांधकाम.",
            descriptionEn = "Concrete road laying and drainage on both sides."
        ),
        DevelopmentProjectEntity(
            id = "proj_2",
            titleMr = "जल जीवन मिशन अंतर्गत नवीन शुद्ध पाणी जलशुद्धीकरण प्रकल्प",
            titleEn = "Jal Jeevan Mission Water Purification Plant",
            sanctionedBudget = "रु. ४५,००,०००/-",
            duration = "६ महिने (पूर्ण झाले)",
            status = "RESOLVED",
            location = "जलशुद्धीकरण केंद्र, पळसखेड दौलत टेकडी",
            descriptionMr = "दररोज ५ लाख लिटर क्षमतेचे नवीन फिल्टर प्लांट.",
            descriptionEn = "New filtration plant with 500k liters daily capacity."
        ),
        DevelopmentProjectEntity(
            id = "proj_3",
            titleMr = "गावात १०० नवीन सौर पथदिवे (Solar Streetlights) बसवणे",
            titleEn = "Installation of 100 Solar Streetlights",
            sanctionedBudget = "रु. १०,००,०००/-",
            duration = "२ महिने (प्रलंबित तपासणी)",
            status = "PENDING",
            location = "गावातील प्रमुख रस्ते व चौक",
            descriptionMr = "स्वयंचलित ऑन/ऑफ सेन्सरयुक्त सौर पथदिवे.",
            descriptionEn = "Automatic sensor-based solar streetlights on main lanes."
        )
    )

    val initialNotifications = listOf(
        NotificationItem(
            id = "notif_1",
            titleMr = "स्वातंत्र्य दिन ग्रामसभा उद्या सकाळी १०:०० वाजता",
            titleEn = "Special Independence Day Gramsabha Tomorrow at 10 AM",
            messageMr = "सर्व ग्रामस्थांनी ग्रामपंचायत प्रांगणात वेळेवर उपस्थित राहावे.",
            messageEn = "All citizens are requested to attend Gramsabha meeting on time.",
            timestamp = "२ तास आधी",
            isUrgent = true
        ),
        NotificationItem(
            id = "notif_2",
            titleMr = "घरपट्टी १०% सवलत अंतिम मुदत जवळ आली आहे",
            titleEn = "Property Tax 10% Rebate Deadline Approaching",
            messageMr = "३० सप्टेंबरपूर्वी ऑनलाइन कर भरून सवलतीचा लाभ घ्या.",
            messageEn = "Pay online before Sept 30 to claim 10% discount.",
            timestamp = "काल",
            isUrgent = false
        ),
        NotificationItem(
            id = "notif_3",
            titleMr = "प्रभाग ३ स्ट्रीट लाईट तक्रार निवारण झाले",
            titleEn = "Ward 3 Streetlight Complaint Resolved",
            messageMr = "आपली तक्रार क्र. GP-2026-0042 दुरुस्ती पूर्ण झाली आहे.",
            messageEn = "Your grievance GP-2026-0042 has been resolved successfully.",
            timestamp = "२ दिवसांपूर्वी",
            isUrgent = false
        )
    )

    val initialComplaints = listOf(
        ComplaintEntity(
            id = "GP-2026-0042",
            title = "मारुती मंदिर चौकातील स्ट्रीट लाईट बंद",
            description = "गेल्या ३ दिवसांपासून मारुती मंदिर चौकातील दोन स्ट्रीट लाईट बंद आहेत, रात्री अंधारामुळे नागरिकांना त्रास होत आहे.",
            category = "streetlight",
            wardNumber = 3,
            locationDetail = "मारुती मंदिर चौक, पोल क्र. १२",
            status = "RESOLVED",
            createdAt = System.currentTimeMillis() - 1000L * 60 * 60 * 48,
            updatedAt = System.currentTimeMillis() - 1000L * 60 * 60 * 12,
            officialRemarks = "वायरमन संतोष पाटील यांनी नवीन LED बल्ब बसवून वायर जोडणी केली आहे. लाईट सुरू झाली आहे.",
            assignedOfficer = "संतोष पाटील (वायरमन)",
            rating = 5,
            citizenFeedback = "खूप जलद काम झाले, धन्यवाद ग्रामपंचायत!"
        ),
        ComplaintEntity(
            id = "GP-2026-0058",
            title = "शाळेजवळील मुख्य पाईपलाईन गळती",
            description = "जि. प. प्राथमिक शाळेच्या पाठीमागील मुख्य पाणी पाईपलाईनमधून मोठ्या प्रमाणावर पाणी वाया जात आहे व रस्ता चिखलमय झाला आहे.",
            category = "water",
            wardNumber = 2,
            locationDetail = "जि. प. शाळा परिसर, वॉर्ड २",
            status = "IN_PROGRESS",
            createdAt = System.currentTimeMillis() - 1000L * 60 * 60 * 20,
            updatedAt = System.currentTimeMillis() - 1000L * 60 * 60 * 4,
            officialRemarks = "तक्रार प्राप्त झाली. दुरुस्ती पथक जागेवर पाठवले असून आज दुपारी ३ पर्यंत दुरुस्ती पूर्ण होईल.",
            assignedOfficer = "पाणीपुरवठा विभाग",
            rating = 0
        ),
        ComplaintEntity(
            id = "GP-2026-0071",
            title = "वॉर्ड ४ मधील कचरा कुंडी स्वच्छता",
            description = "शनिवार पेठ रस्त्यावरील कचरा कुंडी भरली असून दुर्गंधी सुटली आहे, कृपया कचरा गाडी पाठवून स्वच्छता करावी.",
            category = "garbage",
            wardNumber = 4,
            locationDetail = "शनिवार पेठ, गणपती मंदिर जवळ",
            status = "PENDING",
            createdAt = System.currentTimeMillis() - 1000L * 60 * 60 * 6,
            updatedAt = System.currentTimeMillis() - 1000L * 60 * 60 * 6,
            officialRemarks = "तक्रार नोंदवली गेली आहे. स्वच्छता निरीक्षकांना सूचना दिली आहे.",
            assignedOfficer = "स्वच्छता विभाग",
            rating = 0
        )
    )

    val initialNotices = listOf(
        NoticeEntity(
            id = "NOT-2026-01",
            titleMr = "स्वातंत्र्य दिन विशेष ग्रामसभा बैठक सूचना",
            titleEn = "Special Independence Day Gramsabha Meeting",
            descriptionMr = "सर्व ग्रामस्थांना कळविण्यात येते की येत्या १५ ऑगस्ट रोजी सकाळी १०:०० वाजता ग्रामपंचायत प्रांगणात विशेष ग्रामसभा आयोजित केली आहे. यामध्ये विकास योजनांचे वाचन व लाभार्थ्यांची निवड केली जाईल. सर्वांनी उपस्थित राहावे.",
            descriptionEn = "All villagers are informed that a special Gramsabha will be held on 15th August at 10:00 AM at the Grampanchayat premises for development scheme reviews and beneficiary selections.",
            category = "GRAMSABHA",
            publishDate = "१२ ऑगस्ट २०२६",
            isUrgent = true,
            attachmentTitle = "ग्रामसभा_विषयपत्रिका_२०२६.pdf"
        ),
        NoticeEntity(
            id = "NOT-2026-02",
            titleMr = "घरपट्टी व पाणीपट्टी ऑनलाइन भरणा विशेष १०% सवलत",
            titleEn = "Special 10% Rebate on Property & Water Tax Online Payment",
            descriptionMr = "सन २०२६-२७ च्या चालू आर्थिक वर्षातील घरपट्टी व पाणीपट्टी ३० सप्टेंबरपूर्वी ऑनलाइन भरणाऱ्या नागरिकांना करात १०% सवलत दिली जाईल. ॲपमधून घरबसल्या कराचा भरणा करा.",
            descriptionEn = "Citizens paying Property & Water Tax for FY 2026-27 online before 30th September will receive a 10% rebate. Pay from home via this app.",
            category = "TAX",
            publishDate = "०५ ऑगस्ट २०२६",
            isUrgent = false,
            attachmentTitle = "कर_सवलत_परिपत्रक.pdf"
        ),
        NoticeEntity(
            id = "NOT-2026-03",
            titleMr = "जल जीवन मिशन अंतर्गत पाईपलाईन दुरुस्तीमुळे पाणी बंद",
            titleEn = "Water Supply Interruption due to Jal Jeevan Mission Repairs",
            descriptionMr = "दि. २० ऑगस्ट रोजी प्रभाग क्र. १ आणि ३ मध्ये मुख्य टाकी दुरुस्तीमुळे सकाळचा पाणीपुरवठा बंद राहील. नागरिकांनी आदल्या दिवशी पाण्याचा साठा करून ठेवावा.",
            descriptionEn = "On 20th August, morning water supply will be halted in Ward 1 & 3 for overhead tank maintenance. Please store sufficient water.",
            category = "EMERGENCY",
            publishDate = "०१ ऑगस्ट २०२६",
            isUrgent = true,
            attachmentTitle = "दुरुस्ती_वेळापत्रक.pdf"
        ),
        NoticeEntity(
            id = "NOT-2026-04",
            titleMr = "ग्रामपंचायत सिमेंट कॉंक्रिट रस्ता डांबरीकरण निविदा",
            titleEn = "Grampanchayat Tender for Cement Concrete Road Construction",
            descriptionMr = "प्रभाग क्र. ५ व ६ मधील अंतर्गत रस्त्यांच्या काँक्रीटीकरणासाठी ई-निविदा मागविण्यात येत आहेत. अधिक माहितीसाठी ग्रामपंचायत कार्यालयाशी संपर्क साधावा.",
            descriptionEn = "E-tenders are invited for concrete road development in Ward 5 & 6. Visit office for detailed tender document.",
            category = "TENDER",
            publishDate = "२८ जुलै २०२६",
            isUrgent = false,
            attachmentTitle = "निविदा_फॉर्म_रस्ता.pdf"
        )
    )

    val initialWaterSchedules = listOf(
        WaterScheduleEntity(
            wardNumber = 1,
            wardNameMr = "प्रभाग १ (जुनी वेस व बाजार पेठ)",
            wardNameEn = "Ward 1 (Old Gate & Market Area)",
            morningTiming = "सकाळी ६:०० ते ७:३०",
            eveningTiming = "संध्याकाळी ५:०० ते ६:३०",
            daysMr = "दररोज (सोमवार ते रविवार)",
            daysEn = "Daily (Mon to Sun)",
            status = "NORMAL",
            statusNoteMr = "पाणीपुरवठा सुरळीत चालू आहे",
            statusNoteEn = "Supply Running Smoothly"
        ),
        WaterScheduleEntity(
            wardNumber = 2,
            wardNameMr = "प्रभाग २ (जि. प. शाळा व शिक्षक कॉलनी)",
            wardNameEn = "Ward 2 (ZP School & Colony)",
            morningTiming = "सकाळी ७:३० ते ९:००",
            eveningTiming = "संध्याकाळी ६:३० ते ८:००",
            daysMr = "दररोज (सोमवार ते रविवार)",
            daysEn = "Daily (Mon to Sun)",
            status = "NORMAL",
            statusNoteMr = "पाईपलाईन दुरुस्तीनंतर सुरळीत",
            statusNoteEn = "Normal after pipeline fix"
        ),
        WaterScheduleEntity(
            wardNumber = 3,
            wardNameMr = "प्रभाग ३ (मारुती मंदिर व गांधी चौक)",
            wardNameEn = "Ward 3 (Maruti Mandir & Gandhi Chowk)",
            morningTiming = "सकाळी ५:३० ते ७:००",
            eveningTiming = "संध्याकाळी ४:३० ते ६:००",
            daysMr = "दररोज (सोमवार ते रविवार)",
            daysEn = "Daily (Mon to Sun)",
            status = "NORMAL",
            statusNoteMr = "पूर्ण दाबाने पाणीपुरवठा चालू",
            statusNoteEn = "Full Pressure Supply Active"
        ),
        WaterScheduleEntity(
            wardNumber = 4,
            wardNameMr = "प्रभाग ४ (शनिवार पेठ व कुंभार गल्ली)",
            wardNameEn = "Ward 4 (Shaniwar Peth & Kumbhar Galli)",
            morningTiming = "सकाळी ७:०० ते ८:३०",
            eveningTiming = "संध्याकाळी ६:०० ते ७:३०",
            daysMr = "सोम, बुध, शुक्र, रविवारी",
            daysEn = "Mon, Wed, Fri, Sunday",
            status = "NORMAL",
            statusNoteMr = "वेळापत्रकानुसार चालू",
            statusNoteEn = "Running on Schedule"
        ),
        WaterScheduleEntity(
            wardNumber = 5,
            wardNameMr = "प्रभाग ५ (शेतकरी वस्ती व उपनगर)",
            wardNameEn = "Ward 5 (Farmers Settlement & Suburb)",
            morningTiming = "सकाळी ६:३० ते ८:००",
            eveningTiming = "संध्याकाळी ५:३० ते ७:००",
            daysMr = "मंगळ, गुरू, शनिवारी",
            daysEn = "Tue, Thu, Saturday",
            status = "MAINTENANCE",
            statusNoteMr = "मोटर देखभाल चालू, टँकर उपलब्ध",
            statusNoteEn = "Pump Maintenance, Tanker Available"
        ),
        WaterScheduleEntity(
            wardNumber = 6,
            wardNameMr = "प्रभाग ६ (नवीन विकास कॉलनी व MIDC हद्द)",
            wardNameEn = "Ward 6 (Vikas Colony & Border)",
            morningTiming = "सकाळी ८:०० ते ९:३०",
            eveningTiming = "संध्याकाळी ७:०० ते ८:३०",
            daysMr = "दररोज (सोमवार ते रविवार)",
            daysEn = "Daily (Mon to Sun)",
            status = "NORMAL",
            statusNoteMr = "सुरळीत चालू",
            statusNoteEn = "Supply Normal"
        )
    )

    val initialApplications = listOf(
        ServiceApplicationEntity(
            id = "APP-2026-0129",
            serviceType = "birth_cert",
            applicantName = "राजेश विष्णू सावंत",
            mobileNumber = "9876543210",
            wardNumber = 3,
            details = "बाळाचे नाव: आरव राजेश सावंत, जन्म तारीख: १४/०२/२०२६, रुग्णालय: प्राथमिक आरोग्य केंद्र पळसखेड दौलत",
            amountPaid = 20,
            status = "APPROVED",
            appliedDate = "०२ ऑगस्ट २०२६",
            certificateNumber = "GP-BC-2026/8941",
            remarks = "नोंदणी रजिस्टरमध्ये पडताळणी झाली. डिजिटल स्वाक्षरीसह दाखला तयार आहे."
        ),
        ServiceApplicationEntity(
            id = "APP-2026-0144",
            serviceType = "property_tax",
            applicantName = "राजेश विष्णू सावंत",
            mobileNumber = "9876543210",
            wardNumber = 3,
            details = "मालमत्ता क्र. ३/०४५, घरपट्टी वर्ष २०२६-२७, आकारणी रक्कम: रु. १,२००/- (१०% सवलतीनंतर रु. १,०८०/- भरणा यशस्वी)",
            amountPaid = 1080,
            status = "APPROVED",
            appliedDate = "०६ ऑगस्ट २०२६",
            certificateNumber = "TAX-REC-2026-902",
            remarks = "ऑनलाइन कर भरणा पावती यशस्वीरित्या जारी केली."
        )
    )

    // ================= AI CALL SYSTEM INITIAL DATA =================

    val initialAiCallSettings = AiCallSettings(
        defaultLanguage = "mr",
        defaultVoiceId = "mr_female_1",
        callingHoursStart = "09:00",
        callingHoursEnd = "19:00",
        maxCallsPerBatch = 50,
        maxRetries = 2,
        isAiCallingEnabled = true,
        isDemoMode = true,
        webhookEndpointUrl = "https://api.telephony.grampanchayat.gov.in/v1/voice/outbound"
    )

    val initialAiCampaigns = listOf(
        AiCallCampaign(
            id = "camp_001",
            announcementId = "ann_001",
            announcementTitle = "स्वातंत्र्य दिन विशेष ग्रामसभा व विकास आढावा",
            announcementMessage = "आदर्श ग्रामपंचायत पळसखेड दौलतच्या सर्व सन्माननीय ग्रामस्थांना कळविण्यात येते की, दिनांक १५ ऑगस्ट रोजी सकाळी १०:०० वाजता ग्रामपंचायत प्रांगणात विशेष ग्रामसभेचे आयोजन केले आहे. सर्वांनी वेळेवर उपस्थित राहावे.",
            targetAudience = "ALL",
            targetAudienceLabel = "सर्व ग्रामस्थ (१,८२० कुटुंब)",
            language = "mr",
            voiceId = "mr_female_1",
            voiceName = "आरोही (मराठी महिला)",
            status = "COMPLETED",
            totalRecipients = 48,
            connectedCalls = 41,
            unansweredCalls = 4,
            busyCalls = 2,
            failedCalls = 1,
            startedAt = System.currentTimeMillis() - 86400000L * 2,
            completedAt = System.currentTimeMillis() - 86400000L * 2 + 1800000L,
            isDemoMode = true
        ),
        AiCallCampaign(
            id = "camp_002",
            announcementId = "ann_002",
            announcementTitle = "प्रभाग ३ व ४ मध्ये जलवाहिनी दुरुस्ती सूचना",
            announcementMessage = "प्रभाग क्र. ३ व ४ मधील सर्व नागरिकांना सूचित करण्यात येते की, मुख्य पाईपलाईन दुरुस्ती कामामुळे उद्या सकाळी पाणीपुरवठा २ तास उशिरा होईल. नागरिकांनी सहकार्य करावे.",
            targetAudience = "WARD_3",
            targetAudienceLabel = "प्रभाग ३ व ४ नागरिक",
            language = "mr",
            voiceId = "mr_male_1",
            voiceName = "अनिकेत (मराठी पुरुष)",
            status = "COMPLETED",
            totalRecipients = 32,
            connectedCalls = 28,
            unansweredCalls = 3,
            busyCalls = 1,
            failedCalls = 0,
            startedAt = System.currentTimeMillis() - 86400000L,
            completedAt = System.currentTimeMillis() - 86400000L + 900000L,
            isDemoMode = true
        ),
        AiCallCampaign(
            id = "camp_003",
            announcementId = "ann_003",
            announcementTitle = "घरपट्टी व पाणीपट्टी १०% विशेष सवलत मुदत",
            announcementMessage = "ग्रामपंचायत कर भरणाऱ्या नागरिकांसाठी ३१ ऑगस्टपर्यंत १० टक्के विशेष सवलत योजना लागू आहे. ऑनलाइन किंवा कार्यालयात येऊन कर भरणा करावा.",
            targetAudience = "TAX_DEFAULTERS",
            targetAudienceLabel = "करधारक नागरिक",
            language = "mr",
            voiceId = "mr_female_1",
            voiceName = "आरोही (मराठी महिला)",
            status = "COMPLETED",
            totalRecipients = 25,
            connectedCalls = 22,
            unansweredCalls = 2,
            busyCalls = 1,
            failedCalls = 0,
            startedAt = System.currentTimeMillis() - 3600000L * 4,
            completedAt = System.currentTimeMillis() - 3600000L * 3,
            isDemoMode = true
        )
    )

    val initialAiCallLogs = listOf(
        AiCallLog(
            id = "log_001",
            campaignId = "camp_003",
            citizenName = "राजेश विष्णू सावंत",
            mobileNumber = "98765*****",
            wardNumber = 3,
            announcementTitle = "घरपट्टी व पाणीपट्टी १०% विशेष सवलत मुदत",
            announcementMessage = "ग्रामपंचायत कर भरणाऱ्या नागरिकांसाठी ३१ ऑगस्टपर्यंत १० टक्के विशेष सवलत योजना लागू आहे. ऑनलाइन किंवा कार्यालयात येऊन कर भरणा करावा.",
            status = "CONNECTED",
            durationSec = 44,
            timestamp = System.currentTimeMillis() - 3600000L * 4,
            isDemo = true,
            aiVoiceUsed = "आरोही (मराठी महिला)",
            notes = "नागरिकाने कॉल स्वीकारला. AI ने संपूर्ण घोषणा ऐकवली."
        ),
        AiCallLog(
            id = "log_002",
            campaignId = "camp_003",
            citizenName = "आनंद बापूराव शिंदे",
            mobileNumber = "94221*****",
            wardNumber = 1,
            announcementTitle = "घरपट्टी व पाणीपट्टी १०% विशेष सवलत मुदत",
            announcementMessage = "ग्रामपंचायत कर भरणाऱ्या नागरिकांसाठी ३१ ऑगस्टपर्यंत १० टक्के विशेष सवलत योजना लागू आहे.",
            status = "CONNECTED",
            durationSec = 42,
            timestamp = System.currentTimeMillis() - 3600000L * 4 + 60000L,
            isDemo = true,
            aiVoiceUsed = "आरोही (मराठी महिला)",
            notes = "कॉल यशस्वी. नागरिकाने कर भरणा प्रक्रियेची माहिती ऐकली."
        ),
        AiCallLog(
            id = "log_003",
            campaignId = "camp_003",
            citizenName = "विठ्ठल सखाराम पाटील",
            mobileNumber = "98230*****",
            wardNumber = 2,
            announcementTitle = "घरपट्टी व पाणीपट्टी १०% विशेष सवलत मुदत",
            announcementMessage = "ग्रामपंचायत कर भरणाऱ्या नागरिकांसाठी ३१ ऑगस्टपर्यंत १० टक्के विशेष सवलत योजना लागू आहे.",
            status = "NOT_ANSWERED",
            durationSec = 0,
            timestamp = System.currentTimeMillis() - 3600000L * 4 + 120000L,
            isDemo = true,
            aiVoiceUsed = "आरोही (मराठी महिला)",
            notes = "रिंग वाजली, उत्तर दिले नाही. संध्याकाळी फेर-प्रयत्न नियोजित."
        ),
        AiCallLog(
            id = "log_004",
            campaignId = "camp_003",
            citizenName = "सुनीता रमेश मोरे",
            mobileNumber = "91580*****",
            wardNumber = 4,
            announcementTitle = "घरपट्टी व पाणीपट्टी १०% विशेष सवलत मुदत",
            announcementMessage = "ग्रामपंचायत कर भरणाऱ्या नागरिकांसाठी ३१ ऑगस्टपर्यंत १० टक्के विशेष सवलत योजना लागू आहे.",
            status = "CONNECTED",
            durationSec = 48,
            timestamp = System.currentTimeMillis() - 3600000L * 4 + 180000L,
            isDemo = true,
            aiVoiceUsed = "आरोही (मराठी महिला)",
            notes = "नागरिकाने विचारले: 'तक्रार करायची आहे'. AI ने नम्रपणे उत्तर दिले: 'हा कॉल केवळ माहितीसाठी आहे. तक्रारीसाठी अधिकृत पोर्टल वापरावे'."
        ),
        AiCallLog(
            id = "log_005",
            campaignId = "camp_002",
            citizenName = "गणेश बाळकृष्ण माने",
            mobileNumber = "98901*****",
            wardNumber = 3,
            announcementTitle = "प्रभाग ३ व ४ मध्ये जलवाहिनी दुरुस्ती सूचना",
            announcementMessage = "मुख्य पाईपलाईन दुरुस्ती कामामुळे उद्या पाणीपुरवठा २ तास उशिरा होईल.",
            status = "BUSY",
            durationSec = 0,
            timestamp = System.currentTimeMillis() - 86400000L + 60000L,
            isDemo = true,
            aiVoiceUsed = "अनिकेत (मराठी पुरुष)",
            notes = "फोन लाईन व्यस्त (Line Busy)."
        ),
        AiCallLog(
            id = "log_006",
            campaignId = "camp_001",
            citizenName = "दत्तात्रय तुकाराम पवार",
            mobileNumber = "94200*****",
            wardNumber = 5,
            announcementTitle = "स्वातंत्र्य दिन विशेष ग्रामसभा व विकास आढावा",
            announcementMessage = "१५ ऑगस्ट रोजी सकाळी १०:०० वाजता विशेष ग्रामसभेचे आयोजन केले आहे.",
            status = "CONNECTED",
            durationSec = 52,
            timestamp = System.currentTimeMillis() - 86400000L * 2 + 120000L,
            isDemo = true,
            aiVoiceUsed = "आरोही (मराठी महिला)",
            notes = "घोषणा पूर्णपणे ऐकली गेली. कॉल यशस्वी."
        )
    )

    val initialAiScheduledCalls = listOf(
        AiScheduledCall(
            id = "sched_001",
            title = "पोषण पंधरवडा व बाल आरोग्य तपासणी शिबिर",
            message = "ग्रामपंचायत आणि प्राथमिक आरोग्य उपकेंद्र यांच्या संयुक्त विद्यमाने आगामी मंगळवारी मोफत बाल आरोग्य व लसीकरण शिबिर आयोजित करण्यात आले आहे. सर्व पालकांनी लाभ घ्यावा.",
            language = "mr",
            voiceId = "mr_female_1",
            targetAudience = "WOMEN_SHG",
            targetAudienceLabel = "माता व महिला बचत गट",
            recipientCount = 320,
            scheduledDate = "१० सप्टेंबर २०२६",
            scheduledTime = "सकाळी १०:००",
            scheduledTimestamp = System.currentTimeMillis() + 86400000L * 3,
            status = "PENDING"
        ),
        AiScheduledCall(
            id = "sched_002",
            title = "पीएम किसान ई-केवायसी (e-KYC) अंतिम मुदत",
            message = "सर्व शेतकरी बांधवांना कळविण्यात येते की पीएम किसान योजनेचा आगामी हप्ता मिळण्यासाठी ई-केवायसी पूर्ण करण्याची अंतिम तारीख १५ सप्टेंबर आहे. सीएससी केंद्रात त्वरित नोंदणी करावी.",
            language = "mr",
            voiceId = "mr_male_2",
            targetAudience = "FARMERS",
            targetAudienceLabel = "सर्व शेतकरी गट",
            recipientCount = 480,
            scheduledDate = "१२ सप्टेंबर २०२६",
            scheduledTime = "सकाळी ११:३०",
            scheduledTimestamp = System.currentTimeMillis() + 86400000L * 5,
            status = "PENDING"
        )
    )
}

