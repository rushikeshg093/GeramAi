package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.*
import com.example.ui.components.AppTopBar
import com.example.ui.components.StarRatingBar
import com.example.ui.components.StatusBadge
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.*
import com.example.ui.viewmodel.ComplaintDraftState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintsScreen(
    complaints: List<ComplaintEntity>,
    language: AppLanguage,
    currentFilter: String,
    draftState: ComplaintDraftState,
    showNewDialog: Boolean,
    selectedComplaintForDetail: ComplaintEntity?,
    onToggleLanguage: () -> Unit,
    onFilterChange: (String) -> Unit,
    onOpenNewForm: () -> Unit,
    onCloseNewForm: () -> Unit,
    onSelectComplaint: (ComplaintEntity?) -> Unit,
    onSubmitComplaint: (title: String, category: String, desc: String, ward: Int, loc: String, photo: String) -> Unit,
    onSubmitRating: (complaintId: String, rating: Int, feedback: String) -> Unit
) {
    val filteredComplaints = remember(complaints, currentFilter) {
        when (currentFilter) {
            "PENDING" -> complaints.filter { it.status == "PENDING" }
            "IN_PROGRESS" -> complaints.filter { it.status == "IN_PROGRESS" }
            "RESOLVED" -> complaints.filter { it.status == "RESOLVED" }
            else -> complaints
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (language == AppLanguage.MARATHI) "नागरिक तक्रार पेटी" else "Citizen Complaint Box",
                language = language,
                onToggleLanguage = onToggleLanguage
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOpenNewForm,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "नवीन तक्रार नोंदवा" else "New Complaint",
                        fontWeight = FontWeight.Bold
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("complaints_fab_new")
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Filter Tabs
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val filters = listOf(
                    "ALL" to if (language == AppLanguage.MARATHI) "सर्व (${complaints.size})" else "All (${complaints.size})",
                    "PENDING" to if (language == AppLanguage.MARATHI) "प्रलंबित (${complaints.count { it.status == "PENDING" }})" else "Pending (${complaints.count { it.status == "PENDING" }})",
                    "IN_PROGRESS" to if (language == AppLanguage.MARATHI) "प्रगतीपथावर (${complaints.count { it.status == "IN_PROGRESS" }})" else "In Progress (${complaints.count { it.status == "IN_PROGRESS" }})",
                    "RESOLVED" to if (language == AppLanguage.MARATHI) "निवारण झाले (${complaints.count { it.status == "RESOLVED" }})" else "Resolved (${complaints.count { it.status == "RESOLVED" }})"
                )

                items(filters) { (key, label) ->
                    FilterChip(
                        selected = currentFilter == key,
                        onClick = { onFilterChange(key) },
                        label = { Text(label, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("filter_chip_$key")
                    )
                }
            }

            if (filteredComplaints.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Inbox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (language == AppLanguage.MARATHI) "या वर्गवारीत कोणतीही तक्रार नाही." else "No grievances in this category.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredComplaints, key = { it.id }) { comp ->
                        ComplaintCardItem(
                            complaint = comp,
                            language = language,
                            onClick = { onSelectComplaint(comp) }
                        )
                    }
                }
            }
        }
    }

    // New Complaint Dialog
    if (showNewDialog) {
        NewComplaintDialog(
            draftState = draftState,
            language = language,
            onDismiss = onCloseNewForm,
            onSubmit = onSubmitComplaint
        )
    }

    // Complaint Detail & Rating Dialog
    if (selectedComplaintForDetail != null) {
        ComplaintDetailDialog(
            complaint = selectedComplaintForDetail,
            language = language,
            onDismiss = { onSelectComplaint(null) },
            onSubmitRating = onSubmitRating
        )
    }
}

