package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.AppLanguage
import com.example.data.local.DevelopmentProjectEntity
import com.example.data.local.OfficialContactEntity
import com.example.data.local.PanchayatProfile
import com.example.data.local.UserProfile
import com.example.data.local.WardEntity
import com.example.ui.components.AppTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun GrampanchayatInfoScreen(
    userProfile: UserProfile?,
    panchayatProfile: PanchayatProfile?,
    wards: List<WardEntity> = emptyList(),
    officials: List<OfficialContactEntity>,
    projects: List<DevelopmentProjectEntity>,
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
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

    val pin = panchayatProfile?.pinCode?.ifBlank { "443201" } ?: "443201"
    val officeHours = if (language == AppLanguage.MARATHI) {
        panchayatProfile?.officeHoursMr?.ifBlank { "सोम ते शनि, सकाळी १०:०० ते सायं ५:४५" } ?: "सोम ते शनि, सकाळी १०:०० ते सायं ५:४५"
    } else {
        panchayatProfile?.officeHoursEn?.ifBlank { "Mon to Sat, 10:00 AM - 5:45 PM" } ?: "Mon to Sat, 10:00 AM - 5:45 PM"
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (language == AppLanguage.MARATHI) "ग्रामपंचायत माहिती व संपर्क" else "Grampanchayat Info & Directory",
                language = language,
                onToggleLanguage = onToggleLanguage,
                showBackButton = true,
                onBack = onBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding() + 12.dp,
                bottom = paddingValues.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .testTag("gp_info_screen")
        ) {
            // 1. Grampanchayat Header Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.gp_logo),
                                    contentDescription = "GP Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = gpName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = if (language == AppLanguage.MARATHI)
                                "ता. $taluka, जि. $district • पिन: $pin"
                            else
                                "Tal. $taluka, Dist. $district • PIN: $pin",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.MARATHI)
                                        "कार्यालयीन वेळ: $officeHours"
                                    else
                                        "Office Hours: $officeHours",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // 2. Village Statistics Grid (Connected to Panchayat Profile / Wards)
            item {
                Text(
                    text = if (language == AppLanguage.MARATHI) "गावाची सांख्यिकी व माहिती" else "Village Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                val popCount = panchayatProfile?.totalPopulation?.toString() ?: "८,४५०"
                val houseCount = panchayatProfile?.totalHouseholds?.toString() ?: "१,८२०"
                val wardCount = if (wards.isNotEmpty()) "${wards.size} वॉर्ड" else "${panchayatProfile?.totalWards ?: 6} वॉर्ड"

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val stats = listOf(
                        Triple(if (language == AppLanguage.MARATHI) "एकूण लोकसंख्या" else "Population", popCount, Icons.Default.People),
                        Triple(if (language == AppLanguage.MARATHI) "एकूण कुटुंबे" else "Households", houseCount, Icons.Default.Home),
                        Triple(if (language == AppLanguage.MARATHI) "प्रभाग संख्या" else "Wards", wardCount, Icons.Default.LocationCity)
                    )

                    for ((label, value, icon) in stats) {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // 2b. Contact Info Card (Phone, Email, Address)
            if (panchayatProfile != null && (panchayatProfile.phone.isNotBlank() || panchayatProfile.email.isNotBlank() || panchayatProfile.addressMr.isNotBlank())) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "कार्यालय संपर्क तपशील" else "Office Contact Details",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            if (panchayatProfile.phone.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(panchayatProfile.phone, style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            if (panchayatProfile.email.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(panchayatProfile.email, style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            if (panchayatProfile.addressMr.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        if (language == AppLanguage.MARATHI) panchayatProfile.addressMr else panchayatProfile.addressEn.ifBlank { panchayatProfile.addressMr },
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Representatives & Staff Directory
            item {
                Text(
                    text = if (language == AppLanguage.MARATHI) "पदाधिकारी व अधिकारी संपर्क सूची" else "Elected Members & Staff Directory",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(officials) { official ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) official.nameMr else official.nameEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = if (language == AppLanguage.MARATHI) official.designationMr else official.designationEn,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "📞 ${official.phoneNumber}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${official.phoneNumber}"))
                                context.startActivity(intent)
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call")
                        }
                    }
                }
            }

            // 4. Village Development Projects
            item {
                Text(
                    text = if (language == AppLanguage.MARATHI) "गावातील प्रमुख विकासकामे" else "Village Development Works",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(projects) { proj ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) proj.titleMr else proj.titleEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            StatusBadge(status = proj.status, language = language)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "मंजूर निधी: ${proj.sanctionedBudget}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "कालावधी: ${proj.duration}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
