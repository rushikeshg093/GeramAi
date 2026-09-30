package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.AppLanguage
import com.example.data.local.OnlineServiceItem
import com.example.data.local.PanchayatProfile
import com.example.data.local.ServiceApplicationEntity
import com.example.data.local.ServiceType
import com.example.data.local.UserProfile
import com.example.ui.components.AppTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun OnlineServicesScreen(
    applications: List<ServiceApplicationEntity>,
    userProfile: UserProfile?,
    panchayatProfile: PanchayatProfile? = null,
    servicesList: List<OnlineServiceItem> = emptyList(),
    language: AppLanguage,
    selectedApplicationForCertificate: ServiceApplicationEntity?,
    onToggleLanguage: () -> Unit,
    onBack: () -> Unit,
    onOpenCertificate: (ServiceApplicationEntity?) -> Unit,
    onSubmitApplication: (type: String, name: String, mobile: String, ward: Int, details: String, fee: Int) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedServiceForApply by remember { mutableStateOf<ServiceType?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Apply Services, 1: My Applications & Receipts

    val services = listOf(
        ServiceType.PROPERTY_TAX,
        ServiceType.WATER_TAX,
        ServiceType.BIRTH_CERTIFICATE,
        ServiceType.DEATH_CERTIFICATE,
        ServiceType.MARRIAGE_REGISTRATION,
        ServiceType.NOC_CERTIFICATE,
        ServiceType.BPL_CERTIFICATE
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (language == AppLanguage.MARATHI) "ऑनलाइन सेवा व ई-दाखले" else "Online Citizen Services",
                language = language,
                onToggleLanguage = onToggleLanguage,
                showBackButton = true,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "नवीन अर्ज / कर भरणा" else "Apply / Pay Tax",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "माझे दाखले व पावत्या (${applications.size})" else "My Certificates (${applications.size})",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Tax Rebate Banner
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.MARATHI) "१०% विशेष ऑनलाइन कर सवलत" else "10% Special Online Tax Rebate",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = if (language == AppLanguage.MARATHI)
                                            "घरपट्टी व पाणीपट्टी ऑनलाइन भरून त्वरित डिजिटल अधिकृत पावती मिळवा."
                                        else
                                            "Pay property & water taxes online to claim 10% instant rebate.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }
                        }
                    }

                    items(services) { service ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedServiceForApply = service }
                                .testTag("service_card_${service.key}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (service == ServiceType.PROPERTY_TAX || service == ServiceType.WATER_TAX) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (service == ServiceType.PROPERTY_TAX || service == ServiceType.WATER_TAX) Icons.Default.ReceiptLong else Icons.Default.Article,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (language == AppLanguage.MARATHI) service.titleMr else service.titleEn,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "कालावधी: ${service.processingDays} • शासकीय शुल्क: ${if (service.fee == 0) "कर रक्कम" else "रु. ${service.fee}/-"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Button(
                                    onClick = { selectedServiceForApply = service },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(
                                        text = if (service == ServiceType.PROPERTY_TAX || service == ServiceType.WATER_TAX) {
                                            if (language == AppLanguage.MARATHI) "भरा" else "Pay"
                                        } else {
                                            if (language == AppLanguage.MARATHI) "अर्ज करा" else "Apply"
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // My Applications & Certificates
                if (applications.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "कोणताही दाखला किंवा कर पावती उपलब्ध नाही." else "No issued certificates or receipts.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(applications) { app ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenCertificate(app) }
                                    .testTag("app_certificate_card_${app.id}")
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = app.id,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        StatusBadge(status = app.status, language = language)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = app.details,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "दाखला क्र: ${app.certificateNumber.ifBlank { "पडताळणी चालू" }}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = app.appliedDate,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = { onOpenCertificate(app) },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (language == AppLanguage.MARATHI) "डिजिटल दाखला / पावती पहा" else "View Digital Certificate / Receipt",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Form for Applying / Paying
    if (selectedServiceForApply != null) {
        ServiceApplyDialog(
            service = selectedServiceForApply!!,
            userProfile = userProfile,
            language = language,
            onDismiss = { selectedServiceForApply = null },
            onSubmit = { type, name, mob, ward, details, fee ->
                onSubmitApplication(type, name, mob, ward, details, fee)
                selectedServiceForApply = null
                selectedTab = 1
            }
        )
    }

    // Certificate / Receipt View Dialog
    if (selectedApplicationForCertificate != null) {
        DigitalCertificateDialog(
            application = selectedApplicationForCertificate,
            panchayatProfile = panchayatProfile,
            language = language,
            onDismiss = { onOpenCertificate(null) },
            onDownload = {
                onShowToast(
                    if (language == AppLanguage.MARATHI)
                        "दाखला PDF डाउनलोड झाली!"
                    else
                        "Certificate PDF downloaded!"
                )
            }
        )
    }
}

@Composable
fun ServiceApplyDialog(
    service: ServiceType,
    userProfile: UserProfile?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, Int, String, Int) -> Unit
) {
    val isTax = service == ServiceType.PROPERTY_TAX || service == ServiceType.WATER_TAX
    var applicantName by remember { mutableStateOf(userProfile?.fullName ?: "राजेश विष्णू सावंत") }
    var mobile by remember { mutableStateOf(userProfile?.mobileNumber ?: "9876543210") }
    var ward by remember { mutableIntStateOf(userProfile?.wardNumber ?: 3) }
    var propertyNumber by remember { mutableStateOf("मालमत्ता क्र. ३/०४५") }
    var extraDetails by remember {
        mutableStateOf(
            if (service == ServiceType.BIRTH_CERTIFICATE) "बाळाचे नाव: आरव सावंत, जन्म: १४/०२/२०२६"
            else if (service == ServiceType.DEATH_CERTIFICATE) "मयताचे नाव: विष्णू सावंत, दिनांक: १२/०१/२०२६"
            else if (service == ServiceType.MARRIAGE_REGISTRATION) "वर: राजेश सावंत, वधू: स्नेहा पाटील, विवाह: १०/०५/२०२५"
            else if (service == ServiceType.NOC_CERTIFICATE) "नवीन वीज जोडणीसाठी नाहरकत दाखला"
            else "दारिद्र्यरेषा कुटुंब क्रमांक दाखला मागणी"
        )
    }

    var baseTaxAmount by remember { mutableStateOf("1200") }
    val taxAmountInt = baseTaxAmount.toIntOrNull() ?: 1200
    val rebate = if (isTax) (taxAmountInt * 0.10).toInt() else 0
    val finalAmount = if (isTax) (taxAmountInt - rebate) else service.fee

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("service_apply_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) service.titleMr else service.titleEn,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = applicantName,
                    onValueChange = { applicantName = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "अर्जदाराचे नाव" else "Applicant Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "मोबाईल क्रमांक" else "Mobile Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (isTax) {
                    OutlinedTextField(
                        value = propertyNumber,
                        onValueChange = { propertyNumber = it },
                        label = { Text(if (language == AppLanguage.MARATHI) "मालमत्ता / नळ क्रमांक" else "Property / Tap ID") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("एकूण कर आकारणी:", style = MaterialTheme.typography.bodyMedium)
                                Text("रु. $taxAmountInt/-", fontWeight = FontWeight.Bold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("१०% ऑनलाइन सवलत:", color = Color(0xFF2E7D32), style = MaterialTheme.typography.bodyMedium)
                                Text("- रु. $rebate/-", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("अंतिम देय रक्कम:", fontWeight = FontWeight.Bold)
                                Text("रु. $finalAmount/-", fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B5E20), fontSize = 18.sp)
                            }
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = extraDetails,
                        onValueChange = { extraDetails = it },
                        label = { Text(if (language == AppLanguage.MARATHI) "दाखल्यासाठी आवश्यक तपशील" else "Certificate Details") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "शासकीय फी: रु. ${service.fee}/- (ऑनलाइन भरणा)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val detailsText = if (isTax) "$propertyNumber, $baseTaxAmount कर (सवलतीसह रु. $finalAmount भरणा)" else extraDetails
                        onSubmit(service.key, applicantName, mobile, ward, detailsText, finalAmount)
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("service_dialog_submit_button")
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTax) {
                            if (language == AppLanguage.MARATHI) "रु. $finalAmount भरणा करा (UPI)" else "Pay ₹$finalAmount via UPI"
                        } else {
                            if (language == AppLanguage.MARATHI) "अर्ज दाखल व फी भरणा करा" else "Submit & Pay ₹${service.fee}"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DigitalCertificateDialog(
    application: ServiceApplicationEntity,
    panchayatProfile: PanchayatProfile? = null,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    val gpName = if (language == AppLanguage.MARATHI) {
        panchayatProfile?.nameMr?.ifBlank { "आदर्श ग्रामपंचायत पळसखेड दौलत" } ?: "आदर्श ग्रामपंचायत पळसखेड दौलत"
    } else {
        panchayatProfile?.nameEn?.ifBlank { "Model Grampanchayat Palaskhed Daulat" } ?: "Model Grampanchayat Palaskhed Daulat"
    }
    val taluka = if (language == AppLanguage.MARATHI) {
        panchayatProfile?.talukaMr?.ifBlank { "चिखली" } ?: "चिखली"
    } else {
        panchayatProfile?.talukaEn?.ifBlank { "Chikhli" } ?: "Chikhli"
    }
    val district = if (language == AppLanguage.MARATHI) {
        panchayatProfile?.districtMr?.ifBlank { "बुलढाणा" } ?: "बुलढाणा"
    } else {
        panchayatProfile?.districtEn?.ifBlank { "Buldhana" } ?: "Buldhana"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF7)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("digital_certificate_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                // Header of the official certificate
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "महाराष्ट्र शासन" else "Government of Maharashtra",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF795548)
                    )
                    Text(
                        text = gpName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F3057)
                    )
                    Text(
                        text = if (language == AppLanguage.MARATHI) "ता. $taluka, जि. $district" else "Tal. $taluka, Dist. $district",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF424242)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFD4AF37).copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "अधिकृत डिजिटल ई-प्रमाणपत्र" else "Official Digital E-Certificate",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("दाखला क्र: ${application.certificateNumber}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text("दिनांक: ${application.appliedDate}", style = MaterialTheme.typography.labelMedium)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "प्रमाणित करण्यात येते की, श्री/श्रीमती **${application.applicantName}** (प्रभाग क्र. ${application.wardNumber}) यांनी सादर केलेल्या माहितीची ग्रामपंचायत दफ्तरी पडताळणी झाली असून खालीलप्रमाणे नोंद अधिकृत आहे:",
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = application.details,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Icon(Icons.Default.QrCode2, contentDescription = "QR Verify", modifier = Modifier.size(48.dp), tint = Color(0xFF0F3057))
                        Text("QR पडताळणी वैध", fontSize = 10.sp, color = Color.Gray)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("स्वाक्षरी/-", style = MaterialTheme.typography.labelSmall)
                        Text(if (language == AppLanguage.MARATHI) "ग्रामविकास अधिकारी" else "Village Development Officer", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        Text(gpName, style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDownload,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (language == AppLanguage.MARATHI) "दाखला PDF डाउनलोड करा" else "Download Certificate PDF")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (language == AppLanguage.MARATHI) "बंद करा" else "Close")
                }
            }
        }
    }
}
