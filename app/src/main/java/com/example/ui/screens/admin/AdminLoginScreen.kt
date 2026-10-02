package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.ui.theme.GramSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    language: AppLanguage,
    isLoading: Boolean,
    errorMessage: String?,
    onToggleLanguage: () -> Unit,
    onLogin: (adminIdOrMobile: String, pass: String, otp: String, expectedOtp: String) -> Unit,
    onResetPassword: ((query: String) -> Unit)? = null,
    onNavigateToActivation: () -> Unit,
    onBackToRoleSelection: () -> Unit
) {
    var adminIdOrMobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var otpInput by remember { mutableStateOf("") }
    var sentOtp by remember { mutableStateOf("852963") }
    var otpSentMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var resetInput by remember { mutableStateOf("") }
    var localValidationErr by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(if (language == AppLanguage.MARATHI) "पासवर्ड रीसेट करा" else "Reset Officer Password")
            },
            text = {
                Column {
                    Text(
                        if (language == AppLanguage.MARATHI)
                            "कृपया आपला अधिकारी आयडी, नोंदणीकृत मोबाईल नंबर किंवा अधिकृत ईमेल प्रविष्ट करा:"
                        else
                            "Please enter your Admin ID, registered mobile number or official email:"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = resetInput.ifBlank { adminIdOrMobile },
                        onValueChange = { resetInput = it },
                        label = { Text(if (language == AppLanguage.MARATHI) "आयडी / मोबाईल / ईमेल" else "ID / Mobile / Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = resetInput.ifBlank { adminIdOrMobile }.trim()
                        showResetDialog = false
                        if (target.isNotBlank()) {
                            onResetPassword?.invoke(target)
                        }
                    }
                ) {
                    Text(if (language == AppLanguage.MARATHI) "रीसेट लिंक पाठवा" else "Send Reset Instructions")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(if (language == AppLanguage.MARATHI) "रद्द करा" else "Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "अधिकारी व प्रशासन लॉगिन" else "Officer / Admin Login",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToRoleSelection) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to role selection"
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
                .testTag("admin_login_container"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Admin Badge Header
            Surface(
                shape = CircleShape,
                color = GramSaffron.copy(alpha = 0.15f),
                modifier = Modifier.size(76.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = GramSaffron,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.MARATHI) "ग्रामपंचायत प्रशासन कक्ष" else "Grampanchayat Admin Console",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if (language == AppLanguage.MARATHI)
                    "फक्त अधिकृत सरपंच, ग्रामसेवक, तलाठी व ग्रामपंचायत अधिकारी"
                else
                    "Restricted to authorized GP officials, Sarpanch & staff",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Login Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    val displayErr = localValidationErr ?: errorMessage
                    if (!displayErr.isNullOrBlank()) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .testTag("admin_login_error_card")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
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

                    // Field 1: Admin ID or Registered Mobile
                    Text(
                        text = if (language == AppLanguage.MARATHI) "अधिकारी आयडी / नोंदणीकृत मोबाईल नंबर *" else "Admin ID / Registered Mobile Number *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = adminIdOrMobile,
                        onValueChange = {
                            adminIdOrMobile = it
                            localValidationErr = null
                        },
                        placeholder = { Text("उदा. 9423889900 किंवा OFF-BULD-CHK-001") },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_id_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 2: Password
                    Text(
                        text = if (language == AppLanguage.MARATHI) "पासवर्ड (Password) *" else "Password *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            localValidationErr = null
                        },
                        placeholder = { Text("••••••••") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_password_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Field 3: OTP Verification
                    Text(
                        text = if (language == AppLanguage.MARATHI) "सुरक्षित OTP पडताळणी *" else "Secure OTP Verification *",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
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
                            placeholder = { Text("852963") },
                            leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_otp_input"),
                            shape = RoundedCornerShape(12.dp)
                        )

                        FilledTonalButton(
                            onClick = {
                                if (adminIdOrMobile.isBlank()) {
                                    localValidationErr = if (language == AppLanguage.MARATHI)
                                        "कृपया आधी अधिकारी आयडी किंवा मोबाईल नंबर टाका."
                                    else
                                        "Please enter Admin ID or Mobile first."
                                    return@FilledTonalButton
                                }
                                sentOtp = "852963"
                                otpInput = "852963"
                                otpSentMessage = if (language == AppLanguage.MARATHI)
                                    "OTP पाठवला: 852963 (आपोआप भरला)"
                                else
                                    "OTP sent: 852963 (auto-filled)"
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "OTP मिळवा" else "Get OTP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (!otpSentMessage.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "✓ $otpSentMessage",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Forgot Password Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                resetInput = adminIdOrMobile
                                showResetDialog = true
                            },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "पासवर्ड विसरलात? (Forgot Password)" else "Forgot Password?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Login Button
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            if (adminIdOrMobile.isBlank()) {
                                localValidationErr = if (language == AppLanguage.MARATHI) "कृपया अधिकारी आयडी किंवा मोबाईल नंबर प्रविष्ट करा." else "Please enter Admin ID or mobile number."
                                return@Button
                            }
                            if (password.isBlank()) {
                                localValidationErr = if (language == AppLanguage.MARATHI) "कृपया पासवर्ड प्रविष्ट करा." else "Please enter password."
                                return@Button
                            }
                            val validOtp = if (otpInput.isNotBlank()) otpInput.trim() else sentOtp
                            onLogin(adminIdOrMobile.trim(), password.trim(), validOtp, sentOtp)
                        },
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GramSaffron),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("admin_login_submit_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(if (language == AppLanguage.MARATHI) "पडताळणी चालू आहे..." else "Authenticating...", color = Color.White)
                        } else {
                            Icon(Icons.Default.Login, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.MARATHI) "प्रशासन कक्षामध्ये प्रवेश करा" else "Sign In to Admin Dashboard",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ================= ACTIVATION PROMPT CARD =================
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VpnKey,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "नवीन अधिकारी आहात का? (Account Activation)" else "New Officer? Activate Account",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.MARATHI)
                            "सुपर ॲडमिनने आपले खाते आधीच तयार केले असल्यास, येथे OTP पडताळणी करून आपला स्वतःचा पासवर्ड तयार करा व खाते सक्रिय करा."
                        else
                            "If your officer account was pre-provisioned by Super Admin, activate it here using OTP and create your secure password.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onNavigateToActivation,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_go_to_activation")
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "अधिकारी खाते सक्रिय करा (Activate)" else "Activate Officer Account",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ================= PRESETS CARD FOR TESTING =================
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
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
                                "🔒 पळसखेड दौलत ग्रामपंचायत अधिकारी चाचणी लॉगिन"
                            else
                                "🔒 Palaskhed Daulat GP Officer Test Logins",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.MARATHI)
                            "खालीलपैकी एका अधिकृत अधिकाऱ्याची निवड करून त्वरित लॉगिन करा:"
                        else
                            "Select an authorized officer below for instant test login:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val testOfficers = remember { MaharashtraDirectory.getAllPreapprovedOfficers().take(3) }
                    testOfficers.forEach { off ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${off.fullName} (${off.designation.take(22)}...)",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "मोबाईल: ${off.mobileNumber} • GP: पळसखेड दौलत",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Button(
                                onClick = {
                                    adminIdOrMobile = off.mobileNumber
                                    password = "Admin@${off.mobileNumber.takeLast(4)}"
                                    otpInput = "852963"
                                    sentOtp = "852963"
                                    localValidationErr = null
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) "भरा" else "Fill",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Return to Role Selection Button
            OutlinedButton(
                onClick = onBackToRoleSelection,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI) "भूमिका निवडीकडे परत जा" else "Back to Role Selection",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