@Composable
fun ComplaintCardItem(
    complaint: ComplaintEntity,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(complaint.createdAt))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("complaint_card_${complaint.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = getCategoryIcon(complaint.category),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = complaint.id,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                StatusBadge(status = complaint.status, language = language)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = complaint.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = complaint.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${if (language == AppLanguage.MARATHI) "प्रभाग" else "Ward"} ${complaint.wardNumber} • ${complaint.locationDetail}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (complaint.status == "RESOLVED" && complaint.rating > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${complaint.rating}/5",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewComplaintDialog(
    draftState: ComplaintDraftState,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (title: String, category: String, desc: String, ward: Int, loc: String, photo: String) -> Unit
) {
    var title by remember { mutableStateOf(draftState.title) }
    var selectedCategory by remember { mutableStateOf(draftState.category) }
    var description by remember { mutableStateOf(draftState.description) }
    var wardNumber by remember { mutableIntStateOf(draftState.wardNumber) }
    var locationDetail by remember { mutableStateOf(draftState.locationDetail) }
    var hasPhoto by remember { mutableStateOf(false) }

    val categories = listOf(
        ComplaintCategory.WATER,
        ComplaintCategory.ROAD,
        ComplaintCategory.STREETLIGHT,
        ComplaintCategory.GARBAGE,
        ComplaintCategory.DRAINAGE,
        ComplaintCategory.SANITATION,
        ComplaintCategory.AMENITIES,
        ComplaintCategory.OTHER
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp)
                .testTag("new_complaint_dialog")
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
                        text = if (language == AppLanguage.MARATHI) "नवीन तक्रार नोंदणी" else "Register Grievance",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category selector
                Text(
                    text = if (language == AppLanguage.MARATHI) "तक्रारीचा प्रकार (Category)" else "Category",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat.key,
                            onClick = { selectedCategory = cat.key },
                            label = { Text(if (language == AppLanguage.MARATHI) cat.titleMr else cat.titleEn) },
                            leadingIcon = {
                                Icon(
                                    imageVector = getCategoryIcon(cat.key),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "तक्रारीचा विषय (Title)" else "Title") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_title_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Ward selection
                Text(
                    text = if (language == AppLanguage.MARATHI) "प्रभाग क्रमांक (Ward)" else "Select Ward",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (w in 1..6) {
                        FilterChip(
                            selected = wardNumber == w,
                            onClick = { wardNumber = w },
                            label = { Text("वॉर्ड $w") },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Location / Landmark
                OutlinedTextField(
                    value = locationDetail,
                    onValueChange = { locationDetail = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "स्थान / चौक / गल्ली (Landmark)" else "Landmark / Lane") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_location_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "तक्रारीचे सविस्तर वर्णन (Description)" else "Description") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_description_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Photo attachment simulation
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasPhoto) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { hasPhoto = !hasPhoto }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = if (hasPhoto) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = if (hasPhoto) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (hasPhoto) {
                                if (language == AppLanguage.MARATHI) "फोटो जोडला गेला (१ फोटो)" else "Photo Attached (1 photo)"
                            } else {
                                if (language == AppLanguage.MARATHI) "समस्येचा फोटो जोडा (पर्यायी)" else "Attach Photo (Optional)"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (title.isNotBlank() || description.isNotBlank()) {
                            onSubmit(
                                title.ifBlank { if (language == AppLanguage.MARATHI) "नागरिक तक्रार" else "Citizen Grievance" },
                                selectedCategory,
                                description,
                                wardNumber,
                                locationDetail.ifBlank { "प्रभाग क्र. $wardNumber" },
                                if (hasPhoto) "attached_image_sample.jpg" else ""
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("complaint_dialog_submit_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.MARATHI) "तक्रार दाखल करा" else "Submit Grievance",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ComplaintDetailDialog(
    complaint: ComplaintEntity,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitRating: (complaintId: String, rating: Int, feedback: String) -> Unit
) {
    var selectedRating by remember { mutableIntStateOf(if (complaint.rating > 0) complaint.rating else 5) }
    var feedbackText by remember { mutableStateOf(complaint.citizenFeedback) }
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(complaint.createdAt))

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp)
                .testTag("complaint_detail_dialog")
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
                    Column {
                        Text(
                            text = complaint.id,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    StatusBadge(status = complaint.status, language = language)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = complaint.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = complaint.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(14.dp))

                // Timeline Tracking
                Text(
                    text = if (language == AppLanguage.MARATHI) "तक्रार निवारण प्रगती (Timeline)" else "Resolution Progress Timeline",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                val steps = listOf(
                    Triple(
                        if (language == AppLanguage.MARATHI) "१. तक्रार नोंदणी" else "1. Grievance Logged",
                        if (language == AppLanguage.MARATHI) "तक्रार हेल्पडेस्कला प्राप्त झाली" else "Received by Helpdesk",
                        true
                    ),
                    Triple(
                        if (language == AppLanguage.MARATHI) "२. तपासणी व अधिकारी नेमणूक" else "2. Verification & Officer Assigned",
                        complaint.assignedOfficer.ifBlank { if (language == AppLanguage.MARATHI) "संबंधित प्रभाग अधिकारी" else "Ward Officer" },
                        complaint.status != "PENDING"
                    ),
                    Triple(
                        if (language == AppLanguage.MARATHI) "३. जागेवर दुरुस्ती व निवारण" else "3. On-ground Resolution",
                        complaint.officialRemarks.ifBlank { if (language == AppLanguage.MARATHI) "काम प्रगतीपथावर आहे" else "Work underway" },
                        complaint.status == "RESOLVED"
                    )
                )

                steps.forEachIndexed { index, (title, subtitle, isDone) ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isDone) MaterialTheme.colorScheme.primary else Color.LightGray,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text("${index + 1}", color = Color.White, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isDone) MaterialTheme.colorScheme.onSurface else Color.Gray
                            )
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Official Remarks
                if (complaint.officialRemarks.isNotBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (language == AppLanguage.MARATHI) "अधिकारी शेरा / टिप्पणी:" else "Official Remarks:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = complaint.officialRemarks,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Rating & Feedback Section (Page 2: निवारणानंतर 1–5 स्टार रेटिंग)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (language == AppLanguage.MARATHI) "नागरिक अभिप्राय व रेटिंग (Feedback & Rating)" else "Citizen Rating & Feedback",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == AppLanguage.MARATHI)
                        "कामकाजाच्या गुणवत्तेनुसार १ ते ५ स्टार रेटिंग द्या:"
                    else
                        "Rate resolution quality from 1 to 5 stars:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                StarRatingBar(
                    rating = selectedRating,
                    onRatingSelected = { selectedRating = it },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    label = { Text(if (language == AppLanguage.MARATHI) "आपला अभिप्राय / सूचना लिहा" else "Write feedback / suggestions") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_feedback_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        onSubmitRating(complaint.id, selectedRating, feedbackText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("complaint_rating_submit_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.MARATHI) "रेटिंग सेव्ह करा" else "Save Feedback & Rating",
                        fontWeight = FontWeight.Bold
                    )
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
