package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.AppLanguage
import com.example.ui.components.LanguageToggleChip
import com.example.ui.theme.GramSaffron
import com.example.ui.theme.GramSaffronLight

@Composable
fun RoleSelectionScreen(
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onSelectCitizen: () -> Unit,
    onSelectAdmin: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .testTag("role_selection_container"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gp_logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "महाराष्ट्र शासन • ग्रामविकास विभाग" else "Govt of Maharashtra • Rural Development",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (language == AppLanguage.MARATHI) "ई-ग्रामपंचायत पोर्टल" else "E-Grampanchayat Portal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            LanguageToggleChip(
                currentLanguage = language,
                onToggle = onToggleLanguage
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Title & Description
        Text(
            text = if (language == AppLanguage.MARATHI) "प्रवेश पर्याय निवडा" else "Select Portal Role",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (language == AppLanguage.MARATHI)
                "कृपया खालीलपैकी आपला योग्य पर्याय निवडून पुढे जा"
            else
                "Please choose your role to access the appropriate dashboard",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ================= OPTION 1: CITIZEN =================
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                .clickable { onSelectCitizen() }
                .testTag("role_select_citizen_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "नागरिक प्रवेश" else "Citizen Access",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.MARATHI) "१. नागरिक (Citizen)" else "1. Citizen Portal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "गावकरी, शेतकरी व स्थानिक नागरिकांसाठी सार्वजनिक ई-सेवा पोर्टल."
                    else
                        "Universal portal for villagers, farmers & registered village citizens.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Feature items
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    RoleFeatureBullet(
                        icon = Icons.Default.Description,
                        text = if (language == AppLanguage.MARATHI) "जन्म, मृत्यू, विवाह व BPL ऑनलाइन दाखले" else "Birth, death, marriage & NOC certificates"
                    )
                    RoleFeatureBullet(
                        icon = Icons.Default.WaterDrop,
                        text = if (language == AppLanguage.MARATHI) "प्रभागनिहाय पाणीपुरवठा व टँकर बुकिंग" else "Ward water schedules & tanker booking"
                    )
                    RoleFeatureBullet(
                        icon = Icons.Default.ReportProblem,
                        text = if (language == AppLanguage.MARATHI) "तक्रार निवारण व ग्राममित्र AI सहाय्यक" else "Grievance redressal & AI Gram-Mitra help"
                    )
                    RoleFeatureBullet(
                        icon = Icons.Default.Campaign,
                        text = if (language == AppLanguage.MARATHI) "ग्रामसभा नोटीस बोर्ड व विकास कामे माहिती" else "Gramsabha notices & development projects"
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onSelectCitizen,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_select_citizen")
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "नागरिक म्हणून पुढे जा" else "Continue as Citizen",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ================= OPTION 2: ADMIN / OFFICER =================
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GramSaffron.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .clickable { onSelectAdmin() }
                .testTag("role_select_admin_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = CircleShape,
                        color = GramSaffron.copy(alpha = 0.15f),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = GramSaffron,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.MARATHI) "फक्त अधिकृत अधिकारी" else "Restricted Officer Access",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.MARATHI) "२. अधिकारी / प्रशासक (Admin / Officer)" else "2. Officer / Admin Portal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "सरपंच, ग्रामसेवक, तलाठी व अधिकृत कर्मचाऱ्यांसाठी स्वतंत्र प्रशासकीय कक्ष."
                    else
                        "Restricted management portal for Sarpanch, Gramsevak, Talathi & GP officers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Feature items
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    RoleFeatureBullet(
                        icon = Icons.Default.Security,
                        text = if (language == AppLanguage.MARATHI) "अधिकारी आयडी / मोबाईल + पासवर्ड + OTP पडताळणी" else "Admin ID / Mobile + Password + OTP verification"
                    )
                    RoleFeatureBullet(
                        icon = Icons.Default.CheckCircle,
                        text = if (language == AppLanguage.MARATHI) "सुपर ॲडमिन पूर्व-मंजूर अधिकारी खाते सक्रियीकरण" else "Super Admin pre-approved officer activation"
                    )
                    RoleFeatureBullet(
                        icon = Icons.Default.AssignmentTurnedIn,
                        text = if (language == AppLanguage.MARATHI) "नागरिक तक्रार निवारण व अधिकृत शेरे देणे" else "Citizen grievance resolution & officer remarks"
                    )
                    RoleFeatureBullet(
                        icon = Icons.Default.PhoneCallback,
                        text = if (language == AppLanguage.MARATHI) "🤖 AI स्वयंचलित व्हॉइस कॉल ब्रॉडकास्ट सिस्टीम" else "🤖 AI automated citizen voice call system"
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onSelectAdmin,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GramSaffron),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_select_admin")
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "अधिकारी म्हणून प्रवेश करा" else "Enter as Officer / Admin",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Security Policy Notice Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "कडक सुरक्षा नियम: सामान्य नागरिक अधिकारी खाते तयार करू शकत नाहीत. अधिकारी नोंदणीसाठी सुपर ॲडमिनने खाते पूर्व-मंजूर केलेले असणे अनिवार्य आहे."
                    else
                        "Strict Security: Normal citizens cannot register as officers. Officer accounts must be pre-created by Super Admin before activation.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (language == AppLanguage.MARATHI)
                "महाराष्ट्र शासन • ग्रामविकास व पंचायत राज विभाग\nडिजिटल ई-ग्रामपंचायत पुढाकार"
            else
                "Govt of Maharashtra • Rural Development & Panchayati Raj\nDigital E-Grampanchayat Initiative",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RoleFeatureBullet(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
