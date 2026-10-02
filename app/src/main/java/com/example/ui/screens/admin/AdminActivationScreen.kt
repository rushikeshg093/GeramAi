package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AppLanguage
import com.example.data.local.MaharashtraDirectory
import com.example.data.local.PreapprovedOfficer
import com.example.ui.theme.GramSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminActivationScreen(
    language: AppLanguage,
    isLoading: Boolean,
    errorMessage: String?,
    verifiedOfficer: PreapprovedOfficer?,
    onToggleLanguage: () -> Unit,
    onLookupOfficer: (query: String) -> Unit,
    onActivateAccount: (adminIdOrMobile: String, otp: String, expectedOtp: String, pass: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var queryInput by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var sentOtp by remember { mutableStateOf("852963") }
    var otpMessage by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var localValidationErr by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "अधिकारी खाते सक्रियीकरण" else "Officer Account Activation",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToLogin) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to login"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onToggleLanguage) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "English" else "मराठी",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .testTag("admin_activation_container"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Badge
            Surface(
                shape = CircleShape,
                color = GramSaffron.copy(alpha = 0.15f),
                modifier = Modifier.size(70.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = GramSaffron,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == AppLanguage.MARATHI) "सुपर ॲडमिन पूर्व-मंजूर खाते सक्रियीकरण" else "Super Admin Pre-Approved Activation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if (language == AppLanguage.MARATHI)
                    "केवळ सुपर ॲडमिनने आधीच तयार केलेल्या ग्रामपंचायत अधिकाऱ्यांसाठी.\nसामान्य नागरिक येथे खाते तयार करू शकत नाहीत."
                else
                    "Restricted to officers pre-provisioned by Super Admin.\nNormal citizens cannot create officer accounts.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error Message Banner
            val displayErr = localValidationErr ?: errorMessage
            if (!displayErr.isNullOrBlank()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .testTag("activation_error_card")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = displayErr,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Step 1: Officer Identification Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "१",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "पायरी १: अधिकारी पडताळणी" else "Step 1: Officer Verification",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.MARATHI)
                            "सुपर ॲडमिनने दिलेला अधिकारी आयडी किंवा नोंदणीकृत मोबाईल नंबर टाका:"
                        else
                            "Enter Admin ID or Mobile Number provisioned by Super Admin:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = queryInput,
                        onValueChange = {
                            queryInput = it
                            localValidationErr = null
                        },
                        label = { Text(if (language == AppLanguage.MARATHI) "अधिकारी आयडी / मोबाईल नंबर *" else "Admin ID / Mobile Number *") },
                        placeholder = { Text("उदा. 9423889900 किंवा OFF-BULD-CHK-001") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("activation_query_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            if (queryInput.trim().isBlank()) {
                                localValidationErr = if (language == AppLanguage.MARATHI)
                                    "कृपया अधिकारी आयडी किंवा मोबाईल नंबर प्रविष्ट करा."
                                else
                                    "Please enter Admin ID or Mobile Number."
                                return@Button
                            }
                            localValidationErr = null
                            onLookupOfficer(queryInput.trim())
                        },
                        enabled = !isLoading && queryInput.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_verify_officer")
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "अधिकारी नोंद तपासा" else "Check Officer Record",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verified Officer Details Display (if found)
            if (verifiedOfficer != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("verified_officer_badge_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.MARATHI) "अधिकृत अधिकारी सापडला! (Verified)" else "Authorized Officer Found!",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "नाव: ${verifiedOfficer.fullName}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "पद: ${verifiedOfficer.designation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "नियुक्त ग्रामपंचायत: ${verifiedOfficer.gramPanchayatNameMr} (${verifiedOfficer.talukaId.replaceFirstChar { it.uppercase() }}, ${verifiedOfficer.districtId.replaceFirstChar { it.uppercase() }})",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "मोबाईल: ${verifiedOfficer.mobileNumber} • आयडी: ${verifiedOfficer.adminId}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Step 2: OTP Verification & Password Setup
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "२",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.MARATHI) "पायरी २: OTP व पासवर्ड निश्चित करा" else "Step 2: OTP & Set Password",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // OTP Row
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
                                placeholder = { Text("852963") },
                                leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("activation_otp_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            FilledTonalButton(
                                onClick = {
                                    sentOtp = verifiedOfficer.defaultOtp.ifBlank { "852963" }
                                    otpInput = sentOtp
                                    otpMessage = if (language == AppLanguage.MARATHI)
                                        "OTP पाठवला: $sentOtp (आपोआप भरला)"
                                    else
                                        "OTP sent: $sentOtp (auto-filled)"
                                },
                                modifier = Modifier.height(52.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "OTP मिळवा" else "Get OTP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (!otpMessage.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "✓ $otpMessage",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Password Field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(if (language == AppLanguage.MARATHI) "नवीन पासवर्ड (किमान ६ वर्ण) *" else "New Password (min 6 chars) *") },
                            placeholder = { Text("Admin@2026") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("activation_password_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Confirm Password Field
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text(if (language == AppLanguage.MARATHI) "पासवर्ड पुन्हा प्रविष्ट करा *" else "Confirm Password *") },
                            placeholder = { Text("Admin@2026") },
                            leadingIcon = { Icon(Icons.Default.LockClock, contentDescription = null) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("activation_confirm_password_input"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Submit Activation Button
                        Button(
                            onClick = {
                                if (otpInput.length < 6) {
                                    localValidationErr = if (language == AppLanguage.MARATHI) "कृपया ६ अंकी OTP प्रविष्ट करा." else "Please enter 6-digit OTP."
                                    return@Button
                                }
                                if (password.length < 6) {
                                    localValidationErr = if (language == AppLanguage.MARATHI) "पासवर्ड किमान ६ वर्णांचा असावा." else "Password must be at least 6 characters."
                                    return@Button
                                }
                                if (password != confirmPassword) {
                                    localValidationErr = if (language == AppLanguage.MARATHI) "दोन्ही पासवर्ड जुळत नाहीत." else "Passwords do not match."
                                    return@Button
                                }
                                localValidationErr = null
                                onActivateAccount(
                                    queryInput.trim().ifBlank { verifiedOfficer.adminId },
                                    otpInput.trim(),
                                    sentOtp,
                                    password.trim()
                                )
                            },
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GramSaffron),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_submit_activation")
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(if (language == AppLanguage.MARATHI) "सक्रियीकरण चालू आहे..." else "Activating Account...")
                            } else {
                                Icon(Icons.Default.LockOpen, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "खाते सक्रिय करा व पासवर्ड सेव्ह करा" else "Activate Account & Save Password",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ================= PREAPPROVED TEST PRESETS CARD =================
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI)
                                "सुपर ॲडमिन पूर्व-मंजूर अधिकारी चाचणी नमुने"
                            else
                                "Super Admin Pre-Approved Test Accounts",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.MARATHI)
                            "पळसखेड दौलत ग्रामपंचायतीसाठी सुपर ॲडमिनने आधीच तयार केलेले अधिकृत खाते निवडा:"
                        else
                            "Select pre-approved officer account for Palaskhed Daulat Gram Panchayat:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val testOfficers = remember { MaharashtraDirectory.getAllPreapprovedOfficers().take(4) }
                    testOfficers.forEach { off ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${off.fullName} (${off.designation})",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "मोबाईल: ${off.mobileNumber} • आयडी: ${off.adminId}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Button(
                                onClick = {
                                    queryInput = off.mobileNumber
                                    localValidationErr = null
                                    onLookupOfficer(off.mobileNumber)
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "निवडा" else "Select",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Negative test case: Unauthorized Citizen mobile number
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "अनधिकृत नागरिक चाचणी (९८७६५४३२१०)" else "Unauthorized Citizen Test (9876543210)",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = if (language == AppLanguage.MARATHI) "सामान्य नागरिक खाते तयार करू शकत नाही हे तपासा" else "Test that citizen registration is blocked",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                queryInput = "9876543210"
                                localValidationErr = null
                                onLookupOfficer("9876543210")
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "चाचणी" else "Test",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back to Login Link
            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI) "आधीच खाते सक्रिय आहे? येथे लॉगिन करा" else "Already activated? Back to Officer Login",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
