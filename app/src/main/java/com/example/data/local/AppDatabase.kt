package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfileDirect(): UserProfile?

    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    suspend fun getUserProfileById(id: String): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()
}

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Query("SELECT COUNT(*) FROM complaints")
    suspend fun getComplaintCount(): Int

    @Query("SELECT * FROM complaints WHERE id = :id LIMIT 1")
    suspend fun getComplaintById(id: String): ComplaintEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComplaints(complaints: List<ComplaintEntity>)

    @Update
    suspend fun updateComplaint(complaint: ComplaintEntity)

    @Query("DELETE FROM complaints WHERE id = :id")
    suspend fun deleteComplaintById(id: String)

    @Query("DELETE FROM complaints")
    suspend fun deleteAllComplaints()

    @Transaction
    suspend fun syncComplaints(complaints: List<ComplaintEntity>) {
        deleteAllComplaints()
        insertAllComplaints(complaints)
    }

    @Query("UPDATE complaints SET rating = :rating, citizenFeedback = :feedback WHERE id = :id")
    suspend fun updateRatingAndFeedback(id: String, rating: Int, feedback: String)
}

@Dao
interface NoticeDao {
    @Query("SELECT * FROM notices ORDER BY isUrgent DESC, id DESC")
    fun getAllNotices(): Flow<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<NoticeEntity>)

    @Query("DELETE FROM notices WHERE id = :id")
    suspend fun deleteNoticeById(id: String)

    @Query("DELETE FROM notices")
    suspend fun deleteAllNotices()

    @Transaction
    suspend fun syncNotices(notices: List<NoticeEntity>) {
        deleteAllNotices()
        insertNotices(notices)
    }
}

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_schedules ORDER BY wardNumber ASC")
    fun getAllWaterSchedules(): Flow<List<WaterScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<WaterScheduleEntity>)

    @Query("DELETE FROM water_schedules WHERE wardNumber = :wardNumber")
    suspend fun deleteScheduleByWard(wardNumber: Int)

    @Query("DELETE FROM water_schedules")
    suspend fun deleteAllSchedules()

    @Transaction
    suspend fun syncWaterSchedules(schedules: List<WaterScheduleEntity>) {
        deleteAllSchedules()
        insertSchedules(schedules)
    }

    @Query("SELECT * FROM tanker_bookings ORDER BY bookedAt DESC")
    fun getAllTankerBookings(): Flow<List<TankerBookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTankerBooking(booking: TankerBookingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTankerBookings(bookings: List<TankerBookingEntity>)

    @Query("DELETE FROM tanker_bookings")
    suspend fun deleteAllTankerBookings()

    @Transaction
    suspend fun syncTankerBookings(bookings: List<TankerBookingEntity>) {
        deleteAllTankerBookings()
        insertAllTankerBookings(bookings)
    }
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM service_applications ORDER BY appliedDate DESC")
    fun getAllApplications(): Flow<List<ServiceApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ServiceApplicationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllApplications(applications: List<ServiceApplicationEntity>)

    @Query("DELETE FROM service_applications")
    suspend fun deleteAllApplications()

    @Transaction
    suspend fun syncApplications(applications: List<ServiceApplicationEntity>) {
        deleteAllApplications()
        insertAllApplications(applications)
    }
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChat()
}

@Database(
    entities = [
        UserProfile::class,
        ComplaintEntity::class,
        NoticeEntity::class,
        WaterScheduleEntity::class,
        TankerBookingEntity::class,
        ServiceApplicationEntity::class,
        ChatMessageEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun noticeDao(): NoticeDao
    abstract fun waterDao(): WaterDao
    abstract fun serviceDao(): ServiceDao
    abstract fun chatDao(): ChatDao
}
