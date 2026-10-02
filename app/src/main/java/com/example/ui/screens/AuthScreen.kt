package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.AppLanguage
import com.example.data.local.AuthorizedCitizen
import com.example.data.local.District
import com.example.data.local.GramPanchayatInfo
import com.example.data.local.MaharashtraDirectory
import com.example.data.local.Taluka
import com.example.data.local.UserProfile
import com.example.ui.components.LanguageToggleChip
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    currentProfile: UserProfile?,
    language: AppLanguage,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    initialDistrictId: String = "buldhana",
    initialTalukaId: String = "chikhli",
    initialGramPanchayatId: String = "gp_palaskhed_daulat",
    onToggleLanguage: () -> Unit,
    onLogin: (mobile: String, gpId: String, pass: String) -> Unit,
    onRegister: (
        fullName: String,
        districtId: String,
        talukaId: String,
        gpId: String,
        ward: Int,
        mobile: String,
        otp: String,
        expectedOtp: String,
        pass: String
    ) -> Unit,
    onBackToRoleSelection: (() -> Unit)? = null
) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // Hierarchy selection
    var selectedDistrictId by remember { mutableStateOf(initialDistrictId) }
    var selectedTalukaId by remember { mutableStateOf(initialTalukaId) }
    var selectedGramPanchayatId by remember { mutableStateOf(initialGramPanchayatId) }

    var districtDropdownExpanded by remember { mutableStateOf(false) }
    var talukaDropdownExpanded by remember { mutableStateOf(false) }
    var gpDropdownExpanded by remember { mutableStateOf(false) }

    // Input fields
    var mobileNumber by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var selectedWard by remember { mutableIntStateOf(1) }
    var wardDropdownExpanded by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var sentOtp by remember { mutableStateOf("") }
    var otpSentMessage by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localValidationErr by remember { mutableStateOf<String?>(null) }

    // Districts, talukas, GPs lists
    val allDistricts: List<District> = remember { MaharashtraDirectory.getAllDistricts() }
    val talukasForSelectedDistrict: List<Taluka> = remember(selectedDistrictId) {
        MaharashtraDirectory.getTalukasForDistrict(selectedDistrictId)
    }
    val gpsForSelectedTaluka: List<GramPanchayatInfo> = remember(selectedTalukaId) {
        MaharashtraDirectory.getGramPanchayatsForTaluka(selectedTalukaId)
    }

    // Keep child selections in sync when parent changes
    LaunchedEffect(selectedDistrictId) {
        val currentTalukas = MaharashtraDirectory.getTalukasForDistrict(selectedDistrictId)
        if (currentTalukas.none { it.id == selectedTalukaId }) {
            selectedTalukaId = currentTalukas.firstOrNull()?.id ?: ""
        }
    }
    LaunchedEffect(selectedTalukaId) {
        val currentGps = MaharashtraDirectory.getGramPanchayatsForTaluka(selectedTalukaId)
        if (currentGps.none { it.id == selectedGramPanchayatId }) {
            selectedGramPanchayatId = currentGps.firstOrNull()?.id ?: ""
        }
    }

    val currentDistrict: District? = remember(selectedDistrictId, allDistricts) {
        allDistricts.find { it.id == selectedDistrictId }
    }
    val currentTaluka: Taluka? = remember(selectedTalukaId, talukasForSelectedDistrict) {
        talukasForSelectedDistrict.find { it.id == selectedTalukaId }
    }
    val currentGp: GramPanchayatInfo? = remember(selectedGramPanchayatId, gpsForSelectedTaluka) {
        gpsForSelectedTaluka.find { it.id == selectedGramPanchayatId }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .testTag("auth_screen_container")
    ) {
        // Top row with State branding & language toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gp_logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "महाराष्ट्र शासन • ई-ग्रामपंचायत" else "Govt of Maharashtra • E-Grampanchayat",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (language == AppLanguage.MARATHI)
                            (currentGp?.nameMr ?: "सार्वत्रिक नागरिक पोर्टल")
                        else
                            (currentGp?.nameEn ?: "Universal Citizen Portal"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (onBackToRoleSelection != null) {
                    FilledTonalButton(
                        onClick = onBackToRoleSelection,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "भूमिका बदला" else "Role",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                LanguageToggleChip(
                    currentLanguage = language,
                    onToggle = onToggleLanguage
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = GramSaffronLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.MARATHI) "नागरिक पडताळणी व प्रवेश" else "Citizen Verification & Portal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "कडक नियम: एक नागरिक = एकच अधिकृत ग्रामपंचायत. कृपया आपल्या अधिकृत ग्रामपंचायतीची निवड करा."
                    else
                        "Strict Rule: One Citizen = One verified Gram Panchayat. Please select your official Gram Panchayat.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.95f)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Tab Selector for Mode
        TabRow(
            selectedTabIndex = if (!isRegisterMode) 0 else 1,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = !isRegisterMode,
                onClick = {
                    isRegisterMode = false
                    localValidationErr = null
                },
                text = {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "लॉग इन (Login)" else "Login",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            Tab(
                selected = isRegisterMode,
                onClick = {
                    isRegisterMode = true
                    localValidationErr = null
                },
                text = {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "नवीन नोंदणी (Register)" else "New Registration",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Error Banner
        val displayError = localValidationErr ?: errorMessage
        if (!displayError.isNullOrBlank()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("auth_error_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = displayError,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // ================= GRAM PANCHAYAT SELECTION CARD =================
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.MARATHI) "ग्रामपंचायत कार्यक्षेत्र निवडा" else "Select Gram Panchayat Jurisdiction",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // District Dropdown
                ExposedDropdownMenuBox(
                    expanded = districtDropdownExpanded,
                    onExpandedChange = { districtDropdownExpanded = !districtDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = if (language == AppLanguage.MARATHI) (currentDistrict?.nameMr ?: "") else (currentDistrict?.nameEn ?: ""),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (language == AppLanguage.MARATHI) "जिल्हा (District) *" else "District *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("auth_district_selector"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = districtDropdownExpanded,
                        onDismissRequest = { districtDropdownExpanded = false }
                    ) {
                        allDistricts.forEach { dist ->
                            DropdownMenuItem(
                                text = { Text(if (language == AppLanguage.MARATHI) dist.nameMr else dist.nameEn) },
                                onClick = {
                                    selectedDistrictId = dist.id
                                    districtDropdownExpanded = false
                                    localValidationErr = null
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Taluka Dropdown
                ExposedDropdownMenuBox(
                    expanded = talukaDropdownExpanded,
                    onExpandedChange = { talukaDropdownExpanded = !talukaDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = if (language == AppLanguage.MARATHI) (currentTaluka?.nameMr ?: "") else (currentTaluka?.nameEn ?: ""),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (language == AppLanguage.MARATHI) "तालुका (Taluka) *" else "Taluka *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = talukaDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("auth_taluka_selector"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = talukaDropdownExpanded,
                        onDismissRequest = { talukaDropdownExpanded = false }
                    ) {
                        talukasForSelectedDistrict.forEach { tal ->
                            DropdownMenuItem(
                                text = { Text(if (language == AppLanguage.MARATHI) tal.nameMr else tal.nameEn) },
                                onClick = {
                                    selectedTalukaId = tal.id
                                    talukaDropdownExpanded = false
                                    localValidationErr = null
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Gram Panchayat Dropdown
                ExposedDropdownMenuBox(
                    expanded = gpDropdownExpanded,
                    onExpandedChange = { gpDropdownExpanded = !gpDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = if (language == AppLanguage.MARATHI) (currentGp?.nameMr ?: "") else (currentGp?.nameEn ?: ""),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (language == AppLanguage.MARATHI) "ग्रामपंचायत (Gram Panchayat) *" else "Gram Panchayat *") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gpDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("auth_gp_selector"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = gpDropdownExpanded,
                        onDismissRequest = { gpDropdownExpanded = false }
                    ) {
                        gpsForSelectedTaluka.forEach { gp ->
                            DropdownMenuItem(
                                text = { Text(if (language == AppLanguage.MARATHI) gp.nameMr else gp.nameEn) },
                                onClick = {
                                    selectedGramPanchayatId = gp.id
                                    gpDropdownExpanded = false
                                    localValidationErr = null
                                }
                            )
                        }
                    }
                }
            }
        }

        // Registration-only fields: Full Name & Ward
        if (isRegisterMode) {
            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    localValidationErr = null
                },
                label = { Text(if (language == AppLanguage.MARATHI) "आपले पूर्ण नाव *" else "Your Full Name *") },
                placeholder = { Text(if (language == AppLanguage.MARATHI) "उदा. सचिन रमेश पाटील" else "e.g. Sachin Ramesh Patil") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_fullname_input"),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Ward Selector (1 to 6)
            ExposedDropdownMenuBox(
                expanded = wardDropdownExpanded,
                onExpandedChange = { wardDropdownExpanded = !wardDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = if (language == AppLanguage.MARATHI) "प्रभाग क्र. $selectedWard (वॉर्ड $selectedWard)" else "Ward No. $selectedWard",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(if (language == AppLanguage.MARATHI) "प्रभाग क्र. (Ward Number) *" else "Ward Number *") },
                    leadingIcon = { Icon(Icons.Default.HomeWork, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = wardDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("auth_ward_selector"),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = wardDropdownExpanded,
                    onDismissRequest = { wardDropdownExpanded = false }
                ) {
                    for (w in 1..6) {
                        DropdownMenuItem(
                            text = {
                                Text(if (language == AppLanguage.MARATHI) "प्रभाग क्र. $w (वॉर्ड $w)" else "Ward No. $w")
                            },
                            onClick = {
                                selectedWard = w
                                wardDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Mobile Number Field
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = {
                val digits = it.filter { ch -> ch.isDigit() }.take(10)
                mobileNumber = digits
                localValidationErr = null
            },
            label = { Text(if (language == AppLanguage.MARATHI) "१० अंकी मोबाईल नंबर *" else "10-digit Mobile Number *") },
            placeholder = { Text("९८७६५४३२१०") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = if (isRegisterMode) ImeAction.Next else ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_mobile_input"),
            shape = RoundedCornerShape(12.dp)
        )

        // OTP Row (Required for Registration)
        if (isRegisterMode) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = otpInput,
                    onValueChange = {
                        otpInput = it.filter { ch -> ch.isDigit() }.take(6)
                        localValidationErr = null
                    },
                    label = { Text(if (language == AppLanguage.MARATHI) "६ अंकी OTP *" else "6-digit OTP *") },
                    placeholder = { Text("123456") },
                    leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("auth_otp_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                FilledTonalButton(
                    onClick = {
                        if (mobileNumber.length < 10) {
                            localValidationErr = if (language == AppLanguage.MARATHI)
                                "कृपया आधी १० अंकी वैध मोबाईल नंबर प्रविष्ट करा."
                            else
                                "Please enter a valid 10-digit mobile number first."
                            return@FilledTonalButton
                        }
                        sentOtp = "123456"
                        otpInput = "123456"
                        otpSentMessage = if (language == AppLanguage.MARATHI)
                            "OTP पाठवला: 123456 (आपोआप भरला)"
                        else
                            "OTP sent: 123456 (auto-filled)"
                    },
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "OTP मिळवा" else "Get OTP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!otpSentMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "✓ $otpSentMessage",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Password / PIN Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    if (language == AppLanguage.MARATHI)
                        if (isRegisterMode) "पासवर्ड (किमान ६ वर्ण / डिफॉल्ट: Citizen@123)" else "पासवर्ड (डिफॉल्ट: Citizen@123)"
                    else
                        if (isRegisterMode) "Password (min 6 chars / default: Citizen@123)" else "Password (default: Citizen@123)"
                )
            },
            placeholder = { Text("Citizen@123") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Action Button
        Button(
            onClick = {
                if (isRegisterMode) {
                    if (fullName.trim().isBlank()) {
                        localValidationErr = if (language == AppLanguage.MARATHI) "कृपया आपले पूर्ण नाव प्रविष्ट करा." else "Please enter your full name."
                        return@Button
                    }
                    if (mobileNumber.trim().length < 10) {
                        localValidationErr = if (language == AppLanguage.MARATHI) "कृपया १० अंकी वैध मोबाईल नंबर प्रविष्ट करा." else "Please enter a valid 10-digit mobile number."
                        return@Button
                    }
                    val otpToVerify = if (otpInput.isNotBlank()) otpInput.trim() else "123456"
                    val validExpectedOtp = if (sentOtp.isNotBlank()) sentOtp else "123456"
                    val validPass = if (password.trim().length >= 6) password.trim() else "Citizen@123"

                    onRegister(
                        fullName.trim(),
                        selectedDistrictId,
                        selectedTalukaId,
                        selectedGramPanchayatId,
                        selectedWard,
                        mobileNumber.trim(),
                        otpToVerify,
                        validExpectedOtp,
                        validPass
                    )
                } else {
                    if (mobileNumber.trim().length < 10) {
                        localValidationErr = if (language == AppLanguage.MARATHI) "कृपया १० अंकी वैध मोबाईल नंबर प्रविष्ट करा." else "Please enter a valid 10-digit mobile number."
                        return@Button
                    }
                    val validPass = if (password.trim().isNotBlank()) password.trim() else "Citizen@123"
                    onLogin(mobileNumber.trim(), selectedGramPanchayatId, validPass)
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("auth_submit_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI) "पडताळणी चालू आहे..." else "Verifying Citizen Record...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (!isRegisterMode) {
                        if (language == AppLanguage.MARATHI) "लॉग इन करा (Login)" else "Login to Selected GP"
                    } else {
                        if (language == AppLanguage.MARATHI) "नोंदणी पूर्ण करा (Register)" else "Complete Registration"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ================= VERIFIED CITIZEN TEST SELECTOR ASSISTANT =================
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.MARATHI)
                            "🔒 अधिकृत नागरिक चाचणी नमुने (GP Locking Test)"
                        else
                            "🔒 Verified Citizen Test Presets (GP Locking Test)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "खालील अधिकृत नागरिकाची निवड करून लॉक नियमाची चाचणी घ्या. चुकीच्या ग्रामपंचायतीमध्ये लॉगिन/नोंदणीचा प्रयत्न केल्यास त्वरित अडवले जाईल."
                    else
                        "Select a verified citizen below to test the GP Lock. Attempting to login/register to another GP will be blocked immediately.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                val sampleCitizens: List<AuthorizedCitizen> = remember { MaharashtraDirectory.initialAuthorizedCitizens.take(4) }
                sampleCitizens.forEach { citizen: AuthorizedCitizen ->
                    val dist = allDistricts.find { it.id == citizen.districtId }?.nameMr ?: citizen.districtId
                    val gp = MaharashtraDirectory.getGramPanchayatById(citizen.gramPanchayatId)?.nameMr ?: citizen.gramPanchayatId

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${citizen.fullName} (${citizen.mobileNumber})",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "अधिकृत GP: $gp ($dist) • प्रभाग ${citizen.wardNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        Button(
                            onClick = {
                                mobileNumber = citizen.mobileNumber
                                fullName = citizen.fullName
                                selectedWard = citizen.wardNumber
                                otpInput = "123456"
                                sentOtp = "123456"
                                password = "Citizen@123"
                                localValidationErr = null
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "नंबर भरा" else "Fill",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Footer Note
        Text(
            text = if (language == AppLanguage.MARATHI)
                "महाराष्ट्र शासन • ग्रामविकास व पंचायत राज विभाग\nडिजिटल ग्रामपंचायत ई-गव्हर्नन्स पुढाकार"
            else
                "Govt of Maharashtra • Rural Development & Panchayati Raj\nDigital Grampanchayat E-Governance Initiative",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
