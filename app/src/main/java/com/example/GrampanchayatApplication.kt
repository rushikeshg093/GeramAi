package com.example

import android.app.Application
import android.util.Log
import com.example.data.notification.NotificationHelper
import com.example.data.remote.GrampanchayatMessagingService
import com.google.firebase.FirebaseApp

class GrampanchayatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFirebase()
        try {
            NotificationHelper.createNotificationChannel(this)
            GrampanchayatMessagingService.subscribeToPublicTopics()
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring notifications: ${e.message}")
        }
    }

    private fun initializeFirebase() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val app = FirebaseApp.initializeApp(this)
                if (app != null) {
                    Log.i(TAG, "Firebase initialized successfully with app name: ${app.name}")
                } else {
                    Log.w(TAG, "FirebaseApp.initializeApp returned null. Check google-services.json.")
                }
            } else {
                Log.i(TAG, "Firebase already initialized by FirebaseInitProvider.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize FirebaseApp: ${e.message}", e)
        }
    }

    companion object {
        private const val TAG = "GrampanchayatApp"
    }
}
