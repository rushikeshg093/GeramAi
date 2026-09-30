package com.example.data.remote

import android.util.Log
import com.example.data.local.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreDataSource {
    private val TAG = "FirestoreDataSource"

    val isFirebaseAvailable: Boolean
        get() = try {
            FirebaseApp.getInstance() != null
        } catch (e: Exception) {
            false
        }

    private val firestore: FirebaseFirestore?
        get() = try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w(TAG, "FirebaseFirestore not initialized: ${e.message}")
            null
        }

    // ================= PANCHAYAT PROFILE =================

    fun getPanchayatProfileFlow(gpId: String = "gp_palaskhed_daulat"): Flow<PanchayatProfile?> = callbackFlow {
        val db = firestore
        if (db == null) {
            val gpInfo = MaharashtraDirectory.getGramPanchayatById(gpId)
            val fallback = if (gpInfo != null) {
                PanchayatProfile(
                    id = gpInfo.id,
                    gramPanchayatId = gpInfo.id,
                    districtId = gpInfo.districtId,
                    talukaId = gpInfo.talukaId,
                    nameMr = gpInfo.nameMr,
                    nameEn = gpInfo.nameEn,
                    talukaMr = MaharashtraDirectory.getTalukaById(gpInfo.talukaId)?.nameMr ?: "चिखली",
                    talukaEn = MaharashtraDirectory.getTalukaById(gpInfo.talukaId)?.nameEn ?: "Chikhli",
                    districtMr = MaharashtraDirectory.getDistrictById(gpInfo.districtId)?.nameMr ?: "बुलढाणा",
                    districtEn = MaharashtraDirectory.getDistrictById(gpInfo.districtId)?.nameEn ?: "Buldhana",
                    pinCode = gpInfo.pinCode,
                    addressMr = gpInfo.officeAddressMr,
                    addressEn = gpInfo.officeAddressEn,
                    phone = gpInfo.phone,
                    email = gpInfo.email,
                    totalWards = gpInfo.totalWards
                )
            } else InitialData.initialPanchayatProfile
            trySend(fallback)
            awaitClose { }
            return@callbackFlow
        }

        val docId = if (gpId == "gp_palaskhed_daulat") "profile" else gpId
        val listener = db.collection("panchayat").document(docId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "getPanchayatProfile listener error: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val gpInfo = MaharashtraDirectory.getGramPanchayatById(gpId)
                    val profile = PanchayatProfile(
                        id = snapshot.id,
                        gramPanchayatId = snapshot.getString("gramPanchayatId") ?: gpId,
                        districtId = snapshot.getString("districtId") ?: (gpInfo?.districtId ?: "buldhana"),
                        talukaId = snapshot.getString("talukaId") ?: (gpInfo?.talukaId ?: "chikhli"),
                        nameMr = snapshot.getString("nameMr") ?: (gpInfo?.nameMr ?: InitialData.initialPanchayatProfile.nameMr),
                        nameEn = snapshot.getString("nameEn") ?: (gpInfo?.nameEn ?: InitialData.initialPanchayatProfile.nameEn),
                        talukaMr = snapshot.getString("talukaMr") ?: (gpInfo?.let { MaharashtraDirectory.getTalukaById(it.talukaId)?.nameMr } ?: InitialData.initialPanchayatProfile.talukaMr),
                        talukaEn = snapshot.getString("talukaEn") ?: (gpInfo?.let { MaharashtraDirectory.getTalukaById(it.talukaId)?.nameEn } ?: InitialData.initialPanchayatProfile.talukaEn),
                        districtMr = snapshot.getString("districtMr") ?: (gpInfo?.let { MaharashtraDirectory.getDistrictById(it.districtId)?.nameMr } ?: InitialData.initialPanchayatProfile.districtMr),
                        districtEn = snapshot.getString("districtEn") ?: (gpInfo?.let { MaharashtraDirectory.getDistrictById(it.districtId)?.nameEn } ?: InitialData.initialPanchayatProfile.districtEn),
                        pinCode = snapshot.getString("pinCode") ?: (gpInfo?.pinCode ?: InitialData.initialPanchayatProfile.pinCode),
                        addressMr = snapshot.getString("addressMr") ?: (gpInfo?.officeAddressMr ?: InitialData.initialPanchayatProfile.addressMr),
                        addressEn = snapshot.getString("addressEn") ?: (gpInfo?.officeAddressEn ?: InitialData.initialPanchayatProfile.addressEn),
                        phone = snapshot.getString("phone") ?: (gpInfo?.phone ?: InitialData.initialPanchayatProfile.phone),
                        email = snapshot.getString("email") ?: (gpInfo?.email ?: InitialData.initialPanchayatProfile.email),
                        website = snapshot.getString("website") ?: InitialData.initialPanchayatProfile.website,
                        officeHoursMr = snapshot.getString("officeHoursMr") ?: InitialData.initialPanchayatProfile.officeHoursMr,
                        officeHoursEn = snapshot.getString("officeHoursEn") ?: InitialData.initialPanchayatProfile.officeHoursEn,
                        logoUrl = snapshot.getString("logoUrl") ?: "",
                        totalPopulation = snapshot.getString("totalPopulation") ?: "८,४५०",
                        totalHouseholds = snapshot.getString("totalHouseholds") ?: "१,८२०",
                        totalWards = (snapshot.getLong("totalWards") ?: (gpInfo?.totalWards?.toLong() ?: 6L)).toInt()
                    )
                    trySend(profile)
                } else {
                    val gpInfo = MaharashtraDirectory.getGramPanchayatById(gpId)
                    val fallback = if (gpInfo != null) {
                        PanchayatProfile(
                            id = gpInfo.id,
                            gramPanchayatId = gpInfo.id,
                            districtId = gpInfo.districtId,
                            talukaId = gpInfo.talukaId,
                            nameMr = gpInfo.nameMr,
                            nameEn = gpInfo.nameEn,
                            talukaMr = MaharashtraDirectory.getTalukaById(gpInfo.talukaId)?.nameMr ?: "चिखली",
                            talukaEn = MaharashtraDirectory.getTalukaById(gpInfo.talukaId)?.nameEn ?: "Chikhli",
                            districtMr = MaharashtraDirectory.getDistrictById(gpInfo.districtId)?.nameMr ?: "बुलढाणा",
                            districtEn = MaharashtraDirectory.getDistrictById(gpInfo.districtId)?.nameEn ?: "Buldhana",
                            pinCode = gpInfo.pinCode,
                            addressMr = gpInfo.officeAddressMr,
                            addressEn = gpInfo.officeAddressEn,
                            phone = gpInfo.phone,
                            email = gpInfo.email,
                            totalWards = gpInfo.totalWards
                        )
                    } else InitialData.initialPanchayatProfile
                    trySend(fallback)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun savePanchayatProfile(profile: PanchayatProfile): Boolean {
        val db = firestore ?: return false
        return try {
            val docId = if (profile.gramPanchayatId == "gp_palaskhed_daulat") "profile" else profile.gramPanchayatId
            val map = hashMapOf(
                "gramPanchayatId" to profile.gramPanchayatId,
                "districtId" to profile.districtId,
                "talukaId" to profile.talukaId,
                "nameMr" to profile.nameMr,
                "nameEn" to profile.nameEn,
                "talukaMr" to profile.talukaMr,
                "talukaEn" to profile.talukaEn,
                "districtMr" to profile.districtMr,
                "districtEn" to profile.districtEn,
                "pinCode" to profile.pinCode,
                "addressMr" to profile.addressMr,
                "addressEn" to profile.addressEn,
                "phone" to profile.phone,
                "email" to profile.email,
                "website" to profile.website,
                "officeHoursMr" to profile.officeHoursMr,
                "officeHoursEn" to profile.officeHoursEn,
                "logoUrl" to profile.logoUrl,
                "totalPopulation" to profile.totalPopulation,
                "totalHouseholds" to profile.totalHouseholds,
                "totalWards" to profile.totalWards,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("panchayat").document(docId).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "savePanchayatProfile error: ${e.message}")
            false
        }
    }

    // ================= WARDS =================

    fun getWardsFlow(): Flow<List<WardEntity>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("wards")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getWards listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    WardEntity(
                        id = doc.id,
                        wardNumber = (doc.getLong("wardNumber") ?: 1L).toInt(),
                        nameMr = doc.getString("nameMr") ?: "",
                        nameEn = doc.getString("nameEn") ?: "",
                        descriptionMr = doc.getString("descriptionMr") ?: "",
                        descriptionEn = doc.getString("descriptionEn") ?: "",
                        population = doc.getString("population") ?: "",
                        representativeName = doc.getString("representativeName") ?: ""
                    )
                }?.sortedBy { it.wardNumber } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveWard(ward: WardEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (ward.id.isNotBlank()) ward.id else "ward_${ward.wardNumber}"
            val map = hashMapOf(
                "wardNumber" to ward.wardNumber,
                "nameMr" to ward.nameMr,
                "nameEn" to ward.nameEn,
                "descriptionMr" to ward.descriptionMr,
                "descriptionEn" to ward.descriptionEn,
                "population" to ward.population,
                "representativeName" to ward.representativeName,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("wards").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveWard error: ${e.message}")
            false
        }
    }

    suspend fun deleteWard(wardId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("wards").document(wardId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteWard error: ${e.message}")
            false
        }
    }

    // ================= WATER SUPPLY SCHEDULES =================

    fun getWaterSchedulesFlow(): Flow<List<WaterScheduleEntity>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("water_supply")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getWaterSchedules listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    val wardNum = (doc.getLong("wardNumber") ?: doc.id.replace("ward_", "").toLongOrNull() ?: 1L).toInt()
                    WaterScheduleEntity(
                        wardNumber = wardNum,
                        wardNameMr = doc.getString("wardNameMr") ?: "प्रभाग $wardNum",
                        wardNameEn = doc.getString("wardNameEn") ?: "Ward $wardNum",
                        morningTiming = doc.getString("morningTiming") ?: "",
                        eveningTiming = doc.getString("eveningTiming") ?: "",
                        daysMr = doc.getString("daysMr") ?: "दररोज",
                        daysEn = doc.getString("daysEn") ?: "Daily",
                        status = doc.getString("status") ?: "NORMAL",
                        statusNoteMr = doc.getString("statusNoteMr") ?: "पाणीपुरवठा सुरळीत",
                        statusNoteEn = doc.getString("statusNoteEn") ?: "Supply Normal",
                        operatorName = doc.getString("operatorName") ?: "संतोष पाटील",
                        operatorContact = doc.getString("operatorContact") ?: "+91 9422001122"
                    )
                }?.sortedBy { it.wardNumber } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveWaterSchedule(schedule: WaterScheduleEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val docId = "ward_${schedule.wardNumber}"
            val map = hashMapOf(
                "wardNumber" to schedule.wardNumber,
                "wardNameMr" to schedule.wardNameMr,
                "wardNameEn" to schedule.wardNameEn,
                "morningTiming" to schedule.morningTiming,
                "eveningTiming" to schedule.eveningTiming,
                "daysMr" to schedule.daysMr,
                "daysEn" to schedule.daysEn,
                "status" to schedule.status,
                "statusNoteMr" to schedule.statusNoteMr,
                "statusNoteEn" to schedule.statusNoteEn,
                "operatorName" to schedule.operatorName,
                "operatorContact" to schedule.operatorContact,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("water_supply").document(docId).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveWaterSchedule error: ${e.message}")
            false
        }
    }

    suspend fun deleteWaterSchedule(wardNumber: Int): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("water_supply").document("ward_$wardNumber").delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteWaterSchedule error: ${e.message}")
            false
        }
    }

    // ================= NOTICES =================

    fun getNoticesFlow(): Flow<List<NoticeEntity>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("notices")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getNotices listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    NoticeEntity(
                        id = doc.id,
                        titleMr = doc.getString("titleMr") ?: "",
                        titleEn = doc.getString("titleEn") ?: "",
                        descriptionMr = doc.getString("descriptionMr") ?: "",
                        descriptionEn = doc.getString("descriptionEn") ?: "",
                        category = doc.getString("category") ?: "ALL",
                        publishDate = doc.getString("publishDate") ?: "",
                        isUrgent = doc.getBoolean("isUrgent") ?: false,
                        attachmentTitle = doc.getString("attachmentTitle") ?: ""
                    )
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveNotice(notice: NoticeEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (notice.id.isNotBlank()) notice.id else "NOT-${System.currentTimeMillis()}"
            val map = hashMapOf(
                "titleMr" to notice.titleMr,
                "titleEn" to notice.titleEn,
                "descriptionMr" to notice.descriptionMr,
                "descriptionEn" to notice.descriptionEn,
                "category" to notice.category,
                "publishDate" to notice.publishDate,
                "isUrgent" to notice.isUrgent,
                "attachmentTitle" to notice.attachmentTitle,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("notices").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveNotice error: ${e.message}")
            false
        }
    }

    suspend fun deleteNotice(noticeId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("notices").document(noticeId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteNotice error: ${e.message}")
            false
        }
    }

    // ================= GRIEVANCES / COMPLAINTS =================

    fun getComplaintsFlow(): Flow<List<ComplaintEntity>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("complaints")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getComplaints listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    ComplaintEntity(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        category = doc.getString("category") ?: "other",
                        wardNumber = (doc.getLong("wardNumber") ?: 1L).toInt(),
                        locationDetail = doc.getString("locationDetail") ?: "",
                        status = doc.getString("status") ?: "PENDING",
                        createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                        updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis(),
                        officialRemarks = doc.getString("officialRemarks") ?: "",
                        assignedOfficer = doc.getString("assignedOfficer") ?: "",
                        rating = (doc.getLong("rating") ?: 0L).toInt(),
                        citizenFeedback = doc.getString("citizenFeedback") ?: "",
                        photoUri = doc.getString("photoUri") ?: ""
                    )
                }?.sortedByDescending { it.createdAt } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveComplaint(complaint: ComplaintEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "title" to complaint.title,
                "description" to complaint.description,
                "category" to complaint.category,
                "wardNumber" to complaint.wardNumber,
                "locationDetail" to complaint.locationDetail,
                "status" to complaint.status,
                "createdAt" to complaint.createdAt,
                "updatedAt" to System.currentTimeMillis(),
                "officialRemarks" to complaint.officialRemarks,
                "assignedOfficer" to complaint.assignedOfficer,
                "rating" to complaint.rating,
                "citizenFeedback" to complaint.citizenFeedback,
                "photoUri" to complaint.photoUri
            )
            db.collection("complaints").document(complaint.id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveComplaint error: ${e.message}")
            false
        }
    }

    suspend fun updateComplaintAdminFields(
        complaintId: String,
        status: String,
        remarks: String,
        officer: String
    ): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf<String, Any>(
                "status" to status,
                "officialRemarks" to remarks,
                "assignedOfficer" to officer,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("complaints").document(complaintId).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "updateComplaintAdminFields error: ${e.message}")
            false
        }
    }

    suspend fun deleteComplaint(complaintId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("complaints").document(complaintId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteComplaint error: ${e.message}")
            false
        }
    }

    // ================= OFFICIALS & STAFF =================

    fun getOfficialsFlow(): Flow<List<OfficialContactEntity>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("officials")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getOfficials listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    OfficialContactEntity(
                        id = doc.id,
                        nameMr = doc.getString("nameMr") ?: "",
                        nameEn = doc.getString("nameEn") ?: "",
                        designationMr = doc.getString("designationMr") ?: "",
                        designationEn = doc.getString("designationEn") ?: "",
                        phoneNumber = doc.getString("phoneNumber") ?: "",
                        wardOrDeptMr = doc.getString("wardOrDeptMr") ?: "",
                        wardOrDeptEn = doc.getString("wardOrDeptEn") ?: "",
                        imageRes = doc.getString("imageRes") ?: ""
                    )
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveOfficial(official: OfficialContactEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (official.id.isNotBlank()) official.id else "off_${System.currentTimeMillis()}"
            val map = hashMapOf(
                "nameMr" to official.nameMr,
                "nameEn" to official.nameEn,
                "designationMr" to official.designationMr,
                "designationEn" to official.designationEn,
                "phoneNumber" to official.phoneNumber,
                "wardOrDeptMr" to official.wardOrDeptMr,
                "wardOrDeptEn" to official.wardOrDeptEn,
                "imageRes" to official.imageRes,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("officials").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveOfficial error: ${e.message}")
            false
        }
    }

    suspend fun deleteOfficial(officialId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("officials").document(officialId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteOfficial error: ${e.message}")
            false
        }
    }

    // ================= DEVELOPMENT PROJECTS =================

    fun getProjectsFlow(): Flow<List<DevelopmentProjectEntity>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("projects")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getProjects listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    DevelopmentProjectEntity(
                        id = doc.id,
                        titleMr = doc.getString("titleMr") ?: "",
                        titleEn = doc.getString("titleEn") ?: "",
                        sanctionedBudget = doc.getString("sanctionedBudget") ?: "",
                        duration = doc.getString("duration") ?: "",
                        status = doc.getString("status") ?: "IN_PROGRESS",
                        location = doc.getString("location") ?: "",
                        descriptionMr = doc.getString("descriptionMr") ?: "",
                        descriptionEn = doc.getString("descriptionEn") ?: ""
                    )
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveProject(project: DevelopmentProjectEntity): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (project.id.isNotBlank()) project.id else "proj_${System.currentTimeMillis()}"
            val map = hashMapOf(
                "titleMr" to project.titleMr,
                "titleEn" to project.titleEn,
                "sanctionedBudget" to project.sanctionedBudget,
                "duration" to project.duration,
                "status" to project.status,
                "location" to project.location,
                "descriptionMr" to project.descriptionMr,
                "descriptionEn" to project.descriptionEn,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("projects").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveProject error: ${e.message}")
            false
        }
    }

    suspend fun deleteProject(projectId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("projects").document(projectId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteProject error: ${e.message}")
            false
        }
    }

    // ================= ONLINE SERVICES =================

    fun getServicesFlow(): Flow<List<OnlineServiceItem>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("online_services")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getServices listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    OnlineServiceItem(
                        id = doc.id,
                        key = doc.getString("key") ?: doc.id,
                        titleMr = doc.getString("titleMr") ?: "",
                        titleEn = doc.getString("titleEn") ?: "",
                        descriptionMr = doc.getString("descriptionMr") ?: "",
                        descriptionEn = doc.getString("descriptionEn") ?: "",
                        requiredDocsMr = doc.getString("requiredDocsMr") ?: "",
                        requiredDocsEn = doc.getString("requiredDocsEn") ?: "",
                        fee = (doc.getLong("fee") ?: 0L).toInt(),
                        processingDays = doc.getString("processingDays") ?: "३-५ दिवस",
                        active = doc.getBoolean("active") ?: true,
                        applicationLink = doc.getString("applicationLink") ?: ""
                    )
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveService(service: OnlineServiceItem): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (service.id.isNotBlank()) service.id else "srv_${System.currentTimeMillis()}"
            val map = hashMapOf(
                "key" to service.key,
                "titleMr" to service.titleMr,
                "titleEn" to service.titleEn,
                "descriptionMr" to service.descriptionMr,
                "descriptionEn" to service.descriptionEn,
                "requiredDocsMr" to service.requiredDocsMr,
                "requiredDocsEn" to service.requiredDocsEn,
                "fee" to service.fee,
                "processingDays" to service.processingDays,
                "active" to service.active,
                "applicationLink" to service.applicationLink,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("online_services").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveService error: ${e.message}")
            false
        }
    }

    suspend fun deleteService(serviceId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("online_services").document(serviceId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteService error: ${e.message}")
            false
        }
    }

    // ================= NOTIFICATIONS =================

    fun getNotificationsFlow(): Flow<List<NotificationItem>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("notifications")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getNotifications listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    NotificationItem(
                        id = doc.id,
                        titleMr = doc.getString("titleMr") ?: "",
                        titleEn = doc.getString("titleEn") ?: "",
                        messageMr = doc.getString("messageMr") ?: "",
                        messageEn = doc.getString("messageEn") ?: "",
                        timestamp = doc.getString("timestamp") ?: "नुकतेच",
                        isUrgent = doc.getBoolean("isUrgent") ?: false,
                        targetScreen = doc.getString("targetScreen") ?: "NOTIFICATIONS",
                        targetId = doc.getString("targetId") ?: "",
                        wardNumber = doc.getLong("wardNumber")?.toInt(),
                        createdAtEpoch = doc.getLong("createdAt") ?: 0L
                    )
                }?.sortedByDescending { it.createdAtEpoch } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveNotification(notif: NotificationItem): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (notif.id.isNotBlank()) notif.id else "notif_${System.currentTimeMillis()}"
            val map = hashMapOf(
                "titleMr" to notif.titleMr,
                "titleEn" to notif.titleEn,
                "messageMr" to notif.messageMr,
                "messageEn" to notif.messageEn,
                "timestamp" to notif.timestamp,
                "isUrgent" to notif.isUrgent,
                "targetScreen" to notif.targetScreen,
                "targetId" to notif.targetId,
                "wardNumber" to notif.wardNumber,
                "createdAt" to (if (notif.createdAtEpoch > 0) notif.createdAtEpoch else System.currentTimeMillis())
            )
            db.collection("notifications").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveNotification error: ${e.message}")
            false
        }
    }

    suspend fun deleteNotification(notifId: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("notifications").document(notifId).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteNotification error: ${e.message}")
            false
        }
    }

    // ================= CITIZEN PROFILES (ADMIN VIEW) =================

    fun getCitizensFlow(gramPanchayatId: String? = null): Flow<List<UserProfile>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("citizens")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getCitizens listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    val docGpId = doc.getString("gramPanchayatId") ?: "gp_palaskhed_daulat"
                    if (gramPanchayatId != null && docGpId != gramPanchayatId) {
                        null
                    } else {
                        UserProfile(
                            id = doc.id,
                            fullName = doc.getString("fullName") ?: "",
                            fullNameEn = doc.getString("fullNameEn") ?: "",
                            mobileNumber = doc.getString("mobileNumber") ?: "",
                            wardNumber = (doc.getLong("wardNumber") ?: 1L).toInt(),
                            aadharMasked = doc.getString("aadharMasked") ?: "",
                            address = doc.getString("address") ?: "",
                            addressEn = doc.getString("addressEn") ?: "",
                            grampanchayatNameMr = doc.getString("grampanchayatNameMr") ?: "आदर्श ग्रामपंचायत पळसखेड दौलत",
                            grampanchayatNameEn = doc.getString("grampanchayatNameEn") ?: "Model Grampanchayat Palaskhed Daulat",
                            talukaMr = doc.getString("talukaMr") ?: "चिखली",
                            talukaEn = doc.getString("talukaEn") ?: "Chikhli",
                            districtMr = doc.getString("districtMr") ?: "बुलढाणा",
                            districtEn = doc.getString("districtEn") ?: "Buldhana",
                            districtId = doc.getString("districtId") ?: "buldhana",
                            talukaId = doc.getString("talukaId") ?: "chikhli",
                            gramPanchayatId = docGpId,
                            verified = doc.getBoolean("verified") ?: true,
                            isRegistered = doc.getBoolean("isRegistered") ?: true
                        )
                    }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getCitizen(uid: String): UserProfile? {
        val db = firestore ?: return null
        if (uid.isBlank()) return null
        return try {
            val doc = db.collection("citizens").document(uid).get().await()
            if (doc != null && doc.exists()) {
                val docGpId = doc.getString("gramPanchayatId") ?: "gp_palaskhed_daulat"
                UserProfile(
                    id = doc.id,
                    fullName = doc.getString("fullName") ?: "",
                    fullNameEn = doc.getString("fullNameEn") ?: "",
                    mobileNumber = doc.getString("mobileNumber") ?: "",
                    wardNumber = (doc.getLong("wardNumber") ?: 1L).toInt(),
                    aadharMasked = doc.getString("aadharMasked") ?: "",
                    address = doc.getString("address") ?: "",
                    addressEn = doc.getString("addressEn") ?: "",
                    grampanchayatNameMr = doc.getString("grampanchayatNameMr") ?: "आदर्श ग्रामपंचायत पळसखेड दौलत",
                    grampanchayatNameEn = doc.getString("grampanchayatNameEn") ?: "Model Grampanchayat Palaskhed Daulat",
                    talukaMr = doc.getString("talukaMr") ?: "चिखली",
                    talukaEn = doc.getString("talukaEn") ?: "Chikhli",
                    districtMr = doc.getString("districtMr") ?: "बुलढाणा",
                    districtEn = doc.getString("districtEn") ?: "Buldhana",
                    districtId = doc.getString("districtId") ?: "buldhana",
                    talukaId = doc.getString("talukaId") ?: "chikhli",
                    gramPanchayatId = docGpId,
                    verified = doc.getBoolean("verified") ?: true,
                    isRegistered = doc.getBoolean("isRegistered") ?: true
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getCitizen error: ${e.message}")
            null
        }
    }

    suspend fun findCitizenByMobile(mobileNumber: String): UserProfile? {
        val db = firestore ?: return null
        val clean = mobileNumber.trim()
        if (clean.isBlank()) return null
        return try {
            val query = db.collection("citizens").whereEqualTo("mobileNumber", clean).limit(1).get().await()
            val doc = query.documents.firstOrNull()
            if (doc != null && doc.exists()) {
                val docGpId = doc.getString("gramPanchayatId") ?: "gp_palaskhed_daulat"
                UserProfile(
                    id = doc.id,
                    fullName = doc.getString("fullName") ?: "",
                    fullNameEn = doc.getString("fullNameEn") ?: "",
                    mobileNumber = doc.getString("mobileNumber") ?: clean,
                    wardNumber = (doc.getLong("wardNumber") ?: 1L).toInt(),
                    aadharMasked = doc.getString("aadharMasked") ?: "",
                    address = doc.getString("address") ?: "",
                    addressEn = doc.getString("addressEn") ?: "",
                    grampanchayatNameMr = doc.getString("grampanchayatNameMr") ?: "आदर्श ग्रामपंचायत पळसखेड दौलत",
                    grampanchayatNameEn = doc.getString("grampanchayatNameEn") ?: "Model Grampanchayat Palaskhed Daulat",
                    talukaMr = doc.getString("talukaMr") ?: "चिखली",
                    talukaEn = doc.getString("talukaEn") ?: "Chikhli",
                    districtMr = doc.getString("districtMr") ?: "बुलढाणा",
                    districtEn = doc.getString("districtEn") ?: "Buldhana",
                    districtId = doc.getString("districtId") ?: "buldhana",
                    talukaId = doc.getString("talukaId") ?: "chikhli",
                    gramPanchayatId = docGpId,
                    verified = doc.getBoolean("verified") ?: true,
                    isRegistered = doc.getBoolean("isRegistered") ?: true
                )
            } else null
        } catch (e: Exception) {
            Log.e(TAG, "findCitizenByMobile error: ${e.message}")
            null
        }
    }

    suspend fun getAuthorizedCitizen(mobileNumber: String): AuthorizedCitizen? {
        val db = firestore ?: return null
        val clean = mobileNumber.trim()
        if (clean.isBlank()) return null
        return try {
            val doc = db.collection("authorized_citizens").document(clean).get().await()
            if (doc != null && doc.exists()) {
                AuthorizedCitizen(
                    mobileNumber = doc.getString("mobileNumber") ?: clean,
                    fullName = doc.getString("fullName") ?: "",
                    fullNameEn = doc.getString("fullNameEn") ?: "",
                    districtId = doc.getString("districtId") ?: "",
                    talukaId = doc.getString("talukaId") ?: "",
                    gramPanchayatId = doc.getString("gramPanchayatId") ?: "",
                    wardNumber = (doc.getLong("wardNumber") ?: 1L).toInt(),
                    verified = doc.getBoolean("verified") ?: true
                )
            } else null
        } catch (e: Exception) {
            Log.e(TAG, "getAuthorizedCitizen error: ${e.message}")
            null
        }
    }

    suspend fun saveAuthorizedCitizen(citizen: AuthorizedCitizen): Boolean {
        val db = firestore ?: return false
        val clean = citizen.mobileNumber.trim()
        if (clean.isBlank()) return false
        return try {
            val map = hashMapOf(
                "mobileNumber" to clean,
                "fullName" to citizen.fullName,
                "fullNameEn" to citizen.fullNameEn,
                "districtId" to citizen.districtId,
                "talukaId" to citizen.talukaId,
                "gramPanchayatId" to citizen.gramPanchayatId,
                "wardNumber" to citizen.wardNumber,
                "verified" to citizen.verified,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("authorized_citizens").document(clean).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveAuthorizedCitizen error: ${e.message}")
            false
        }
    }

    suspend fun saveCitizen(profile: UserProfile): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "fullName" to profile.fullName,
                "fullNameEn" to profile.fullNameEn,
                "mobileNumber" to profile.mobileNumber,
                "wardNumber" to profile.wardNumber,
                "aadharMasked" to profile.aadharMasked,
                "address" to profile.address,
                "addressEn" to profile.addressEn,
                "grampanchayatNameMr" to profile.grampanchayatNameMr,
                "grampanchayatNameEn" to profile.grampanchayatNameEn,
                "talukaMr" to profile.talukaMr,
                "talukaEn" to profile.talukaEn,
                "districtMr" to profile.districtMr,
                "districtEn" to profile.districtEn,
                "districtId" to profile.districtId,
                "talukaId" to profile.talukaId,
                "gramPanchayatId" to profile.gramPanchayatId,
                "verified" to profile.verified,
                "isRegistered" to profile.isRegistered,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("citizens").document(profile.id).set(map, SetOptions.merge()).await()

            // Also keep authorized_citizens registry in sync
            val cleanMobile = profile.mobileNumber.trim()
            if (cleanMobile.isNotBlank()) {
                val authMap = hashMapOf(
                    "mobileNumber" to cleanMobile,
                    "fullName" to profile.fullName,
                    "fullNameEn" to profile.fullNameEn,
                    "districtId" to profile.districtId,
                    "talukaId" to profile.talukaId,
                    "gramPanchayatId" to profile.gramPanchayatId,
                    "wardNumber" to profile.wardNumber,
                    "verified" to profile.verified,
                    "updatedAt" to System.currentTimeMillis()
                )
                db.collection("authorized_citizens").document(cleanMobile).set(authMap, SetOptions.merge()).await()
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveCitizen error: ${e.message}")
            false
        }
    }

    // ================= INITIAL DATA SEEDING TO FIRESTORE =================

    suspend fun seedAllDataToFirestore(): Boolean {
        val db = firestore ?: return false
        return try {
            // 1. Panchayat Profile
            savePanchayatProfile(InitialData.initialPanchayatProfile)

            // 2. Wards
            for (ward in InitialData.initialWards) {
                saveWard(ward)
            }

            // 3. Water Schedules
            for (water in InitialData.initialWaterSchedules) {
                saveWaterSchedule(water)
            }

            // 4. Notices
            for (notice in InitialData.initialNotices) {
                saveNotice(notice)
            }

            // 5. Officials
            for (official in InitialData.officials) {
                saveOfficial(official)
            }

            // 6. Development Projects
            for (proj in InitialData.developmentProjects) {
                saveProject(proj)
            }

            // 7. Online Services
            for (srv in InitialData.initialServices) {
                saveService(srv)
            }

            // 8. Notifications
            for (notif in InitialData.initialNotifications) {
                saveNotification(notif)
            }

            // 9. Initial Citizen
            saveCitizen(InitialData.initialProfile)

            // 10. Initial Complaints
            for (comp in InitialData.initialComplaints) {
                saveComplaint(comp)
            }

            // 11. Initial AI Call Campaigns
            for (camp in InitialData.initialAiCampaigns) {
                saveAiCampaign(camp)
            }

            // 12. Initial AI Call Logs
            for (log in InitialData.initialAiCallLogs) {
                saveAiCallLog(log)
            }

            // 13. Initial AI Scheduled Calls
            for (sched in InitialData.initialAiScheduledCalls) {
                saveAiScheduledCall(sched)
            }

            // 14. Initial AI Call Settings
            saveAiCallSettings(InitialData.initialAiCallSettings)

            // 15. Pre-seed Authorized Citizens across Maharashtra
            for (authorized in MaharashtraDirectory.initialAuthorizedCitizens) {
                saveAuthorizedCitizen(authorized)
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "seedAllDataToFirestore error: ${e.message}")
            false
        }
    }

    // ================= AI CALL SYSTEM =================

    fun getAiCampaignsFlow(): Flow<List<AiCallCampaign>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(InitialData.initialAiCampaigns)
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("ai_call_campaigns")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getAiCampaigns listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    AiCallCampaign(
                        id = doc.id,
                        announcementId = doc.getString("announcementId") ?: "",
                        announcementTitle = doc.getString("announcementTitle") ?: "",
                        announcementMessage = doc.getString("announcementMessage") ?: "",
                        targetAudience = doc.getString("targetAudience") ?: "ALL",
                        targetAudienceLabel = doc.getString("targetAudienceLabel") ?: "सर्व नागरिक",
                        language = doc.getString("language") ?: "mr",
                        voiceId = doc.getString("voiceId") ?: "mr_female_1",
                        voiceName = doc.getString("voiceName") ?: "आरोही (मराठी महिला)",
                        status = doc.getString("status") ?: "COMPLETED",
                        totalRecipients = (doc.getLong("totalRecipients") ?: 0L).toInt(),
                        connectedCalls = (doc.getLong("connectedCalls") ?: 0L).toInt(),
                        unansweredCalls = (doc.getLong("unansweredCalls") ?: 0L).toInt(),
                        busyCalls = (doc.getLong("busyCalls") ?: 0L).toInt(),
                        failedCalls = (doc.getLong("failedCalls") ?: 0L).toInt(),
                        scheduledTime = doc.getLong("scheduledTime"),
                        startedAt = doc.getLong("startedAt") ?: System.currentTimeMillis(),
                        completedAt = doc.getLong("completedAt"),
                        isDemoMode = doc.getBoolean("isDemoMode") ?: true
                    )
                }?.sortedByDescending { it.startedAt } ?: emptyList()

                trySend(if (list.isEmpty()) InitialData.initialAiCampaigns else list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveAiCampaign(campaign: AiCallCampaign): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (campaign.id.isNotBlank()) campaign.id else "camp_${System.currentTimeMillis()}"
            val map = hashMapOf(
                "announcementId" to campaign.announcementId,
                "announcementTitle" to campaign.announcementTitle,
                "announcementMessage" to campaign.announcementMessage,
                "targetAudience" to campaign.targetAudience,
                "targetAudienceLabel" to campaign.targetAudienceLabel,
                "language" to campaign.language,
                "voiceId" to campaign.voiceId,
                "voiceName" to campaign.voiceName,
                "status" to campaign.status,
                "totalRecipients" to campaign.totalRecipients,
                "connectedCalls" to campaign.connectedCalls,
                "unansweredCalls" to campaign.unansweredCalls,
                "busyCalls" to campaign.busyCalls,
                "failedCalls" to campaign.failedCalls,
                "scheduledTime" to campaign.scheduledTime,
                "startedAt" to campaign.startedAt,
                "completedAt" to campaign.completedAt,
                "isDemoMode" to campaign.isDemoMode,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("ai_call_campaigns").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveAiCampaign error: ${e.message}")
            false
        }
    }

    fun getAiCallLogsFlow(): Flow<List<AiCallLog>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(InitialData.initialAiCallLogs)
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("ai_call_logs")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getAiCallLogs listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    AiCallLog(
                        id = doc.id,
                        campaignId = doc.getString("campaignId") ?: "",
                        citizenName = doc.getString("citizenName") ?: "",
                        mobileNumber = doc.getString("mobileNumber") ?: "",
                        wardNumber = (doc.getLong("wardNumber") ?: 1L).toInt(),
                        announcementTitle = doc.getString("announcementTitle") ?: "",
                        announcementMessage = doc.getString("announcementMessage") ?: "",
                        status = doc.getString("status") ?: "CONNECTED",
                        durationSec = (doc.getLong("durationSec") ?: 0L).toInt(),
                        timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis(),
                        isDemo = doc.getBoolean("isDemo") ?: true,
                        aiVoiceUsed = doc.getString("aiVoiceUsed") ?: "आरोही (मराठी)",
                        notes = doc.getString("notes") ?: ""
                    )
                }?.sortedByDescending { it.timestamp } ?: emptyList()

                trySend(if (list.isEmpty()) InitialData.initialAiCallLogs else list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveAiCallLog(log: AiCallLog): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (log.id.isNotBlank()) log.id else "log_${System.currentTimeMillis()}"
            val map = hashMapOf(
                "campaignId" to log.campaignId,
                "citizenName" to log.citizenName,
                "mobileNumber" to log.mobileNumber,
                "wardNumber" to log.wardNumber,
                "announcementTitle" to log.announcementTitle,
                "announcementMessage" to log.announcementMessage,
                "status" to log.status,
                "durationSec" to log.durationSec,
                "timestamp" to log.timestamp,
                "isDemo" to log.isDemo,
                "aiVoiceUsed" to log.aiVoiceUsed,
                "notes" to log.notes,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("ai_call_logs").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveAiCallLog error: ${e.message}")
            false
        }
    }

    fun getAiScheduledCallsFlow(): Flow<List<AiScheduledCall>> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(InitialData.initialAiScheduledCalls)
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("ai_scheduled_calls")
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w(TAG, "getAiScheduledCalls listener error: ${error.message}")
                    return@addSnapshotListener
                }
                val list = snapshots?.documents?.mapNotNull { doc ->
                    AiScheduledCall(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        message = doc.getString("message") ?: "",
                        language = doc.getString("language") ?: "mr",
                        voiceId = doc.getString("voiceId") ?: "mr_female_1",
                        targetAudience = doc.getString("targetAudience") ?: "ALL",
                        targetAudienceLabel = doc.getString("targetAudienceLabel") ?: "सर्व नागरिक",
                        recipientCount = (doc.getLong("recipientCount") ?: 0L).toInt(),
                        scheduledDate = doc.getString("scheduledDate") ?: "",
                        scheduledTime = doc.getString("scheduledTime") ?: "",
                        scheduledTimestamp = doc.getLong("scheduledTimestamp") ?: 0L,
                        status = doc.getString("status") ?: "PENDING",
                        createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                    )
                }?.sortedBy { it.scheduledTimestamp } ?: emptyList()

                trySend(if (list.isEmpty()) InitialData.initialAiScheduledCalls else list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveAiScheduledCall(call: AiScheduledCall): Boolean {
        val db = firestore ?: return false
        return try {
            val id = if (call.id.isNotBlank()) call.id else "sched_${System.currentTimeMillis()}"
            val map = hashMapOf(
                "title" to call.title,
                "message" to call.message,
                "language" to call.language,
                "voiceId" to call.voiceId,
                "targetAudience" to call.targetAudience,
                "targetAudienceLabel" to call.targetAudienceLabel,
                "recipientCount" to call.recipientCount,
                "scheduledDate" to call.scheduledDate,
                "scheduledTime" to call.scheduledTime,
                "scheduledTimestamp" to call.scheduledTimestamp,
                "status" to call.status,
                "createdAt" to call.createdAt,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("ai_scheduled_calls").document(id).set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveAiScheduledCall error: ${e.message}")
            false
        }
    }

    suspend fun deleteAiScheduledCall(id: String): Boolean {
        val db = firestore ?: return false
        return try {
            db.collection("ai_scheduled_calls").document(id).delete().await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "deleteAiScheduledCall error: ${e.message}")
            false
        }
    }

    fun getAiCallSettingsFlow(): Flow<AiCallSettings?> = callbackFlow {
        val db = firestore
        if (db == null) {
            trySend(InitialData.initialAiCallSettings)
            awaitClose { }
            return@callbackFlow
        }

        val listener = db.collection("ai_call_settings").document("default")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "getAiCallSettings listener error: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val settings = AiCallSettings(
                        defaultLanguage = snapshot.getString("defaultLanguage") ?: "mr",
                        defaultVoiceId = snapshot.getString("defaultVoiceId") ?: "mr_female_1",
                        callingHoursStart = snapshot.getString("callingHoursStart") ?: "09:00",
                        callingHoursEnd = snapshot.getString("callingHoursEnd") ?: "19:00",
                        maxCallsPerBatch = (snapshot.getLong("maxCallsPerBatch") ?: 50L).toInt(),
                        maxRetries = (snapshot.getLong("maxRetries") ?: 2L).toInt(),
                        isAiCallingEnabled = snapshot.getBoolean("isAiCallingEnabled") ?: true,
                        isDemoMode = snapshot.getBoolean("isDemoMode") ?: true,
                        webhookEndpointUrl = snapshot.getString("webhookEndpointUrl") ?: ""
                    )
                    trySend(settings)
                } else {
                    trySend(InitialData.initialAiCallSettings)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun saveAiCallSettings(settings: AiCallSettings): Boolean {
        val db = firestore ?: return false
        return try {
            val map = hashMapOf(
                "defaultLanguage" to settings.defaultLanguage,
                "defaultVoiceId" to settings.defaultVoiceId,
                "callingHoursStart" to settings.callingHoursStart,
                "callingHoursEnd" to settings.callingHoursEnd,
                "maxCallsPerBatch" to settings.maxCallsPerBatch,
                "maxRetries" to settings.maxRetries,
                "isAiCallingEnabled" to settings.isAiCallingEnabled,
                "isDemoMode" to settings.isDemoMode,
                "webhookEndpointUrl" to settings.webhookEndpointUrl,
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("ai_call_settings").document("default").set(map, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "saveAiCallSettings error: ${e.message}")
            false
        }
    }
}

