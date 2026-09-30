package com.example.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.local.NotificationItem
import java.util.concurrent.ConcurrentHashMap

object NotificationHelper {

    private const val TAG = "NotificationHelper"
    const val CHANNEL_ID = "gp_public_notifications_channel"
    const val CHANNEL_NAME = "ग्रामपंचायत सार्वजनिक सूचना (Public Alerts)"

    const val EXTRA_TARGET_SCREEN = "EXTRA_TARGET_SCREEN"
    const val EXTRA_TARGET_ID = "EXTRA_TARGET_ID"
    const val EXTRA_NOTIF_TITLE = "EXTRA_NOTIF_TITLE"
    const val EXTRA_NOTIF_BODY = "EXTRA_NOTIF_BODY"
    const val EXTRA_WARD_NUMBER = "EXTRA_WARD_NUMBER"

    // Dedup memory cache: stores notification IDs and keys to guarantee single notification per event
    private val dispatchedIds = ConcurrentHashMap.newKeySet<String>()
    private val dispatchedKeys = ConcurrentHashMap<String, Long>()

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                importance
            ).apply {
                description = "ग्रामपंचायत सार्वजनिक पाणीपुरवठा, सूचना, योजना, व विकास कामांच्या सूचना"
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Determines if a change is citizen-facing/public or personal/private.
     * PUBLIC/CITIZEN-FACING -> returns true (Send notification)
     * PERSONAL/PRIVATE -> returns false (Never send notification)
     */
    fun isPublicCitizenFacingChange(category: String): Boolean {
        val cat = category.uppercase().trim()
        // Strictly Private/Personal Categories -> NEVER send notification
        if (cat in listOf(
            "CITIZEN",
            "CITIZEN_PROFILE",
            "CITIZEN_NAME",
            "CITIZEN_MOBILE",
            "CITIZEN_ADDRESS",
            "CITIZEN_DOCUMENT",
            "CITIZEN_PRIVATE",
            "INTERNAL_ADMIN",
            "INTERNAL_ADMIN_SETTINGS",
            "ADMIN_CALL_CAMPAIGN",
            "ADMIN_AI_SETTINGS"
        )) {
            return false
        }

        // Explicitly Public / Citizen-Facing Categories -> Send notification
        return when {
            cat.contains("WATER") ||
            cat.contains("NOTICE") ||
            cat.contains("GRAM_SABHA") ||
            cat.contains("PANCHAYAT") ||
            cat.contains("PUBLIC") ||
            cat.contains("ANNOUNCEMENT") ||
            cat.contains("EVENT") ||
            cat.contains("PROJECT") ||
            cat.contains("SERVICE") ||
            cat.contains("SCHEME") ||
            cat.contains("WARD") ||
            cat.contains("ALERT") ||
            cat.contains("BROADCAST") -> true

            else -> false
        }
    }

    fun showPublicNotification(context: Context, notif: NotificationItem, isMarathi: Boolean = true) {
        try {
            // Deduplication Check 1: Stable notification ID (never notify twice for exact same event ID)
            if (notif.id.isNotBlank() && !dispatchedIds.add(notif.id)) {
                Log.d(TAG, "Notification ID ${notif.id} already shown on this device. Skipping.")
                return
            }

            // Deduplication Check 2: Content signature cooldown (prevents rapid double-clicks on save)
            val dedupKey = "${notif.titleMr}_${notif.messageMr}_${notif.targetScreen}_${notif.wardNumber}"
            val now = System.currentTimeMillis()
            val lastSent = dispatchedKeys[dedupKey]
            if (lastSent != null && (now - lastSent) < 15_000) {
                Log.d(TAG, "Notification content signature '$dedupKey' already dispatched recently, skipping duplicate.")
                return
            }
            dispatchedKeys[dedupKey] = now

            // Clean older memory entries
            if (dispatchedKeys.size > 200) {
                dispatchedKeys.entries.removeIf { (now - it.value) > 60_000 }
            }
            if (dispatchedIds.size > 500) {
                dispatchedIds.clear()
            }

            createNotificationChannel(context)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(EXTRA_TARGET_SCREEN, notif.targetScreen)
                putExtra(EXTRA_TARGET_ID, notif.targetId)
                putExtra(EXTRA_NOTIF_TITLE, if (isMarathi) notif.titleMr.ifBlank { notif.titleEn } else notif.titleEn.ifBlank { notif.titleMr })
                putExtra(EXTRA_NOTIF_BODY, if (isMarathi) notif.messageMr.ifBlank { notif.messageEn } else notif.messageEn.ifBlank { notif.messageMr })
                putExtra(EXTRA_WARD_NUMBER, notif.wardNumber ?: -1)
            }

            val requestCode = (notif.id.hashCode() and 0x7FFFFFFF)
            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val displayTitle = if (isMarathi) {
                notif.titleMr.ifBlank { notif.titleEn }
            } else {
                notif.titleEn.ifBlank { notif.titleMr }
            }

            val displayMessage = if (isMarathi) {
                notif.messageMr.ifBlank { notif.messageEn }
            } else {
                notif.messageEn.ifBlank { notif.messageMr }
            }

            val notificationId = requestCode

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(displayTitle)
                .setContentText(displayMessage)
                .setStyle(NotificationCompat.BigTextStyle().bigText(displayMessage))
                .setPriority(if (notif.isUrgent) NotificationCompat.PRIORITY_MAX else NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, builder.build())
            Log.d(TAG, "Successfully displayed public notification: $displayTitle")
        } catch (e: Exception) {
            Log.e(TAG, "Error showing public notification: ${e.message}", e)
        }
    }
}
