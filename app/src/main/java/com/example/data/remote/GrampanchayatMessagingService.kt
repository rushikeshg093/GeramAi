package com.example.data.remote

import android.util.Log
import com.example.data.local.NotificationItem
import com.example.data.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class GrampanchayatMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "GPMessagingService"

        fun subscribeToPublicTopics() {
            try {
                FirebaseMessaging.getInstance().subscribeToTopic("public_alerts")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Subscribed to public_alerts topic successfully")
                        }
                    }
                FirebaseMessaging.getInstance().subscribeToTopic("water_supply_alerts")
                FirebaseMessaging.getInstance().subscribeToTopic("panchayat_notices")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to subscribe to FCM topics: ${e.message}")
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM Token received: $token")
        subscribeToPublicTopics()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Received message from: ${remoteMessage.from}")

        val data = remoteMessage.data
        val category = data["category"] ?: "PUBLIC_ALERT"

        // STRICT ENFORCEMENT: Never send or display notifications for private citizen data
        if (!NotificationHelper.isPublicCitizenFacingChange(category)) {
            Log.d(TAG, "Skipping notification: category '$category' is personal/private or internal admin.")
            return
        }

        val titleMr = data["titleMr"] ?: remoteMessage.notification?.title ?: "ग्रामपंचायत सूचना"
        val titleEn = data["titleEn"] ?: data["titleMr"] ?: "Grampanchayat Notification"
        val messageMr = data["messageMr"] ?: remoteMessage.notification?.body ?: ""
        val messageEn = data["messageEn"] ?: data["messageMr"] ?: ""
        val targetScreen = data["targetScreen"] ?: "NOTIFICATIONS"
        val targetId = data["targetId"] ?: ""
        val wardNumber = data["wardNumber"]?.toIntOrNull()
        val isUrgent = data["isUrgent"]?.toBooleanStrictOrNull() ?: false

        val notifItem = NotificationItem(
            id = data["id"] ?: "fcm_${System.currentTimeMillis()}",
            titleMr = titleMr,
            titleEn = titleEn,
            messageMr = messageMr,
            messageEn = messageEn,
            timestamp = "नुकतेच",
            isUrgent = isUrgent,
            targetScreen = targetScreen,
            targetId = targetId,
            wardNumber = wardNumber,
            createdAtEpoch = System.currentTimeMillis()
        )

        NotificationHelper.showPublicNotification(applicationContext, notifItem)
    }
}
