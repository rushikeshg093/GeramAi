package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.AppLanguage
import com.example.data.local.TankerBookingEntity
import com.example.data.local.UserProfile
import com.example.data.local.WaterScheduleEntity
import com.example.ui.components.AppTopBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@Composable
fun WaterServicesScreen(
    waterSchedules: List<WaterScheduleEntity>,
    tankerBookings: List<TankerBookingEntity>,
    userProfile: UserProfile?,
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onBack: () -> Unit,
    onBookTanker: (name: String, mobile: String, ward: Int, address: String, date: String, slot: String, purpose: String) -> Unit
) {
    val context = LocalContext.current
    var showBookingModal by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Timetable, 1: Tanker Booking

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (language == AppLanguage.MARATHI) "पाणीपुरवठा सेवा व वेळापत्रक" else "Water Supply Services",
                language = language,
                onToggleLanguage = onToggleLanguage,
                showBackButton = true,
                onBack = onBack
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showBookingModal = true },
                icon = { Icon(Icons.Default.LocalShipping, contentDescription = null) },
                text = {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "टँकर बुक करा" else "Book Water Tanker",
                        fontWeight = FontWeight.Bold
                    )
                },
                containerColor = Color(0xFF0288D1),
                contentColor = Color.White,
                modifier = Modifier.testTag("water_fab_book_tanker")
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Tab Header
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
                            text = if (language == AppLanguage.MARATHI) "वॉर्ड वेळापत्रक (Timetable)" else "Ward Timetable",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.MARATHI) "माझे टँकर अर्ज (${tankerBookings.size})" else "My Tankers (${tankerBookings.size})",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Emergency Helpline Banner
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE1F5FE)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(14.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF0288D1),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.PhoneInTalk,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (language == AppLanguage.MARATHI) "पाणीपुरवठा नियंत्रण कक्ष (हेल्पलाईन)" else "Water Supply Control Room Helpline",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF01579B)
                                    )
                                    Text(
                                        text = if (language == AppLanguage.MARATHI)
                                            "संतोष पाटील (प्रमुख): +91 9422001122"
                                        else
                                            "Santosh Patil (Operator): +91 9422001122",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF0277BD)
                                    )
                                }
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9422001122"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(if (language == AppLanguage.MARATHI) "कॉल करा" else "Call", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // List of All Wards
                    items(waterSchedules) { item ->
                        val isMyWard = item.wardNumber == (userProfile?.wardNumber ?: 3)
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isMyWard) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                            ),
                            border = if (isMyWard) CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)
                            ) else null,
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF0288D1)
                                        ) {
                                            Text(
                                                text = "वॉर्ड ${item.wardNumber}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }

                                        if (isMyWard) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = GramSaffron
                                            ) {
                                                Text(
                                                    text = if (language == AppLanguage.MARATHI) "माझा प्रभाग" else "My Ward",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }

                                    StatusBadge(status = item.status, language = language)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (language == AppLanguage.MARATHI) item.wardNameMr else item.wardNameEn,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.MARATHI) "🌅 सकाळ वेळ" else "🌅 Morning",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = item.morningTiming,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.MARATHI) "🌇 संध्याकाळ वेळ" else "🌇 Evening",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = item.eveningTiming,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "📅 ${if (language == AppLanguage.MARATHI) item.daysMr else item.daysEn}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            } else {
                // Tanker Bookings
                if (tankerBookings.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (language == AppLanguage.MARATHI)
                                    "आपण अद्याप कोणताही पाणी टँकर बुक केलेला नाही."
                                else
                                    "No water tanker bookings yet.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tankerBookings) { booking ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = booking.id,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0288D1)
                                        )
                                        StatusBadge(status = booking.status, language = language)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "📍 ${booking.deliveryAddress} (प्रभाग ${booking.wardNumber})",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "🕒 तारीख: ${booking.requiredDate} • वेळ: ${booking.timeSlot}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "हेतू: ${booking.purpose}",
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
    }

    // Modal Dialog for Tanker Booking
    if (showBookingModal) {
        TankerBookingDialog(
            userProfile = userProfile,
            language = language,
            onDismiss = { showBookingModal = false },
            onSubmit = { name, mobile, ward, addr, date, slot, purpose ->
                onBookTanker(name, mobile, ward, addr, date, slot, purpose)
                showBookingModal = false
                selectedTab = 1
            }
        )
    }
}

@Composable
fun TankerBookingDialog(
    userProfile: UserProfile?,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Int, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(userProfile?.fullName ?: "राजेश विष्णू सावंत") }
    var mobile by remember { mutableStateOf(userProfile?.mobileNumber ?: "9876543210") }
    var ward by remember { mutableIntStateOf(userProfile?.wardNumber ?: 3) }
    var address by remember { mutableStateOf(userProfile?.address ?: "मारुती मंदिर चौक, पळसखेड दौलत") }
    var requiredDate by remember { mutableStateOf("आज (Today)") }
    var timeSlot by remember { mutableStateOf("सकाळी ९:०० ते ११:०० (Morning)") }
    var purpose by remember { mutableStateOf("घरगुती पिण्याचे पाणी टंचाई / कार्यक्रम") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("tanker_booking_dialog")
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
                        text = if (language == AppLanguage.MARATHI) "पाणी टँकर मागणी अर्ज" else "Water Tanker Request",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0288D1)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "अर्जदाराचे नाव" else "Applicant Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "मोबाईल नंबर" else "Mobile Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "टँकर पोहोच पत्ता / गल्ली" else "Delivery Address / Lane") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "पाणी लागण्याचे कारण / हेतू" else "Purpose / Event") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onSubmit(name, mobile, ward, address, requiredDate, timeSlot, purpose)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("tanker_dialog_submit_button")
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.MARATHI) "टँकर बुकिंग निश्चित करा" else "Confirm Tanker Booking",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
